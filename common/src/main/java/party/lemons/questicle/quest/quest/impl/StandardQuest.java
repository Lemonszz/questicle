package party.lemons.questicle.quest.quest.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestDependency;
import party.lemons.questicle.quest.quest.QuestType;
import party.lemons.questicle.quest.quest.QuestTypes;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.icon.impl.BlankQuestIcon;
import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.reward.RewardTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandardQuest implements Quest
{
    public static final Codec<StandardQuest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            ResourceLocation.CODEC.optionalFieldOf("id", Questicle.INVALID_RESOURCE).forGetter(Quest::id),
                            Codec.STRING.fieldOf("name").forGetter(StandardQuest::displayName),
                            Codec.STRING.optionalFieldOf("description", "").forGetter(StandardQuest::description),
                            QuestIconTypes.CODEC.optionalFieldOf("icon", BlankQuestIcon.INSTANCE).forGetter(StandardQuest::icon),
                            GoalTypes.CODEC.listOf().fieldOf("goals").forGetter(StandardQuest::goals),
                            QuestDependency.CODEC.listOf().optionalFieldOf("dependencies", new ArrayList<>()).forGetter(StandardQuest::dependencies),
                            RewardTypes.CODEC.listOf().fieldOf("rewards").forGetter(StandardQuest::rewards)
                    )
                    .apply(instance, StandardQuest::new));

    @NotNull private final String name;
    @NotNull private final String description;
    @NotNull private final QuestIcon icon;

    @NotNull private final List<Goal> goals;
    @NotNull private final List<QuestDependency> dependencies;
    @NotNull private final List<Reward> rewards;

    @Nullable private ResourceLocation id;

    public StandardQuest(@Nullable ResourceLocation id, @NotNull String name, @Nullable String description, @Nullable QuestIcon icon, @NotNull List<Goal> goals, List<QuestDependency> dependencies, List<Reward> rewards)
    {
        this.name = name;
        this.description = description == null ? "" : description;
        this.icon = icon == null ? BlankQuestIcon.INSTANCE : icon;
        this.goals = goals;
        this.dependencies = dependencies;
        this.rewards = rewards;
        this.id = id;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    @Override
    public void setID(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public @NotNull List<Goal> goals() {
        return goals;
    }

    @Override
    public String displayName() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return description;
    }

    @Override
    public QuestIcon icon() {
        return icon;
    }

    @Override
    public @NotNull List<QuestDependency> dependencies() {
        return dependencies;
    }

    @Override
    public @NotNull List<Reward> rewards() {
        return rewards;
    }

    @Override
    public QuestType<?> type() {
        return QuestTypes.STANDARD.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if(o instanceof Quest otherQuest)
        {
            return otherQuest.id().equals(id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
