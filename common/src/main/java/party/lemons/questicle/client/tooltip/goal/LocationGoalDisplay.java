package party.lemons.questicle.client.tooltip.goal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import party.lemons.questicle.client.ClientConfig;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.quest.goal.impl.LocationGoal;

public class LocationGoalDisplay implements GoalDisplay<LocationGoal>
{
    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, GoalDisplayContext<LocationGoal> context, int mouseX, int mouseY, float delta) {

        int textX = drawX + 18;

        if(!drawOverrideIcon(graphics, drawX, drawY, context))
        {
            textX = drawX;
        }

        Component text = getText(context);
        graphics.drawString(Minecraft.getInstance().font, text, textX, drawY + (DrawUtils.fontLineHeight() / 2), 0xFFFFFF);
    }

    @Override
    public Vector2i getSize(GoalDisplayContext<LocationGoal> context) {

        Component text = getText(context);
        Font font = Minecraft.getInstance().font;
        int width = 24 + font.width(text);
        int height = 16;

        return new Vector2i(width, height);
    }


    public Component getText(GoalDisplayContext<LocationGoal> context)
    {
        return ClientConfig.applyQuestFont(context.goal().getHoverTooltip(context.questStorage(), Minecraft.getInstance().level));
    }
}
