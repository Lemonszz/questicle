package party.lemons.questicle.client.gui.widget.button;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.gui.renderable.RenderComponent;

public class IconButton extends QButton {

    private final RenderComponent normalTexture;
    private final RenderComponent hoverTexture;
    private final RenderComponent disabledTexture;
    public IconButton(RenderComponent normalTexture, RenderComponent hoverTexture, RenderComponent disabledTexture, int x, int y, int width, int height, AbstractButtonPress onPressed)
    {
        super(x, y, width, height, onPressed);

        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.disabledTexture = disabledTexture;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        RenderComponent component;
        if (!isActive())
            component = disabledTexture;
        else if (isHovered())
            component = hoverTexture;
        else
            component = normalTexture;

        component.render(guiGraphics, getX(), getY(), getWidth(), getHeight());
    }
}
