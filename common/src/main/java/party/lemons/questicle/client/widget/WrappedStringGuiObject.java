package party.lemons.questicle.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import party.lemons.questicle.client.gui.GuiObject;

public class WrappedStringGuiObject implements GuiObject<WrappedStringGuiObject.StringContext> {


    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, StringContext context, int mouseX, int mouseY, float delta) {
        graphics.drawWordWrap(context.font, context.component, drawX, drawY, context.maxWidth, context.color);
    }

    @Override
    public int getHeight(StringContext context) {
        return context.font.wordWrapHeight(context.component, context.maxWidth);
    }

    public record StringContext(MutableComponent component, int maxWidth, int color, Font font)
    {
    }
}
