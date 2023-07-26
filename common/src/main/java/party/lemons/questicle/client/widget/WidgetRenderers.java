package party.lemons.questicle.client.widget;

import party.lemons.questicle.quest.widget.Widget;
import party.lemons.questicle.quest.widget.WidgetType;

import java.util.HashMap;
import java.util.Map;

public class WidgetRenderers
{
    private static Map<WidgetType<?>, WidgetRenderer<?>> renderers = new HashMap<>();

    public static <T extends Widget> void registerRenderer(WidgetType<T> type, WidgetRenderer<T> renderer)
    {
        renderers.put(type, renderer);
    }

    public static <T extends Widget> WidgetRenderer<T> getRenderer(WidgetType<T> type)
    {
        return (WidgetRenderer<T>) renderers.get(type);
    }
}
