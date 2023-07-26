package party.lemons.questicle.quest.goal.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import party.lemons.questicle.party.PartyManager;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KillMobGoal implements Goal
{
    private static final String TAG_COUNT = "KilledCount";

    public static final Codec<KillMobGoal> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            Codec.STRING.fieldOf("id").forGetter(KillMobGoal::id),
                            Codec.either(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), TagKey.hashedCodec(Registries.ENTITY_TYPE)).fieldOf("mob").forGetter(KillMobGoal::either),
                            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(KillMobGoal::tag),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(KillMobGoal::targetAmount)
                    )
                    .apply(instance, KillMobGoal::new));

    private final EntityType<?> targetMob;
    private final TagKey<EntityType<?>> targetTag;
    private final boolean isTag;
    private final int count;
    private final String id;
    private final Optional<CompoundTag> tag;

    public KillMobGoal(String id, Either<EntityType<?>, TagKey<EntityType<?>>> item, Optional<CompoundTag> tag, int count){
        this.targetMob = item.left().orElse(null);
        this.targetTag = item.right().orElse(null);
        if(targetTag == null && targetMob == null)   //TODO: does this even decode if both are null?
            throw new IllegalArgumentException("Missing both tag and mob for goal. Must have at least one!");

        isTag = targetMob == null;

        this.count = count;
        this.id = id;
        this.tag = tag;
    }

    @Override
    public boolean onEntityKilled(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer killer, LivingEntity killed) {
        if(targetMatches(killed.getType()))
        {
            int num = NBTUtil.increaseInt(TAG_COUNT, storage.getProgress(this));
            party.getStorage().markDirty();
            if(num >= targetAmount())
                return true;
        }
        return false;
    }

    @Override
    public Component getHoverTooltip(QuestStorage questStorage) {
        return Component.translatable(isTag ? "questicle.goal.kill_mob.tag" : "questicle.goal.kill_mob.mob", targetAmount(), isTag ? "#" + targetTag.location() : targetMob.create(Minecraft.getInstance().level).getDisplayName());
    }

    public boolean targetMatches(EntityType<?> type)
    {
        if(isTag)
            return type.is(targetTag);
        return type == targetMob;
    }

    @Override
    public GoalType<?> type() {
        return GoalTypes.KILL_MOB.get();
    }

    private Either<EntityType<?>, TagKey<EntityType<?>>> either(){
        return isTag ? Either.right(targetTag) : Either.left(targetMob);
    }

    public int targetAmount() {
        return count;
    }

    @Override
    public String id() {
        return id;
    }

    public Optional<CompoundTag> tag() {
        return tag;
    }

    public List<EntityType<?>> getValidMobs()
    {
        if(isTag)
        {
            //TODO: perhaps cache this as I imagine it's slow
            List<EntityType<?>> li = new ArrayList<>();
            BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(targetTag).forEach((it)->li.add(it.value()));
            return li;
        }

        return List.of(targetMob);
    }


    public int getKilledCount(CompoundTag progress)
    {
        return  progress.getInt(TAG_COUNT);
    }
}
