package party.lemons.questicle.quest.goal;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

import java.util.Optional;

public abstract class Goal
{
    protected static <P extends Goal> Products.P2<RecordCodecBuilder.Mu<P>, String, Optional<TextureData>> baseCodec(RecordCodecBuilder.Instance<P> instance) {
        return instance.group(
                Codec.STRING.fieldOf("id").forGetter(Goal::id),
                TextureData.CODEC.optionalFieldOf("icon").forGetter(Goal::icon)
        );
    }
    private final String id;
    private final Optional<TextureData> icon;

    public Goal(String id, Optional<TextureData> icon)
    {
        this.id = id;
        this.icon = icon;
    }

    public abstract GoalType<?> type();
    public abstract Component getHoverTooltip(QuestStorage questStorage);

    public String id(){
        return this.id;
    }

    public Optional<TextureData> icon()
    {
        return icon;
    }

    public QuestStorage getStorage(QuestParty party, Quest quest)
    {
        return party.getStorage().getQuestProgress(quest);
    }

    public boolean onEntityKilled(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer killer, LivingEntity killed)
    {
        return false;
    }

    public boolean onInventoryChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ItemStack stack)
    {
        return false;
    }

    public boolean onDimensionChanged(Quest quest, QuestStorage storage, QuestParty party, ServerPlayer player, ResourceLocation newDimension)
    {
        return false;
    }

}
