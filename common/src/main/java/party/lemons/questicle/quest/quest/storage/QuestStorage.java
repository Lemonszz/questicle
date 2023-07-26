package party.lemons.questicle.quest.quest.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.quest.Quest;

import java.util.*;

public class QuestStorage
{
    public static Codec<QuestStorage> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("quest").forGetter(QuestStorage::getQuestID),
                    Codec.unboundedMap(Codec.STRING, CompoundTag.CODEC).fieldOf("progress").forGetter(QuestStorage::getProgressMap),
                    UUIDUtil.STRING_CODEC.listOf().fieldOf("claimed_players").forGetter(QuestStorage::getClaimedPlayers),
                    Codec.BOOL.fieldOf("completed").forGetter(QuestStorage::isCompleted)
            ).apply(instance, QuestStorage::new));

    private final ResourceLocation questID;
    private final Map<String, CompoundTag> progress;
    private final List<UUID> claimedPlayers;
    private boolean isCompleted;

    public QuestStorage(ResourceLocation questID, Map<String, CompoundTag> progress, List<UUID> claimedPlayers, boolean completed)
    {
        this.questID = questID;
        this.progress = new HashMap<>(progress);
        this.claimedPlayers = claimedPlayers;
        this.isCompleted = completed;
    }

    public QuestStorage(Quest quest)
    {
        this(quest.id(), new HashMap<>(), new ArrayList<>(), false);
    }

    public boolean isGoalComplete(String goal)
    {
        if(progress.containsKey(goal))
        {
            CompoundTag tag = progress.get(goal);
            return tag.contains("completed") && tag.getBoolean("completed");
        }
        return false;
    }

    public boolean isGoalComplete(Goal goal)
    {
        return isGoalComplete(goal.id());
    }

    public boolean setGoalComplete(Goal goal) {
        return setGoalComplete(goal.id());
    }

    public boolean setGoalComplete(String goal)
    {
        CompoundTag tag = getProgress(goal);
        tag.putBoolean("completed", true);

        if(Quests.quests.get(getQuestID()).checkCompleteness(this))
        {
            isCompleted = true;
            return true;
        }
        return false;
    }

    public CompoundTag getProgress(String goal)
    {
        return progress.computeIfAbsent(goal, (g)->new CompoundTag());
    }

    public CompoundTag getProgress(Goal goal)
    {
        return getProgress(goal.id());
    }


    public ResourceLocation getQuestID() {
        return questID;
    }

    public Map<String, CompoundTag> getProgressMap() {
        return progress;
    }

    public List<UUID> getClaimedPlayers() {
        return claimedPlayers;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean hasPlayerClaimed(Player player)
    {
        return getClaimedPlayers().contains(player.getUUID());
    }

    public void setClaimed(ServerPlayer pl)
    {
        getClaimedPlayers().add(pl.getUUID());
    }
}
