package party.lemons.questicle.quest.display.frame;

import party.lemons.questicle.Questicle;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

public interface QuestFrame
{
    TextureQuestFrame DEFAULT = new TextureQuestFrame(
            26, 26,
            new TextureData(Questicle.id("quest_frames"), Questicle.id("standard")),
            new TextureData(Questicle.id("quest_frames"), Questicle.id("standard_hover")),
            new TextureData(Questicle.id("quest_frames"), Questicle.id("standard_complete")),
            new TextureData(Questicle.id("quest_frames"), Questicle.id("standard_disabled")),
            255, 255
    );

    int width();
    int height();

    QuestFrameType<?> type();
}
