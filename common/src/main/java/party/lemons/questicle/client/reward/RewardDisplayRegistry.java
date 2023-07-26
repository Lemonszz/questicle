package party.lemons.questicle.client.reward;

import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.reward.RewardType;

import java.util.HashMap;
import java.util.Map;

public class RewardDisplayRegistry
{
    private static Map<RewardType<?>, RewardDisplay<?>> displays = new HashMap<>();

    public static <T extends Reward> void register(RewardType<T> type, RewardDisplay<T> tooltip)
    {
        displays.put(type, tooltip);
    }

    public static <T extends Reward> RewardDisplay<T> getDisplay(RewardType<T> type)
    {
        return (RewardDisplay<T>) displays.get(type);
    }
}
