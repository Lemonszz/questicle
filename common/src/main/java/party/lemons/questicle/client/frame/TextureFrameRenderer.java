package party.lemons.questicle.client.frame;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.client.texture.TextureDef;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

public class TextureFrameRenderer implements FrameRenderer<TextureQuestFrame>
{
    @Override
    public void render(GuiGraphics g, TextureQuestFrame frame, QuestDisplay display,  int drawX, int drawY, float delta, boolean hovered)
    {
        TextureDef def;

        //TODO: this should go in the frame itself
        //Perhaps even a TextureDefinition supplier class?
        switch (display.quest().getQuestStatus(ClientStorage.clientParty.getStorage()))
        {
            case COMPLETE -> def = frame.completeDef();
            case UNAVAILABLE -> def = frame.disabledDef();
            default -> def = hovered ? frame.hoverDef() : frame.standardDef(); //Available/Unknown
        }

        g.blit(frame.texture(), drawX, drawY, def.x(), def.y(), def.width(), def.height(), frame.textureWidth(), frame.textureHeight());
    }
}
