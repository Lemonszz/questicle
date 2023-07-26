package party.lemons.questicle.quest.icon;

import com.mojang.serialization.Codec;

public record QuestIconType<T extends QuestIcon>(Codec<T> codec)
{
}
