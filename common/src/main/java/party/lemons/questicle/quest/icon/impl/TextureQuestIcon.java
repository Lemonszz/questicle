package party.lemons.questicle.quest.icon.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.QuestIconType;
import party.lemons.questicle.quest.icon.QuestIconTypes;

import java.util.Optional;

public record TextureQuestIcon(TextureData texture) implements QuestIcon {

    public static final Codec<TextureQuestIcon> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            TextureData.CODEC.fieldOf("texture").forGetter(TextureQuestIcon::texture)
                    )
                    .apply(instance, TextureQuestIcon::new));
    @Override
    public QuestIconType<?> type() {
        return QuestIconTypes.TEXTURE.get();
    }
}
