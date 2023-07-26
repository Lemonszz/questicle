package party.lemons.questicle.quest.goal.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

public class ChangeDimensionGoal implements Goal {

    public static final Codec<ChangeDimensionGoal> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            Codec.STRING.fieldOf("id").forGetter(ChangeDimensionGoal::id),
                            ResourceLocation.CODEC.fieldOf("dimension").forGetter(changeDimensionGoal -> changeDimensionGoal.targetDimension)
                    )
                    .apply(instance, ChangeDimensionGoal::new));


    private final String id;
    private final ResourceLocation targetDimension;

    public ChangeDimensionGoal(String id, ResourceLocation targetDimension)
    {
        this.id = id;
        this.targetDimension = targetDimension;
    }

    @Override
    public boolean onDimensionChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ResourceLocation newDimension) {
        if(newDimension.equals(targetDimension))
        {
            return true;
        }
        return false;
    }

    @Override
    public GoalType<?> type() {
        return GoalTypes.CHANGE_DIMENSION.get();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Component getHoverTooltip(QuestStorage questStorage) {
        return Component.translatable("questicle.goal.change_dimension", targetDimension);
    }
}
