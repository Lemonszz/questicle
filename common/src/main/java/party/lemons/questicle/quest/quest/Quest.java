package party.lemons.questicle.quest.quest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.reward.RewardType;

import java.util.List;

public interface Quest
{
    List<Goal> goals();
    String displayName();
    String description();
    QuestIcon icon();
    @NotNull List<QuestDependency> dependencies();

    @NotNull List<Reward> rewards();
    ResourceLocation id();
    void setID(ResourceLocation id);
    QuestType<?> type();

    default boolean isQuestAvailable(QuestParty party)
    {
        return isQuestAvailable(party.getStorage());
    }

    default boolean isQuestAvailable(PartyStorage storage)
    {
        if(storage.getQuestProgress(this).isCompleted())
            return false;

        if(dependencies().isEmpty())
            return true;

        for(QuestDependency dependency : dependencies())
        {
            if(dependency.required() && !storage.getQuestProgress(Quests.quests.get(dependency.quest())).isCompleted())
                return false;
        }

        return true;
    }

    default QuestStatus getQuestStatus(PartyStorage storage)
    {
        if(storage.getQuestProgress(this).isCompleted())
            return QuestStatus.COMPLETE;

        if(isQuestAvailable(storage))
            return QuestStatus.AVAILABLE;

        return QuestStatus.UNAVAILABLE;
    }

    default boolean checkCompleteness(QuestStorage questStorage){
        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal))
                return false;
        }
        return true;
    }

    default void onLivingDeath(QuestParty party, PartyStorage storage, ServerPlayer killer, LivingEntity killed)
    {
        QuestStorage questStorage = storage.getQuestProgress(this);
        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal)) {
                if(goal.onEntityKilled(this, questStorage, party, killer, killed))
                {
                    if(questStorage.setGoalComplete(goal)) {
                        storage.onQuestComplete(this);
                    }
                }
            }
        }
    }
    default void onInventoryChanged(QuestParty party, PartyStorage storage, ServerPlayer player, ItemStack stack)
    {
        QuestStorage questStorage = storage.getQuestProgress(this);
        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal)) {
                if(goal.onInventoryChanged(this, questStorage, party, player, stack))
                {
                    if(questStorage.setGoalComplete(goal))
                        storage.onQuestComplete(this);
                }
            }
        }
    }

    default void onDimensionChanged(QuestParty party, PartyStorage storage, ServerPlayer player, ResourceLocation dimension)
    {
        QuestStorage questStorage = storage.getQuestProgress(this);
        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal)) {
                if(goal.onDimensionChanged(this, questStorage, party, player, dimension))
                {
                    if(questStorage.setGoalComplete(goal))
                        storage.onQuestComplete(this);
                }
            }
        }
    }

    default void giveRewards(ServerPlayer pl){
        for(Reward reward : rewards())
        {
            reward.awardTo(pl);
        }
    }
}
