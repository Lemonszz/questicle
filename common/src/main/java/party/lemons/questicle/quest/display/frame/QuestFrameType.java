package party.lemons.questicle.quest.display.frame;

import com.mojang.serialization.Codec;
import party.lemons.questicle.quest.icon.QuestIcon;

public record QuestFrameType<T extends QuestFrame>(Codec<T> codec)
{
}
