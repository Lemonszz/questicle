package party.lemons.questicle.client.icon;

import org.jetbrains.annotations.Nullable;
import party.lemons.questicle.client.gui.GuiObject;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.icon.QuestIcon;

public interface IconRenderer<T extends QuestIcon> extends GuiObject<IconRenderer.IconRendererContext<T>>
{
    record IconRendererContext<T extends QuestIcon>(T icon, @Nullable QuestFrame frame, float zoom)
    {
        public static <T extends QuestIcon> IconRendererContext<T> create(T icon, @Nullable QuestFrame frame, float zoom)   //TODO: this method is pointless
        {
            return new IconRendererContext<>(icon ,frame, zoom);
        }

        public int getFrameWidth()
        {
            if(frame() == null)
                return 0;

            return frame().width();
        }

        public int getFrameHeight()
        {
            if(frame() == null)
                return 0;

            return frame().height();
        }
    }

    @Override
    default int getWidth(IconRendererContext<T> context) {
        if(context.frame == null)
            return 16;

        return 26;
    }

    @Override
    default int getHeight(IconRendererContext<T> context) {
        if(context.frame == null)
            return 16;

        return 26;
    }
}
