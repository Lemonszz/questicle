package party.lemons.questicle.client.tooltip.goal;

import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;

import java.util.HashMap;
import java.util.Map;

public class GoalDisplayRegistry
{
    private static Map<GoalType<?>, GoalDisplay<?>> displays = new HashMap<>();

    public static <T extends Goal> void register(GoalType<T> type, GoalDisplay<T> tooltip)
    {
        displays.put(type, tooltip);
    }

    public static <T extends Goal> GoalDisplay<T> getTooltip(GoalType<T> type)
    {
        return (GoalDisplay<T>) displays.get(type);
    }
}
