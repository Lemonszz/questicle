package party.lemons.questicle.client.icon;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.quest.icon.impl.ItemQuestIcon;
import party.lemons.questicle.quest.icon.impl.TextureQuestIcon;

public class TextureIconRenderer implements IconRenderer<TextureQuestIcon> {

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, IconRendererContext<TextureQuestIcon> context, int mouseX, int mouseY, float delta)
    {
        TextureQuestIcon icon = context.icon();
        int offsetX = 0;
        int offsetY = 0;

        if(context.frame() != null) {
            offsetX = (context.getFrameWidth() - 16) / 2;
            offsetY = (context.getFrameHeight() - 16) / 2;
        }

        DrawUtils.drawTexture(graphics, icon.texture(), drawX + offsetX, drawY + offsetY, 16, 16);
    }
}
