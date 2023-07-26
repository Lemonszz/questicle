package party.lemons.questicle.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.quest.widget.impl.RepeatingTextureWidget;

public class RepeatingTextureWidgetRenderer implements WidgetRenderer<RepeatingTextureWidget> {

    @Override
    public void render(GuiGraphics g, RepeatingTextureWidget widget, int originX, int originY, int mouseX, int mouseY, float delta) {
        switch (widget.textureMode())
        {
            default -> renderDefault(g, widget, originX, originY, mouseX, mouseY, delta);
            case BLOCK_ATLAS -> renderBlockAtlas(g, widget, originX, originY, mouseX, mouseY, delta);
        }
    }

    private void renderBlockAtlas(GuiGraphics g, RepeatingTextureWidget widget, int originX, int originY, int mouseX, int mouseY, float delta)
    {
        final float epsilon = 0.0000001F;   //Adding/Subtracting this to the UV positions prevents UV bleeding in most situations
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite sprite = atlas.getSprite(widget.texture());
        DrawUtils.blitRepeating(g, TextureAtlas.LOCATION_BLOCKS, originX + widget.x(), originY + widget.y(), widget.width(), widget.height(), 16, 16, sprite.getU0() + epsilon, sprite.getV0() + epsilon,  sprite.getU1() - epsilon, sprite.getV1() - epsilon);
    }

    private void renderDefault(GuiGraphics g, RepeatingTextureWidget widget, int originX, int originY, int mouseX, int mouseY, float delta)
    {
        DrawUtils.blitRepeating(g, widget.texture(), originX + widget.x(), originY + widget.y(), widget.width(), widget.height(), 0, 0, (int)widget.textureWidth(), (int)widget.textureHeight());
    }

}
