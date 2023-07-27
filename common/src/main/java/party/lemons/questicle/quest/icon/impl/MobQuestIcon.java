package party.lemons.questicle.quest.icon.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.quest.goal.impl.KillMobGoal;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.QuestIconType;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.util.QCodecs;

import java.util.Optional;

public record MobQuestIcon(EntityType<?> entityType, Optional<CompoundTag> tag, float scale) implements QuestIcon {

    public static final Codec<MobQuestIcon> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("mob").forGetter(MobQuestIcon::entityType),
                            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(MobQuestIcon::tag),
                            Codec.FLOAT.optionalFieldOf("scale", 1.5F).forGetter(MobQuestIcon::scale)
                    )
                    .apply(instance, MobQuestIcon::new));
    @Override
    public QuestIconType<?> type() {
        return QuestIconTypes.MOB.get();
    }
}
