package party.lemons.questicle.client.tooltip.goal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.gui.QComponents;
import party.lemons.questicle.quest.goal.impl.ChangeDimensionGoal;

public class ChangeDimensionGoalDisplay implements GoalDisplay<ChangeDimensionGoal>
{
    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, GoalDisplayContext<ChangeDimensionGoal> context, int mouseX, int mouseY, float delta) {
        QComponents.ICON_NETHER_PORTAL.render(graphics, drawX, drawY, 16, 16);

        Component text = getText(context);
        graphics.drawString(Minecraft.getInstance().font, text, drawX + 18, drawY + (DrawUtils.DEFAULT_STRING_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    public Vector2i getSize(GoalDisplayContext<ChangeDimensionGoal> context) {

        Component text = getText(context);
        Font font = Minecraft.getInstance().font;
        int width = 24 + font.width(text);
        int height = 16;

        return new Vector2i(width, height);
    }


    public Component getText(GoalDisplayContext<ChangeDimensionGoal> context)
    {
        return context.goal().getHoverTooltip(context.questStorage());
    }
}
