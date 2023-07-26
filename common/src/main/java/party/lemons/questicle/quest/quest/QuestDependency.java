package party.lemons.questicle.quest.quest;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record QuestDependency(ResourceLocation quest, boolean required)
{
    public static Codec<QuestDependency> FULL_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("quest").forGetter(QuestDependency::quest),
                    Codec.BOOL.optionalFieldOf("required", true).forGetter(QuestDependency::required)
            ).apply(instance, QuestDependency::new));

    public static Codec<QuestDependency> CODEC = Codec.either(
            ResourceLocation.CODEC,
            FULL_CODEC
    ).xmap(
            either -> either.map((location -> new QuestDependency(location, true)), location -> location),
            Either::right
    );
}
