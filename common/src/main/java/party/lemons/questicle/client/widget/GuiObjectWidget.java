package party.lemons.questicle.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import party.lemons.questicle.client.gui.GuiObject;

public class GuiObjectWidget<T> extends AbstractWidget implements TooltipProvider
{
    private final GuiObject<T> object;
    private final T context;
    private Renderable tooltip = null;

    public GuiObjectWidget(GuiObject<T> object, T context, int x, int y)
    {
        super(x, y, object.getWidth(context), object.getHeight(context), Component.empty());
        this.object = object;
        this.context = context;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        tooltip = null;

        object.render(guiGraphics, getX(), getY(), context, mouseX, mouseY, delta);

        if(isHovered())
            tooltip = object.getTooltip(context, getX(), getY(), mouseX, mouseY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public @Nullable Renderable getWidgetTooltip() {
        return tooltip;
    }
}
