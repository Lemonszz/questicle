package party.lemons.questicle.quest.reward;

import net.minecraft.server.level.ServerPlayer;

public interface Reward
{
    RewardType<?> type();

    void awardTo(ServerPlayer player);
}
