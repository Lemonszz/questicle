package party.lemons.questicle.client.tooltip.goal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.gui.QComponents;
import party.lemons.questicle.client.tooltip.ItemStackTooltip;
import party.lemons.questicle.quest.goal.impl.CollectGoal;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.util.QMath;

import java.util.List;

public class CollectGoalDisplay implements GoalDisplay<CollectGoal>
{
    ItemStack lastDrawnStack = ItemStack.EMPTY;

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, GoalDisplayContext<CollectGoal> context, int mouseX, int mouseY, float delta) {

        if(!drawOverrideIcon(graphics, drawX, drawY, context))
        {
            List<ItemStack> validStacks = context.goal().getValidStacks();
            lastDrawnStack = DrawUtils.drawItemStackList(graphics, validStacks, drawX, drawY);
        }

        Component text = getText(context);
        graphics.drawString(Minecraft.getInstance().font, text, drawX + 18, drawY + (DrawUtils.DEFAULT_STRING_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    public Renderable getTooltip(GoalDisplayContext<CollectGoal> context, int drawX, int drawY, int mouseX, int mouseY)
    {
        if(QMath.inArea(mouseX, mouseY, drawX, drawY, drawX + 16, drawY + 16))
        {
            return new ItemStackTooltip(lastDrawnStack);
        }

        return GoalDisplay.super.getTooltip(context, drawX, drawY, mouseX, mouseY);
    }

    @Override
    public Vector2i getSize(GoalDisplayContext<CollectGoal> context) {

        Component text = getText(context);
        Font font = Minecraft.getInstance().font;
        int width = 24 + font.width(text);
        int height = 16;

        return new Vector2i(width, height);
    }


    public Component getText(GoalDisplayContext<CollectGoal> context)
    {
        int maxCount = context.goal().getRequiredCount();
        int currentCount = context.goal().getLastCount(context.questStorage().getProgress(context.goal()));
        int percent = (int)(((float)currentCount / (float)maxCount) * 100F);

        MutableComponent component = Component.literal(currentCount + "/" + maxCount + " (" + percent + "%) ").append(Component.translatable("questicle.text.collected"));
        if(maxCount == currentCount)
            component = component.withStyle(ChatFormatting.STRIKETHROUGH);

        return component;
    }
}
