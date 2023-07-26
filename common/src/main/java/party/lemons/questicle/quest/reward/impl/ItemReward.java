package party.lemons.questicle.quest.reward.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.reward.RewardType;
import party.lemons.questicle.quest.reward.RewardTypes;
import party.lemons.questicle.util.QCodecs;

import java.util.List;

public record ItemReward(List<ItemStack> itemStacks) implements Reward
{
    public static final Codec<ItemReward> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            QCodecs.SIMPLE_ITEM_STACK.listOf().fieldOf("items").forGetter(ItemReward::itemStacks)
                    )
                    .apply(instance, ItemReward::new));

    @Override
    public RewardType<?> type() {
        return RewardTypes.ITEM.get();
    }

    @Override
    public void awardTo(ServerPlayer player) {
        for(ItemStack stack : itemStacks())
            player.getInventory().add(stack.copy());
    }
}
