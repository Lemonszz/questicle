package party.lemons.questicle.client.gui.renderable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record StandardTexture(ResourceLocation location, int texX, int texY, int texWidth, int texHeight, int sheetWidth, int sheetHeight) implements RenderComponent
{
    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height)
    {
        graphics.blit(location, x, y, width, height, texX(), texY(), texWidth(), texHeight(), sheetWidth(), sheetHeight());
    }

    public void render(GuiGraphics graphics, int x, int y)
    {
        graphics.blit(location, x, y, texWidth(), texHeight(), texX(), texY(), texWidth(), texHeight(), sheetWidth(), sheetHeight());
    }
}