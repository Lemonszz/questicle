package party.lemons.questicle.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.widget.Widget;

public interface WidgetRenderer<T extends Widget>
{
    void render(GuiGraphics g, T widget, int originX, int originY, int mouseX, int mouseY, float delta);
}
