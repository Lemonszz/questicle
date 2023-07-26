package party.lemons.questicle.client.frame;

import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;

import java.util.HashMap;
import java.util.Map;

public class FrameRenderers
{
    private static Map<ResourceLocation, FrameRenderer<?>> renderers = new HashMap<>();

    public static <T extends QuestFrame> void registerRenderer(QuestFrameType<T> type, FrameRenderer<T> renderer)
    {
        renderers.put(QuestFrameTypes.REGISTRY.getId(type), renderer);
    }

    public static <T extends QuestFrame> FrameRenderer<T> getRenderer(QuestFrameType<T> type)
    {
        ResourceLocation id = QuestFrameTypes.REGISTRY.getId(type);

        return (FrameRenderer<T>) renderers.get(id);
    }
}
