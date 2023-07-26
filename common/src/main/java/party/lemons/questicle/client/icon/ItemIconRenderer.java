package party.lemons.questicle.client.icon;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.impl.ItemQuestIcon;

public class ItemIconRenderer implements IconRenderer<ItemQuestIcon> {

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, IconRendererContext<ItemQuestIcon> context, int mouseX, int mouseY, float delta)
    {
        ItemQuestIcon icon = context.icon();
        int offsetX = 0;
        int offsetY = 0;

        if(context.frame() != null) {
            offsetX = (context.getFrameWidth() - 16) / 2;
            offsetY = (context.getFrameHeight() - 16) / 2;
        }

        graphics.renderItem(icon.itemStack(), drawX + offsetX, drawY + offsetY);
    }
}
