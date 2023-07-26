package party.lemons.questicle.party;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.network.S2cSyncParty;
import party.lemons.questicle.network.S2cSyncQuests;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.Quests;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyManager
{
    private static final Map<UUID, QuestParty> PARTIES = new HashMap<>();     //All loaded parties. UUID is the quest UUID
    private static final Map<UUID, QuestParty> PLAYER_PARTIES = new HashMap<>();  //Cache of Player UUID -> Party
    private static final Map<UUID, PartyInvitation> INVITES = new HashMap<>();    //Current invitations. TODO: needs to be cleared out occasionally

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void init()
    {
        LifecycleEvent.SERVER_STARTING.register(PartyManager::onServerStart);
        LifecycleEvent.SERVER_STOPPING.register(PartyManager::onServerStop);
        TickEvent.SERVER_POST.register(PartyManager::onServerTick);
        PlayerEvent.PLAYER_JOIN.register(PartyManager::onPlayerJoin);
        PlayerEvent.PLAYER_QUIT.register(PartyManager::onPlayerLeave);
    }

    /*
        Called when the Player Joins the server.
            - Finds the player's party and caches it.
            - If the player doesn't have a party, create a new one with them as the owner
     */
    private static void onPlayerJoin(ServerPlayer player)
    {
        for(QuestParty party : PARTIES.values())
        {
            if(party.isInParty(player.getUUID()))
            {
                PLAYER_PARTIES.put(player.getUUID(), party);
                return;
            }
        }

        //If reached here, player has no party, so create a blank one.
        //TODO: strategy for alternate party systems.
        QuestParty newParty = new QuesticleParty(player.getServer(), UUID.randomUUID(), player.getUUID());

        PARTIES.put(newParty.partyID(), newParty);
        PLAYER_PARTIES.put(player.getUUID(), newParty);

        new S2cSyncParty(newParty).sendTo(player);
    }

    /*
        Called when the player leaves the server
            - Removes them from the player -> party cache
     */
    private static void onPlayerLeave(ServerPlayer player)
    {
        PLAYER_PARTIES.remove(player.getUUID());
    }

    /*
        Called on server start
            - Loads parties from file
     */
    private static void onServerStart(MinecraftServer server)
    {
        load(server);
    }

    /*
        Called on server stop
            - Saves parties to file
    */
    private static void onServerStop(MinecraftServer server)
    {
        save(server);
        for(QuestParty party : PARTIES.values())
        {
            party.getStorage().save(server, true);
        }
    }

    private static void onServerTick(MinecraftServer server)
    {
        if(Quests.isDirty)
        {
            Quests.isDirty = false;
            new S2cSyncQuests().sendToAll(server);
        }

        if(server.getTickCount() % 42 == 0)
        {
            for(QuestParty party : PARTIES.values())
            {
                party.getStorage().save(server, false);
            }
        }
    }

    /***
     * Loads Parties from file. Will overwrite any existing data.
     * @param server
     */
    public static void load(MinecraftServer server)
    {
        PARTIES.clear();
        File partiesFile = getPartiesFile(server).toFile();

        //If we don't have a parties file, there's nothing to load so we return early.
        if(!partiesFile.exists())
        {
            LOGGER.info("No Questicles Parties Found");
            return;
        }

        //Load the parties from json
        try(FileReader reader = new FileReader(partiesFile))
        {
            JsonElement json = GSON.fromJson(reader, JsonElement.class);


            //This purposefully crashes if the json is bad so data isn't lost.
            //TODO: better error message
            List<QuestParty> parties = QuestPartyTypes.CODEC.listOf().decode(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error).getFirst();
            for(QuestParty party : parties) {
                PARTIES.put(party.partyID(), party);
                party.setStorage(PartyStorage.load(party, server));
            }

            LOGGER.info("Loaded {} Questicle parties", PARTIES.size());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("Unable to load Questicle Parties! Quest Progress may be lost.");
        }
    }

    /***
     * Saves parties to file
     * @param server
     */
    public static void save(MinecraftServer server)
    {
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        File partiesFile = getPartiesFile(server).toFile();
        if(!partiesFile.exists()) { //this is ugly - does java have a better way of doing this?
            try {
                Files.createDirectories(Questicle.getQuesticleDir(server));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Encode and save
        try(FileWriter writer = new FileWriter(partiesFile))
        {
            DataResult<JsonElement> element = QuestPartyTypes.CODEC.listOf().encodeStart(JsonOps.INSTANCE, PARTIES.values().stream().toList());
            if(element.error().isEmpty())
            {
                gson.toJson(element.result().get(), writer);
            }
            else {
                LOGGER.error("Unable to save Questicle Parties! Quest Progress may be lost.");
                LOGGER.error(element.error().get().message());
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /***
     * Gets the path to to the questicle parties file
     * @param server
     * @return parties file path
     */
    public static Path getPartiesFile(MinecraftServer server)
    {
        return Questicle.getQuesticleDir(server).resolve("parties.json");
    }

    /***
     * Gets the Party instance for the given player UUID
     * @param id
     * @return Party
     */
    public static QuestParty getPlayerParty(UUID id)
    {
        return PLAYER_PARTIES.get(id);
    }

    public static QuestParty getPlayerParty(ServerPlayer player)
    {
        return PLAYER_PARTIES.get(player.getUUID());
    }

    /***
     * Sends an invitation to the target player
     * @param server
     * @param from  invite sender
     * @param target target player
     * @return success
     */
    public static boolean invitePlayer(MinecraftServer server, ServerPlayer from, ServerPlayer target)
    {
        QuestParty currentTargetParty = getPlayerParty(target.getUUID());  //Target's current party
        QuestParty invitingParty = getPlayerParty(from.getUUID()); //The inviting party

        if(currentTargetParty.equals(invitingParty))    //If the target is in the same party as the sender, fail.
            return false;

        PartyInvitation currentInvite = INVITES.getOrDefault(target.getUUID(), null);   //Players current invite
        if(currentInvite != null && !currentInvite.hasTimedOut(server)) //If the player has a currently active invite, fail
            return false;

        //Invite is valid - create and send to player

        INVITES.put(target.getUUID(), new PartyInvitation(invitingParty.partyID(), server.getTickCount()));

        //This is here and not in the command, so it can be reused later in a UI or something for this.
        target.sendSystemMessage(Component.translatable("questicle.text.invited", from.getDisplayName().copy().withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GOLD));
        target.sendSystemMessage(Component.translatable("questicle.text.invited.command",
                                Component.literal("/qparty accept").withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE)
                        )
                        .withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE)
                        .withStyle((s)->s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/qparty accept")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("questicle.text.invited.action"))))
        );

        return true;
    }

    /***
     * Cancled an invitation send to a player
     * @param server
     * @param from      Player cancelling the invite
     * @param target    Player to have the invite cancelled
     * @return success
     */
    public static boolean cancelInvitePlayer(MinecraftServer server, UUID from, ServerPlayer target)
    {
        PartyInvitation invitation = INVITES.getOrDefault(target.getUUID(), null);
        if(invitation != null && (invitation.hasTimedOut(server) && PARTIES.get(invitation.party).isInParty(from)))
        {
            INVITES.put(target.getUUID(), null);
            return true;
        }
        return false;
    }

    /***
     * Accepts and joins party for a pending invite
     * @param server
     * @param accepting accepting player
     * @return success
     */
    public static boolean acceptInvitation(MinecraftServer server, ServerPlayer accepting)
    {
        PartyInvitation invitation = INVITES.getOrDefault(accepting.getUUID(), null);
        if(invitation != null && !invitation.hasTimedOut(server))
        {
            return addPartyMember(server, invitation.party(), accepting.getUUID());
        }

        return false;
    }

    /***
     * Deletes a party if it no longer contains any members
     * @param server
     * @param partyID
     */
    public static void deregisterParty(MinecraftServer server, UUID partyID)
    {
        QuestParty party = PARTIES.get(partyID);
        if(party != null && !party.partyMembers().isEmpty())
        {
            LOGGER.error("Tried to deregister party {} that currently has members", partyID);
        }

        PARTIES.remove(partyID);
        save(server);
    }

    /***
     * Adds a given party member to the party.
     * Will deregister the target's current party if it is now empty
     * @param server
     * @param partyID
     * @param playerID
     * @return success
     */
    public static boolean addPartyMember(MinecraftServer server, UUID partyID, UUID playerID)
    {
        QuestParty party = PARTIES.get(partyID);
        if(party.addMember(playerID))
        {
            QuestParty currentParty = PLAYER_PARTIES.get(playerID);
            if(currentParty != null)
                currentParty.removeMember(server, playerID);

            PLAYER_PARTIES.put(playerID, party);
            save(server);

            ServerPlayer player = server.getPlayerList().getPlayer(playerID);
            if(player != null)
                new S2cSyncParty(party).sendTo(player);
            return true;
        }
        return false;
    }

    public static QuestParty getParty(UUID partyID)
    {
        return PARTIES.get(partyID);
    }

    private record PartyInvitation(UUID party, int inviteTime)
    {
        static final int MAX_INVITE_TIME = 60 * 20;

        public boolean hasTimedOut(MinecraftServer server)
        {
            return server.getTickCount() - inviteTime > MAX_INVITE_TIME;
        }
    }
}
