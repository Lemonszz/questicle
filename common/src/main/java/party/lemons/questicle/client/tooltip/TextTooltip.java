package party.lemons.questicle.client.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

public record TextTooltip(Component component) implements Renderable {

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float delta)
    {
        guiGraphics.renderTooltip(Minecraft.getInstance().font, component, x, y);
    }
}