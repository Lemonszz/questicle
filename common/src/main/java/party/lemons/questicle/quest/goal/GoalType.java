package party.lemons.questicle.quest.goal;

import com.mojang.serialization.Codec;

public record GoalType<T extends Goal>(Codec<T> codec)
{
}
