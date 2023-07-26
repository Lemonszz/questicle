package party.lemons.questicle.quest.display.frame;

import party.lemons.questicle.Questicle;
import party.lemons.questicle.client.texture.TextureDef;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

public interface QuestFrame
{
    TextureQuestFrame DEFAULT = new TextureQuestFrame(
            Questicle.id("textures/gui/frames.png"),
            26, 26,
            new TextureDef(0, 26, 26, 26),
            new TextureDef(0, 0, 26, 26),
            new TextureDef(0, 52, 26, 26),
            new TextureDef(0, 78, 26, 26),
            255, 255
    );

    int width();
    int height();

    QuestFrameType<?> type();
}
