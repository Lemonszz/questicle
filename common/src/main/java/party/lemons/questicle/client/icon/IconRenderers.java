package party.lemons.questicle.client.icon;

import party.lemons.questicle.client.frame.FrameRenderer;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.QuestIconType;

import java.util.HashMap;
import java.util.Map;

public class IconRenderers
{
    private static Map<QuestIconType<?>, IconRenderer<?>> renderers = new HashMap<>();

    public static <T extends QuestIcon> void registerRenderer(QuestIconType<T> type, IconRenderer<T> renderer)
    {
        renderers.put(type, renderer);
    }

    public static <T extends QuestIcon> IconRenderer<T> getRenderer(QuestIconType<T> type)
    {
        return (IconRenderer<T>) renderers.get(type);
    }
}
