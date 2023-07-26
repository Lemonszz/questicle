package party.lemons.questicle.quest.icon.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.quest.icon.QuestIconType;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.util.QCodecs;

public record ItemQuestIcon(ItemStack itemStack) implements QuestIcon {

    public static final Codec<ItemQuestIcon> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                        QCodecs.SIMPLE_ITEM_STACK.fieldOf("item").forGetter(ItemQuestIcon::itemStack)
                    )
                    .apply(instance, ItemQuestIcon::new));
    @Override
    public QuestIconType<?> type() {
        return QuestIconTypes.ITEM.get();
    }
}
