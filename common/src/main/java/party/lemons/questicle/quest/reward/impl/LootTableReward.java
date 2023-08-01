package party.lemons.questicle.quest.reward.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.reward.RewardType;
import party.lemons.questicle.quest.reward.RewardTypes;
import party.lemons.questicle.util.QCodecs;
import party.lemons.questicle.util.QUtil;

import java.util.List;
import java.util.function.Consumer;

public record LootTableReward(ResourceLocation table) implements Reward
{
    public static final Codec<LootTableReward> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            ResourceLocation.CODEC.fieldOf("table").forGetter(LootTableReward::table)
                    )
                    .apply(instance, LootTableReward::new));

    @Override
    public RewardType<?> type() {
        return RewardTypes.LOOT_TABLE.get();
    }

    @Override
    public void awardTo(ServerPlayer player) {
        LootTable lootTable = player.getServer().getLootData().getLootTable(table());
        LootParams.Builder ctx = new LootParams.Builder(player.serverLevel()).withOptionalParameter(LootContextParams.ORIGIN, player.position());
        lootTable.getRandomItems(ctx.create(LootContextParamSets.CHEST), itemStack -> QUtil.giveOrDrop(player, itemStack));
    }
}
