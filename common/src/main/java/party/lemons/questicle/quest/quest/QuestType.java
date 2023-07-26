package party.lemons.questicle.quest.quest;

import com.mojang.serialization.Codec;

public record QuestType<T extends Quest>(Codec<T> codec)
{
}
