package party.lemons.questicle.party;

import dev.architectury.networking.simple.BaseS2CMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.network.S2cSyncParty;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.quest.Quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface QuestParty
{
    Set<UUID> partyMembers();
    UUID partyOwner();
    UUID partyID();

    default boolean isInParty(UUID player)
    {
        for(UUID id : partyMembers())
            if(id.equals(player))
                return true;

        return false;
    }

    PartyStorage getStorage();
    void setStorage(PartyStorage storage);

    default boolean addMember(UUID player)
    {
        try {
            return partyMembers().add(player);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    default boolean isOwner(UUID player)
    {
        return partyOwner().equals(player);
    }

    default void removeMember(MinecraftServer server, UUID playerID){
        partyMembers().remove(playerID);

        if(partyOwner().equals(playerID))
        {
            if(partyMembers().isEmpty())
                PartyManager.deregisterParty(server, partyID());
            else {
                setPartyOwner(partyMembers().stream().findFirst().get());
            }
        }
    }

    void setPartyOwner(UUID newOwner);

    default List<UUID> partyMemberList()
    {
        return List.copyOf(partyMembers());
    }

    QuestPartyType<?> type();


    default void onLivingDeath(ServerPlayer killer, LivingEntity killed)
    {
        for(Quest quest : getStorage().activeQuests())
        {
            quest.onLivingDeath(this, getStorage(), killer, killed);
        }
    }

    default void onInventoryChanged(ServerPlayer player, ItemStack stack)
    {
        for(Quest quest : getStorage().activeQuests())
        {
            quest.onInventoryChanged(this, getStorage(), player, stack);
        }
    }

    default void onDimensionChanged(ServerPlayer player, ResourceLocation dimension)
    {
        for(Quest quest : getStorage().activeQuests())
        {
            quest.onDimensionChanged(this, getStorage(), player, dimension);
        }
    }

    default List<ServerPlayer> getOnlinePlayers(MinecraftServer server)
    {
        List<ServerPlayer> players = new ArrayList<>();
        for(UUID playerID : partyMemberList())
        {
            ServerPlayer player = server.getPlayerList().getPlayer(playerID);
            if(player != null)
                players.add(player);
        }

        return players;
    }

    default void sendMessage(BaseS2CMessage message, MinecraftServer server)
    {
        getOnlinePlayers(server).forEach(message::sendTo);
    }
}
