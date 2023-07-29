package party.lemons.questicle.quest.goal.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.util.QUtil;

import java.util.Optional;

public class ChangeDimensionGoal extends Goal {

    public static final Codec<ChangeDimensionGoal> CODEC = RecordCodecBuilder.create(instance ->
            baseCodec(instance)
                    .and(
                            ResourceLocation.CODEC.fieldOf("dimension").forGetter(changeDimensionGoal -> changeDimensionGoal.targetDimension)
                        )
            .apply(instance, ChangeDimensionGoal::new));


    private final ResourceLocation targetDimension;

    public ChangeDimensionGoal(String id, Optional<TextureData> icon, ResourceLocation targetDimension)
    {
        super(id, icon);
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
    public Component getHoverTooltip(QuestStorage questStorage, Level level) {
        String dimensionName = QUtil.titleCase(targetDimension.getPath().replace("_", " ")); //Best guess dimension name? TODO: Maybe just use this as fallback. lang strings for vanilla dimensions. Is there a common format for modded?
        return Component.translatable("questicle.goal.change_dimension", dimensionName);
    }
}
