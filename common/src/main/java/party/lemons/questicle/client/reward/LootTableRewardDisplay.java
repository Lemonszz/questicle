package party.lemons.questicle.client.reward;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.tooltip.ItemStackTooltip;
import party.lemons.questicle.client.tooltip.TextTooltip;
import party.lemons.questicle.quest.reward.impl.ItemReward;
import party.lemons.questicle.quest.reward.impl.LootTableReward;
import party.lemons.questicle.quest.reward.impl.XpReward;

import java.util.List;

public class LootTableRewardDisplay implements RewardDisplay<LootTableReward> {

    private final List<ItemStack> displayStack = List.of(Items.CHEST.getDefaultInstance());

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, RewardDisplayContext<LootTableReward> context, int mouseX, int mouseY, float delta) {
        DrawUtils.drawItemStackList(graphics, displayStack, drawX, drawY);
    }

    @Override
    public Renderable getTooltip(RewardDisplayContext<LootTableReward> context, int drawX, int drawY, int mouseX, int mouseY)
    {
        return new TextTooltip(Component.translatable("questicle.text.reward.loottable", context.reward().table()));
    }

    @Override
    public int getWidth(RewardDisplayContext<LootTableReward> context) {
        return 16;
    }

    @Override
    public int getHeight(RewardDisplayContext<LootTableReward> context) {
        return 16;
    }
}
