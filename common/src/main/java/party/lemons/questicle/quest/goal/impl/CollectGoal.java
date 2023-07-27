package party.lemons.questicle.quest.goal.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;

public class CollectGoal extends Goal implements InventoryCountGoal
{
    public static String TAG_LAST_COUNT = "LastCount";

    public static final Codec<CollectGoal> CODEC = RecordCodecBuilder.create(instance ->
            baseCodec(instance)
                    .and(
                            instance.group(
                                    Codec.either(BuiltInRegistries.ITEM.byNameCodec(), TagKey.hashedCodec(Registries.ITEM)).fieldOf("item").forGetter(CollectGoal::either),
                                    Codec.INT.optionalFieldOf("count", 1).forGetter(i->i.count)
                            )                    )
                    .apply(instance, CollectGoal::new));

    private final Item checkItem;
    private final TagKey<Item> checkTag;
    private final boolean isTag;
    private final int count;
    public CollectGoal(String id, Either<Item, TagKey<Item>> item, int count){
        super(id);

        this.checkItem = item.left().orElse(null);
        this.checkTag = item.right().orElse(null);
        if(checkTag == null && checkItem == null)   //TODO: does this even decode if both are null?
            throw new IllegalArgumentException("Missing both tag and item for goal. Must have at least one!");

        isTag = checkItem == null;

        this.count = count;
    }

    public boolean onInventoryChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ItemStack stack)
    {
        if(isTag) {
            if(!stack.is(checkTag))
                return false;
        }
        else if(!stack.is(checkItem))
            return false;

        int num = NBTUtil.setInt(TAG_LAST_COUNT, getCount(party.getOnlinePlayers(player.getServer())), storage.getProgress(this));
        party.getStorage().markDirty();

        return num >= getRequiredCount();
    }

    @Override
    public Component getHoverTooltip(QuestStorage questStorage) {
        return Component.translatable(isTag ? "questicle.goal.collect.tag" : "questicle.goal.collect.item", getRequiredCount(), isTag ? "#" + checkTag.location() : checkItem.getDefaultInstance().getDisplayName());
    }

    public int getLastCount(CompoundTag tag)
    {
        return tag.getInt(TAG_LAST_COUNT);
    }

    @Override
    public GoalType<?> type() {
        return GoalTypes.COLLECT_GOAL.get();
    }

    @Override
    public int getRequiredCount() {
        return count;
    }

    @Override
    public boolean itemMatches(ItemStack stack)
    {
        if(isTag)
            return stack.is(checkTag);

        return stack.is(checkItem);
    }

    private Either<Item, TagKey<Item>> either(){
        return isTag ? Either.right(checkTag) : Either.left(checkItem);
    }

    public List<ItemStack> getValidStacks()
    {
        if(isTag)
        {
            //TODO: perhaps cache this as I imagine it's slow
            List<ItemStack> li = new ArrayList<>();
            BuiltInRegistries.ITEM.getTagOrEmpty(checkTag).forEach((it)->li.add(new ItemStack(it.value())));
            return li;
        }

        return List.of(checkItem.getDefaultInstance());
    }
}
