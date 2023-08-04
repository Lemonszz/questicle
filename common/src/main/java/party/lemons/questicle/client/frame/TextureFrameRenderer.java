package party.lemons.questicle.client.frame;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

public class TextureFrameRenderer implements FrameRenderer<TextureQuestFrame>
{
    @Override
    public void render(GuiGraphics g, TextureQuestFrame frame, QuestDisplay display,  int drawX, int drawY, float delta, boolean hovered)
    {
        TextureData def;

        switch (display.quest().getQuestStatus(ClientStorage.clientParty.getStorage()))
        {
            case COMPLETE -> def = frame.completeDef();
            case UNAVAILABLE -> def = frame.disabledDef();
            default -> def = hovered ? frame.hoverDef() : frame.standardDef(); //Available/Unknown
        }

        TextureAtlasSprite sprite = def.sprite();

        g.blit(drawX, drawY, 0, sprite.contents().width(), sprite.contents().height(), sprite);
    }
}
