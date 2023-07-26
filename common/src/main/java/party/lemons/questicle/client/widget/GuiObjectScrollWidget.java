package party.lemons.questicle.client.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import party.lemons.questicle.client.gui.ClickableObject;
import party.lemons.questicle.client.gui.GuiObject;
import party.lemons.questicle.client.gui.QComponents;
import party.lemons.questicle.client.gui.renderable.StandardTexture;
import party.lemons.questicle.util.QMath;

public class GuiObjectScrollWidget<T> extends AbstractScrollWidget implements TooltipProvider
{
    private final GuiObject<T> object;
    private final T context;
    private boolean isDragScrolling = false;
    protected Renderable tooltip = null;

    protected GuiObject<T> hoverObject;
    protected T hoverContext;

    public GuiObjectScrollWidget(int x, int y, int width, int height, GuiObject<T> object, T ctx)
    {
        super(x, y, width, height, Component.empty());
        this.object = object;
        this.context = ctx;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }

    @Override
    public int getInnerHeight()
    {
        return object.getHeight(context);
    }

    @Override
    protected double scrollRate() {
        return 9.0D;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        object.render(guiGraphics, getX(), getY(), context, mouseX, (int)(mouseY - scrollAmount()), delta);
        if(object.isMouseOver(getX(), getY(), mouseX, mouseY, context))
        {
            tooltip = object.getTooltip(context, getX(), getY(), mouseX, mouseY);
            this.hoverObject = object;
            this.hoverContext = context;
        }
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            this.hoverObject = null;
            this.hoverContext = null;
            tooltip = null;
            g.enableScissor(this.getX(), this.getY(), this.getX() + this.width - 1, this.getY() + this.height - 1);
            g.pose().pushPose();
            g.pose().translate(0.0, -this.scrollAmount(), 0.0);
            this.renderContents(g, mouseX, mouseY, delta);
            g.pose().popPose();
            g.disableScissor();
            this.renderDecorations(g, mouseX, mouseY);
        }
    }

    protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        if(scrollbarVisible()) {
            renderScrollBar(guiGraphics, mouseX, mouseY);
            guiGraphics.fill(getX() - 2, getY() - 1, getX() + width-1, getY(), -8355712);

            int contentHeight = getAdjustedHeight();
            guiGraphics.fill(getX() - 2, getY() +contentHeight + 1, getX() + width-1, getY() + contentHeight, -8355712);
        }
    }

    public int getAdjustedHeight()
    {
        return Math.min(getInnerHeight(), getHeight());
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double scrollX, double scrollY)
    {
        if(scrollbarVisible() && isDragScrolling)
        {
            if (mouseY < (double)this.getY()) {
                this.setScrollAmount(0.0);
            } else if (mouseY > (double)(this.getY() + this.height)) {
                this.setScrollAmount(this.getMaxScrollAmount());
            } else {
                int scrollBarHeight = this.scrollBarHeight();
                double speed = Math.max(1, this.getMaxScrollAmount() / (this.height - scrollBarHeight));
                this.setScrollAmount(this.scrollAmount() + scrollY * speed);
            }
            return true;
        }

        return false;
    }

    @Override
    protected void setScrollAmount(double d) {
        super.setScrollAmount(d);
        setTooltip(null);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(button == InputConstants.MOUSE_BUTTON_LEFT) {
            if (scrollbarVisible() && isOverScrollbar((int) mouseX, (int) mouseY)) {
                isDragScrolling = true;
                return true;
            }else if(hoverObject != null && hoverObject instanceof ClickableObject clickable && clickable.isClickable(hoverContext))
            {
                clickable.onClick(hoverContext);
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double d, double e, int button) {
        if(isDragScrolling && button == InputConstants.MOUSE_BUTTON_LEFT) {
            isDragScrolling = false;
            return true;
        }

        return false;
    }

    private void renderScrollBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        StandardTexture component = QComponents.WIDGET_SCROLLBAR;
        if(isOverScrollbar(mouseX, mouseY) || isDragScrolling)
            component = QComponents.WIDGET_SCROLLBAR_HOVER;

        int drawX = getScrollbarX();
        int drawY = getScrollbarY();

        guiGraphics.fill(drawX + 3, getY() - 1, drawX + 4, getY() + getHeight(), -8355712);
        component.render(guiGraphics, drawX, drawY);
    }

    public int getScrollbarX()
    {
        return this.getX() + this.width - scrollBarWidth() - 1;
    }

    public int getScrollbarY()
    {
        return Math.max(this.getY(), (int)this.scrollAmount() * (this.height - scrollBarHeight()) / this.getMaxScrollAmount() + this.getY());
    }

    public int scrollBarHeight()
    {
        return 16;
    }

    public int scrollBarWidth()
    {
        return 6;
    }

    public boolean isOverScrollbar(int x, int y)
    {
        int scrollY = getScrollbarY();

        return QMath.inArea(x, y, getScrollbarX(), scrollY, getScrollbarX() + 6, scrollY + scrollBarHeight());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public Renderable getWidgetTooltip() {
        return tooltip;
    }
}
