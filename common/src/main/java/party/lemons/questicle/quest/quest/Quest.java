package party.lemons.questicle.quest.quest;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.impl.LocationGoal;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.quest.reward.Reward;

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
            if(!dependency.required())
                continue;

            //TODO: need a centralised way of getting the quest map rather than platform checks?
            if(Platform.getEnv() == EnvType.CLIENT)
                return !storage.getQuestProgress(ClientStorage.clientQuests.get(dependency.quest())).isCompleted();

            return !storage.getQuestProgress(Quests.quests.get(dependency.quest())).isCompleted();
        }

        return true;
    }

    public default void onMadeAvailable(QuestParty party)
    {
        QuestStorage questStorage = party.getStorage().getQuestProgress(this);

        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal)) {
                if(goal.onMadeAvailable(party, questStorage))
                {
                    if(questStorage.setGoalComplete(goal)) {
                        party.getStorage().onQuestComplete(this);
                    }
                }
            }
        }
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
                    if(questStorage.setGoalComplete(goal)) {
                        storage.onQuestComplete(this);

                        /*
                                TODO: Loop through the quests again so any new active quest the player has the items for already is completed.
                                The problem is that Collect goals will only check the stack parameter, so we'd have to loop though every item in the inventory?
                                So this for a collect quest when it becomes active?
                         */
                    }
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

    default void checkLocation(QuestParty questParty, PartyStorage storage, LocationGoal.LocationContext ctx)
    {
        QuestStorage questStorage = storage.getQuestProgress(this);
        for(Goal goal : goals())
        {
            if(!questStorage.isGoalComplete(goal)) {
                if(goal.checkLocation(this, questStorage, ctx))
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
