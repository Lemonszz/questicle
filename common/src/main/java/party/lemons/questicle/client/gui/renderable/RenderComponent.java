package party.lemons.questicle.client.gui.renderable;

import net.minecraft.client.gui.GuiGraphics;

public interface RenderComponent {
    void render(GuiGraphics graphics, int x, int y, int width, int height);
}
