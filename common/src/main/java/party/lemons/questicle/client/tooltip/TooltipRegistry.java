package party.lemons.questicle.client.tooltip;

import party.lemons.questicle.client.tooltip.quest.QuestTooltip;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestType;

import java.util.HashMap;
import java.util.Map;

public class TooltipRegistry
{
    private static Map<QuestType<?>, QuestTooltip<?>> tooltips = new HashMap<>();

    public static <T extends Quest> void register(QuestType<T> type, QuestTooltip<T> tooltip)
    {
        tooltips.put(type, tooltip);
    }

    public static <T extends Quest> QuestTooltip<T> getTooltip(QuestType<T> type)
    {
        return (QuestTooltip<T>) tooltips.get(type);
    }
}
