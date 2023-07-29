package party.lemons.questicle.client.gui.widget;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.gui.GuiObject;

import java.util.ArrayList;
import java.util.List;

public class ListGuiObjectScrollWidget<T> extends GuiObjectScrollWidget<T>
{
    private final List<Pair<GuiObject<T>, T>> objects = new ArrayList<>();
    private final int gap;

    private int height = -1;
    private boolean heightDirty = true;

    public ListGuiObjectScrollWidget(int x, int y, int width, int height, int gap) {
        super(x, y, width, height, null, null);

        this.gap = gap;
    }

    public ListGuiObjectScrollWidget<T> add(GuiObject<T> obj, T ctx)
    {
        objects.add(Pair.of(obj, ctx));
        return this;
    }

    @Override
    public int getInnerHeight()
    {
        if(heightDirty)
        {
            heightDirty = false;
            height = 0;

            for(Pair<GuiObject<T>, T> obj : objects)
            {
                height += obj.getFirst().getHeight(obj.getSecond()) + gap;
            }
            height -= gap;
        }

        return height;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        int drawY = getY();

        for(Pair<GuiObject<T>, T> obj : objects)
        {
            GuiObject<T> guiObj = obj.getFirst();
            if(guiObj == null)
                return;

            T ctx = obj.getSecond();
            int height = guiObj.getHeight(ctx);

            guiObj.render(guiGraphics, getX(), drawY, ctx, mouseX,  (int) (mouseY + scrollAmount()), delta);

            if(mouseY >= getY() && mouseY  < getY() + getAdjustedHeight() && guiObj.isMouseOver(getX(), drawY, mouseX, (int) (mouseY + scrollAmount()), ctx))
            {
                tooltip = guiObj.getTooltip(ctx, getX(), drawY, mouseX, (int) (mouseY + scrollAmount()));

                hoverObject = guiObj;
                hoverContext = ctx;
            }

            drawY += gap + height;
        }
    }
}
