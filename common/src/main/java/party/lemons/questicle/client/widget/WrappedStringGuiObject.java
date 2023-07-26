package party.lemons.questicle.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import party.lemons.questicle.client.gui.GuiObject;

public class WrappedStringGuiObject implements GuiObject<WrappedStringGuiObject.StringContext> {


    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, StringContext context, int mouseX, int mouseY, float delta) {
        graphics.drawWordWrap(Minecraft.getInstance().font, context.component, drawX, drawY, context.maxWidth, context.color);
    }

    @Override
    public int getHeight(StringContext context) {
        return Minecraft.getInstance().font.wordWrapHeight(context.component, context.maxWidth);
    }

    public record StringContext(Component component, int maxWidth, int color)
    {
    }
}
