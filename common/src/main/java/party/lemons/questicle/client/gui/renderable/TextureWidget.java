package party.lemons.questicle.client.gui.renderable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

public record TextureWidget(RenderComponent texture, int x, int y, int width, int height) implements Renderable {

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        texture.render(guiGraphics, x(), y(), width(), height);
    }
}
