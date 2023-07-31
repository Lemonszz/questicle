package party.lemons.questicle.quest.goal.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import party.lemons.questicle.client.texture.TextureData;
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

public class CollectGoal extends Goal implements InventoryCountGoal
{
    public static String TAG_LAST_COUNT = "LastCount";

    public static final Codec<CollectGoal> CODEC = RecordCodecBuilder.create(instance ->
            baseCodec(instance)
                    .and(
                            instance.group(
                                    Codec.either(BuiltInRegistries.ITEM.byNameCodec(), TagKey.hashedCodec(Registries.ITEM)).fieldOf("item").forGetter(CollectGoal::either),
                                    Codec.INT.optionalFieldOf("count", 1).forGetter(i->i.count),
                                    CompoundTag.CODEC.optionalFieldOf("tag").forGetter(i->i.dataTag)
                            )                    )
                    .apply(instance, CollectGoal::new));

    private final Item checkItem;
    private final TagKey<Item> checkTag;
    private final boolean isTag;
    private final int count;
    private final Optional<CompoundTag> dataTag;
    public CollectGoal(String id, Optional<TextureData> icon, Either<Item, TagKey<Item>> item, int count, Optional<CompoundTag> dataTag){
        super(id, icon);

        this.checkItem = item.left().orElse(null);
        this.checkTag = item.right().orElse(null);
        if(checkTag == null && checkItem == null)   //TODO: does this even decode if both are null?
            throw new IllegalArgumentException("Missing both tag and item for goal. Must have at least one!");

        isTag = checkItem == null;

        this.count = count;
        this.dataTag = dataTag;
    }

    public boolean onInventoryChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ItemStack stack)
    {
        if(isTag) {
            if(!stack.is(checkTag))
                return false;
        }
        else if(!stack.is(checkItem))
            return false;

        if(dataTag.isPresent() && !isTag)
        {
            ItemStack withData = checkItem.getDefaultInstance();
            withData.setTag(dataTag.get().copy());
            if(!ItemStack.isSameItemSameTags(stack, withData))
                return false;
        }

        return checkPartyCount(party, storage);
    }

    private boolean checkPartyCount(QuestParty party, QuestStorage questStorage)
    {
        int num = NBTUtil.setInt(TAG_LAST_COUNT, getCount(party.getOnlinePlayers()), questStorage.getProgress(this));
        party.getStorage().markDirty();

        return num >= getRequiredCount();
    }

    @Override
    public boolean onMadeAvailable(QuestParty party, QuestStorage questStorage)
    {
        return checkPartyCount(party, questStorage);
    }

    @Override
    public MutableComponent getHoverTooltip(QuestStorage questStorage, Level level) {
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
        ItemStack defaultStack = checkItem.getDefaultInstance();
        dataTag.ifPresent(tag -> defaultStack.setTag(tag.copy()));


        return List.of(defaultStack);
    }
}
