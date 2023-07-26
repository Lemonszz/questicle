package party.lemons.questicle.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import party.lemons.questicle.util.QMath;

public interface GuiObject<T> {
    void render(GuiGraphics graphics, int drawX, int drawY, T context, int mouseX, int mouseY, float delta);

    default int getHeight(T context)
    {
        return 0;
    }

    default int getWidth(T context)
    {
        return 0;
    }

    default boolean isMouseOver(int drawX, int drawY, int mouseX, int mouseY, T context){
        return QMath.inArea(mouseX, mouseY, drawX, drawY, drawX + getWidth(context), drawY + getHeight(context));
    }

    default Renderable getTooltip(T context, int drawX, int drawY, int mouseX, int mouseY)
    {
        return null;
    }
}
