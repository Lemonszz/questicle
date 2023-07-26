package party.lemons.questicle.client.gui.renderable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record NineSliceTexture(ResourceLocation location, int texX, int texY, int texWidth, int texHeight, int borderX, int borderY, int sheetWidth, int sheetHeight, boolean fill) implements RenderComponent
{
    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        //graphics.blitNineSliced(location(), x, y, width, height, borderX, borderX, texWidth, texHeight, texX, texY);  SLOW

        //top
        graphics.blit(location, x + borderX(), y, width - (borderX() * 2), borderY(), texX + borderX(), texY, texWidth - (borderX() * 2), borderY(), sheetWidth, sheetHeight);
        //bottom
        graphics.blit(location, x + borderX(), y + height - borderY(), width - (borderX() * 2), borderY(), texX + borderX(), texY + (texHeight - borderY()), texWidth - (borderX() * 2), borderY(), sheetWidth, sheetHeight);

        //l
        graphics.blit(location, x, y + borderY(), borderX, height - (borderY() * 2), texX, texY + borderY(), borderX(), texHeight - (borderY() * 2), sheetWidth, sheetHeight);
        //r
        graphics.blit(location, x + width - (borderX()), y + borderY(), borderX, height - (borderY() * 2), texX + texWidth - borderX(), texY + borderY(), borderX(), texHeight - (borderY() * 2), sheetWidth, sheetHeight);


        //Corners
        graphics.blit(location, x, y, texX, texY, borderX, borderY, sheetWidth, sheetHeight);
        graphics.blit(location, x + width - borderX, y, texX + (texWidth - borderX()), texY, borderX, borderY, sheetWidth, sheetHeight);

        graphics.blit(location, x, y + height - borderY, texX, texY + (texHeight - borderY()), borderX, borderY, sheetWidth, sheetHeight);
        graphics.blit(location, x + width - borderX, y + height - borderY, texX + (texWidth - borderX()), texY + (texHeight - borderY()), borderX, borderY, sheetWidth, sheetHeight);

        //fill
        if(fill)
            graphics.blit(location, x + borderX(), y + borderY(), width - (borderX() * 2), height - (borderY() * 2), texX() + borderX(), texY() + borderY(), texWidth - (borderX() * 2), texHeight - (borderY() * 2), sheetWidth, sheetHeight);
    }
}