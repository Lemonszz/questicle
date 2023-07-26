package party.lemons.questicle.party.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import party.lemons.questicle.network.S2cSyncParty;
import party.lemons.questicle.party.PartyManager;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestStatus;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PartyStorage
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Codec<PartyStorage> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.STRING_CODEC.fieldOf("party").forGetter(i->i.getParty().partyID()),
                    Codec.unboundedMap(ResourceLocation.CODEC, QuestStorage.CODEC).fieldOf("progress").forGetter(PartyStorage::getProgress)
            ).apply(instance, PartyStorage::new));


    public static final LevelResource STORAGE_DIR = new LevelResource("questicle/storage");   //Directory in the world folder to save data

    private final QuestParty party;
    private final Map<ResourceLocation, QuestStorage> questProgress = new HashMap<>();
    private final List<Quest> activeQuests = new ArrayList<>();
    private boolean dirty, refreshActive;

    public PartyStorage(UUID partyID, Map<ResourceLocation, QuestStorage> questProgress)
    {
        this.party = PartyManager.getParty(partyID);
        this.questProgress.putAll(questProgress);
        refreshActiveQuests();
    }

    public PartyStorage(QuestParty party)
    {
        this.party = party;
        refreshActiveQuests();
    }

    public QuestStorage getQuestProgress(Quest quest)
    {
        return questProgress.computeIfAbsent(quest.id(), (id)-> new QuestStorage(quest));
    }

    public boolean isQuestCompleted(Quest quest)
    {
        return getQuestProgress(quest).isCompleted();
    }

    public boolean hasPendingRewards(Player player, Quest quest)
    {
        return !getQuestProgress(quest).hasPlayerClaimed(player);
    }
    public void requestRewardClaim(ServerPlayer pl, Quest quest)
    {
        if(quest.getQuestStatus(this).equals(QuestStatus.COMPLETE) && hasPendingRewards(pl, quest))
        {
            quest.giveRewards(pl);
            getQuestProgress(quest).setClaimed(pl);
        }
    }
    public void requestAllRewardClaims(ServerPlayer pl)
    {
        for(Quest quest : Quests.quests.values())
        {
            requestRewardClaim(pl, quest);
        }
    }


    public static PartyStorage load(QuestParty party, MinecraftServer server)
    {
        //TODO: this is fairly ugly

        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        File storageDir = getStorageDir(server).toFile();
        if(!storageDir.exists()) {
            try {
                Files.createDirectories(getStorageDir(server));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File partyStorage = getPartyStoragePath(server, party).toFile();
        if(!partyStorage.exists())
            return new PartyStorage(party);

        try(FileReader reader = new FileReader(partyStorage))
        {
            JsonElement json = gson.fromJson(reader, JsonElement.class);


            DataResult<Pair<PartyStorage, JsonElement>> storageData = PartyStorage.CODEC.decode(JsonOps.INSTANCE, json);
            if(storageData.error().isPresent()) {
                LOGGER.error("Error loading Party Storage for {}, Data may be lost", storageData.error().get().message());
                return new PartyStorage(party);
            }

            return storageData.get().left().get().getFirst();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new PartyStorage(party);
    }

    public void save(MinecraftServer server, boolean force)
    {
        if(!dirty && !force)
            return;

        party.sendMessage(new S2cSyncParty(getParty()), server);

        dirty = false;
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        File storageDir = getStorageDir(server).toFile();
        if(!storageDir.exists()) {
            try {
                Files.createDirectories(getStorageDir(server));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File partyStorage = getPartyStoragePath(server, party).toFile();
        //Encode and save
        try(FileWriter writer = new FileWriter(partyStorage))
        {
            DataResult<JsonElement> element = PartyStorage.CODEC.encodeStart(JsonOps.INSTANCE, this);
            if(element.error().isEmpty())
            {
                gson.toJson(element.result().get(), writer);
            }
            else {
                LOGGER.error("Unable to save Party Storage {}! Quest Progress may be lost.", getParty().partyID());
                LOGGER.error(element.error().get().message());
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Map<ResourceLocation, QuestStorage> getProgress() {
        return questProgress;
    }

    public QuestParty getParty() {
        return party;
    }

    public static Path getPartyStoragePath(MinecraftServer server, QuestParty party)
    {
        return server.getWorldPath(STORAGE_DIR).resolve(party.partyID().toString() + ".json");
    }
    public static Path getStorageDir(MinecraftServer server)
    {
        return server.getWorldPath(STORAGE_DIR);
    }

    public List<Quest> activeQuests() {
        if(refreshActive)
            refreshActiveQuests();

        return activeQuests;
    }

    private void refreshActiveQuests()
    {
        activeQuests.clear();

        for(Quest quest : Quests.quests.values())
        {
            QuestStorage storage = getQuestProgress(quest);
            if(storage.isCompleted())
                continue;

            if(quest.isQuestAvailable(this))
                activeQuests.add(quest);
        }
    }

    public void markDirty()
    {
        this.dirty = true;
    }

    public void onQuestComplete(Quest quest)
    {
        markDirty();
        refreshActive();
    }

    public void refreshActive()
    {
        refreshActive = true;
    }
}
