package party.lemons.questicle.quest.goal;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

public interface Goal
{
    GoalType<?> type();

    String id();

    default QuestStorage getStorage(QuestParty party, Quest quest)
    {
        return party.getStorage().getQuestProgress(quest);
    }

    default boolean onEntityKilled(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer killer, LivingEntity killed)
    {
        return false;
    }

    default boolean onInventoryChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ItemStack stack)
    {
        return false;
    }

    default boolean onDimensionChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ResourceLocation newDimension)
    {
        return false;
    }

    Component getHoverTooltip(QuestStorage questStorage);
}
