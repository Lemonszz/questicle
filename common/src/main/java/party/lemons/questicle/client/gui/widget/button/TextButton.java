package party.lemons.questicle.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import party.lemons.questicle.client.gui.renderable.RenderComponent;

public class TextButton extends QButton
{
    private final RenderComponent normalTexture;
    private final RenderComponent hoverTexture;
    private final RenderComponent disabledTexture;
    private final Component component;

    public TextButton(Component component, RenderComponent normalTexture, RenderComponent hoverTexture, RenderComponent disabledTexture, int x, int y, int width, int height, AbstractButtonPress onPressed) {
        super(x, y, width, height, onPressed);

        this.component = component;
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.disabledTexture = disabledTexture;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        RenderComponent background;
        if (!isActive())
            background = disabledTexture;
        else if (isHovered())
            background = hoverTexture;
        else
            background = normalTexture;

        background.render(guiGraphics, getX(), getY(), getWidth(), getHeight());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, component, getX() + (getWidth() / 2), getY() + Minecraft.getInstance().font.lineHeight / 2, isActive() ? 0xFFFFFF : 0x3b3a3a);
    }
}
