package party.lemons.questicle.client.icon;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.icon.impl.BlankQuestIcon;

public class BlankFrameRenderer implements IconRenderer<BlankQuestIcon> {

    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, IconRendererContext<BlankQuestIcon> context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public int getWidth(IconRendererContext<BlankQuestIcon> context) {
        return 0;
    }

    @Override
    public int getHeight(IconRendererContext<BlankQuestIcon> context) {
        return 0;
    }
}
