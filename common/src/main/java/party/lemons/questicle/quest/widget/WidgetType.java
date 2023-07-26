package party.lemons.questicle.quest.widget;

import com.mojang.serialization.Codec;
import party.lemons.questicle.quest.icon.QuestIcon;

public record WidgetType<T extends Widget>(Codec<T> codec)
{
}
