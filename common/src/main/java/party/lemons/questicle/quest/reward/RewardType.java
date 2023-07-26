package party.lemons.questicle.quest.reward;

import com.mojang.serialization.Codec;
import party.lemons.questicle.quest.icon.QuestIcon;

public record RewardType<T extends Reward>(Codec<T> codec)
{
}
