package party.lemons.questicle.quest.goal.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.goal.GoalType;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.util.QUtil;

import java.util.List;
import java.util.Optional;

public class LocationGoal extends Goal
{
    public static final Codec<LocationGoal> CODEC = RecordCodecBuilder.create(instance ->
                    Goal.baseCodec(instance)
                            .and(
                                    instance.group(
                                            Codec.STRING.fieldOf("display_text").forGetter(LocationGoal::displayText),
                                            Codec.either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.BIOME)).optionalFieldOf("biome").forGetter(LocationGoal::checkBiome),
                                            ResourceLocation.CODEC.optionalFieldOf("dimension").forGetter(LocationGoal::checkDimension),
                                            Codec.either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.STRUCTURE)).optionalFieldOf("structure").forGetter(LocationGoal::checkStructure)
                                    )
                            )
           .apply(instance, LocationGoal::new)
    );

    private final Optional<Either<ResourceLocation, TagKey<Biome>>> checkBiome;
    private final Optional<Either<ResourceLocation, TagKey<Structure>>> checkStructure;
    private final Optional<ResourceLocation> checkDimension;
    private final String displayText;

    public LocationGoal(String id, Optional<TextureData> icon, String displayText, Optional<Either<ResourceLocation, TagKey<Biome>>> biome, Optional<ResourceLocation> dimension,  Optional<Either<ResourceLocation, TagKey<Structure>>> structure)
    {
        super(id, icon);

        this.displayText = displayText;
        this.checkBiome = biome;
        this.checkDimension = dimension;
        this.checkStructure = structure;
    }

    @Override
    public GoalType<?> type() {
        return GoalTypes.LOCATION.get();
    }

    @Override
    public Component getHoverTooltip(QuestStorage questStorage, Level level) {
        return Component.translatable(displayText());
    }

    @Override
    public boolean checkLocation(Quest quest, QuestStorage questStorage, LocationContext ctx) {
        return isInDimension(ctx) && isInBiome(ctx) && isInStructure(ctx);
    }

    public boolean isInBiome(LocationContext context)
    {
        if(checkBiome.isEmpty())
            return true;

        Either<ResourceLocation, TagKey<Biome>> either = checkBiome.get();
        Optional<ResourceLocation> location = either.left();
        Optional<TagKey<Biome>> tag = either.right();

        if(location.isPresent())
            return context.biome.is(location.get());

        if(tag.isPresent())
            return context.biome.is(tag.get());

        return false;
    }

    public boolean isInDimension(LocationContext context)
    {
        if(checkDimension.isPresent())
        {
            return context.dimension().location().equals(checkDimension.get());
        }

        return true;
    }

    public boolean isInStructure(LocationContext context)
    {
        if(checkStructure.isEmpty())
            return true;

        Either<ResourceLocation, TagKey<Structure>> either = checkStructure.get();
        Optional<ResourceLocation> location = either.left();
        Optional<TagKey<Structure>> tag = either.right();


        for(StructureStart structureStart : context.structure){
            Holder<Structure> structureHolder = context.player().serverLevel().registryAccess().registryOrThrow(Registries.STRUCTURE).wrapAsHolder(structureStart.getStructure());

            if(location.isPresent())
                return structureHolder.is(location.get());
            if(tag.isPresent())
                return structureHolder.is(tag.get());
        }
        return false;

    }

    private Optional<Either<ResourceLocation, TagKey<Biome>>> checkBiome()
    {
        return checkBiome;
    }
    private Optional<ResourceLocation> checkDimension()
    {
        return checkDimension;
    }

    private Optional<Either<ResourceLocation, TagKey<Structure>>> checkStructure()
    {
        return checkStructure;
    }

    public String displayText()
    {
        return displayText;
    }

    public record LocationContext(ServerPlayer player, Holder<Biome> biome, List<StructureStart> structure, ResourceKey<Level> dimension)
    {
        public static LocationContext create(ServerPlayer player)
        {
            return new LocationContext(player, player.level().getBiome(player.blockPosition()), QUtil.getStructuresAt(player.serverLevel(), player.blockPosition()), player.serverLevel().dimension());
        }
    }
}
