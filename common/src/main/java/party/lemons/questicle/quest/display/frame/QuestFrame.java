package party.lemons.questicle.quest.display.frame;

import party.lemons.questicle.Questicle;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

public interface QuestFrame
{
    TextureQuestFrame DEFAULT = new TextureQuestFrame(
            26, 26,
            new TextureData(Questicle.id("textures/gui/frames.png"), 0, 26, 26, 26, 256, 256),
            new TextureData(Questicle.id("textures/gui/frames.png"), 0, 0, 26, 26, 256, 256),
            new TextureData(Questicle.id("textures/gui/frames.png"), 0, 52, 26, 26, 256, 256),
            new TextureData(Questicle.id("textures/gui/frames.png"), 0, 78, 26, 26, 256, 256),
            255, 255
    );

    int width();
    int height();

    QuestFrameType<?> type();
}
