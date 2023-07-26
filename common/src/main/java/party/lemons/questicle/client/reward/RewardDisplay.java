package party.lemons.questicle.client.reward;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.gui.GuiObject;
import party.lemons.questicle.quest.reward.Reward;

public interface RewardDisplay<T extends Reward> extends GuiObject<RewardDisplay.RewardDisplayContext<T>>
{
    @Override
    void render(GuiGraphics graphics, int drawX, int drawY, RewardDisplayContext<T> context, int mouseX, int mouseY, float delta);

    record RewardDisplayContext<T extends Reward>(T reward)
    {
    }
}
