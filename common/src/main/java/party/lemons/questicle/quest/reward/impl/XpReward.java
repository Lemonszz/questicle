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

public record XpReward(int xp, boolean isLevels) implements Reward
{
    public static final Codec<XpReward> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                        Codec.INT.fieldOf("xp").forGetter(XpReward::xp),
                        Codec.BOOL.optionalFieldOf("is_levels", true).forGetter(XpReward::isLevels)
                    )
                    .apply(instance, XpReward::new));

    @Override
    public RewardType<?> type() {
        return RewardTypes.XP.get();
    }

    @Override
    public void awardTo(ServerPlayer player) {
        if(isLevels())
            player.giveExperienceLevels(xp());
        else
            player.giveExperiencePoints(xp);
    }
}
