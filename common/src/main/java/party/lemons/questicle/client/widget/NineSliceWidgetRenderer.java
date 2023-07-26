package party.lemons.questicle.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.gui.renderable.NineSliceTexture;
import party.lemons.questicle.quest.widget.impl.NineSliceTextureWidget;

public class NineSliceWidgetRenderer implements WidgetRenderer<NineSliceTextureWidget> {
    @Override
    public void render(GuiGraphics g, NineSliceTextureWidget widget, int originX, int originY, int mouseX, int mouseY, float delta) {

        new NineSliceTexture(
                widget.texture(),
                widget.textureX(), widget.textureY(),
                widget.textureWidth(), widget.textureHeight(),
                widget.border(), widget.border(), widget.sheetWidth(),
                widget.sheetHeight(), widget.fill()
        ).render(g, originX + widget.x(), originY + widget.y(), widget.width(), widget.height());
    }
}
