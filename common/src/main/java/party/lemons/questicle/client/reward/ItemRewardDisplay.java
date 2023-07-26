package party.lemons.questicle.client.reward;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.tooltip.ItemStackTooltip;
import party.lemons.questicle.quest.reward.impl.ItemReward;

public class ItemRewardDisplay implements RewardDisplay<ItemReward> {

    private ItemStack lastStack = ItemStack.EMPTY;

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, RewardDisplayContext<ItemReward> context, int mouseX, int mouseY, float delta) {
        lastStack = DrawUtils.drawItemStackListWithDecoration(graphics, context.reward().itemStacks(), drawX, drawY);
    }

    @Override
    public Renderable getTooltip(RewardDisplayContext<ItemReward> context, int drawX, int drawY, int mouseX, int mouseY) {
        return new ItemStackTooltip(lastStack);
    }

    @Override
    public int getWidth(RewardDisplayContext<ItemReward> context) {
        return 16;
    }

    @Override
    public int getHeight(RewardDisplayContext<ItemReward> context) {
        return 16;
    }
}
