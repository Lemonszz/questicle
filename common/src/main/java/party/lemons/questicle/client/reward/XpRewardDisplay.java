package party.lemons.questicle.client.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.tooltip.ItemStackTooltip;
import party.lemons.questicle.client.tooltip.TextTooltip;
import party.lemons.questicle.quest.reward.impl.ItemReward;
import party.lemons.questicle.quest.reward.impl.XpReward;

public class XpRewardDisplay implements RewardDisplay<XpReward>
{
    private final ItemStack XP_BOTTLE = Items.EXPERIENCE_BOTTLE.getDefaultInstance();

    @Override
    public Renderable getTooltip(RewardDisplayContext<XpReward> context, int drawX, int drawY, int mouseX, int mouseY)
    {
        Component component;

        if(!context.reward().isLevels())
            component = Component.translatable("questicle.text.reward.xp", context.reward().xp());
        else
            component = Component.translatable("questicle.text.reward.xp.levels", context.reward().xp());

        return new TextTooltip(component);
    }

    @Override
    public int getWidth(RewardDisplayContext<XpReward> context) {
        return 16;
    }

    @Override
    public int getHeight(RewardDisplayContext<XpReward> context) {
        return 16;
    }

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, RewardDisplayContext<XpReward> context, int mouseX, int mouseY, float delta) {
        graphics.renderFakeItem(XP_BOTTLE, drawX, drawY);

        Font font = Minecraft.getInstance().font;
        int tX = drawX + 10;
        int tY = drawY + 8;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 200);
        graphics.drawString(font, "+", tX + 1, tY, 0, false);
        graphics.drawString(font, "+", tX - 1, tY, 0, false);
        graphics.drawString(font, "+", tX, tY + 1, 0, false);
        graphics.drawString(font, "+", tX, tY - 1, 0, false);
        graphics.drawString(font, "+", tX, tY, 8453920, false);
        graphics.pose().popPose();
    }
}
