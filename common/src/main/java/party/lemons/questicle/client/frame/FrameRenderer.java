package party.lemons.questicle.client.frame;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.QuesticleClient;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.QuestFrame;

public interface FrameRenderer<T extends QuestFrame>
{
    void render(GuiGraphics g, T frame, QuestDisplay display, int drawX, int drawY, float delta, boolean hovered);

    default void renderRewardPending(GuiGraphics graphics, T frame, int drawX, int drawY, float delta, boolean hovered)
    {
        graphics.blit(QuesticleClient.FRAMES_TEXTURE, drawX + 4, drawY - 2, hovered ? 4 : 0, 246, 4, 10);
    }
}
