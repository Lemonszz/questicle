package party.lemons.questicle.client.tooltip.goal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector2i;
import party.lemons.questicle.QuesticleClient;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.quest.goal.impl.KillMobGoal;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

import java.util.List;

public class KillMobDisplayGoal implements GoalDisplay<KillMobGoal>
{
    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, GoalDisplayContext<KillMobGoal> context, int mouseX, int mouseY, float delta)
    {
        KillMobGoal goal = context.goal();

        List<EntityType<?>> validMobs = goal.getValidMobs();
        DrawUtils.drawEntityIcon(graphics, validMobs, goal.tag(), drawX + 8, drawY + 12, 1.0F);

        Component text = getText(context);
        graphics.drawString(Minecraft.getInstance().font, text, drawX + 18, drawY + (DrawUtils.DEFAULT_STRING_HEIGHT / 2), 0xFFFFFF);
    }

    @Override
    public Vector2i getSize(GoalDisplayContext<KillMobGoal> context) {

        Component text = getText(context);
        Font font = Minecraft.getInstance().font;
        int width = 24 + font.width(text);
        int height = 16;

        return new Vector2i(width, height);
    }

    public Component getText( GoalDisplayContext<KillMobGoal> context)
    {
        int maxCount = context.goal().targetAmount();
        int currentCount = context.goal().getKilledCount(context.questStorage().getProgress(context.goal()));
        int percent = (int)(((float)currentCount / (float)maxCount) * 100F);

        MutableComponent component = Component.literal(currentCount + "/" + maxCount + " (" + percent + "%) ").append(Component.translatable("questicle.text.killed"));
        if(maxCount == currentCount)
            component = component.withStyle(ChatFormatting.STRIKETHROUGH);

        return component;
    }


}
