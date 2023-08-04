package party.lemons.questicle.client.texture;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.client.QAtlases;

import java.util.Objects;

public class TextureData
{
    //TODO: 1.20.2  - Use what Gui Sprites do




    public static Codec<TextureData> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("atlas", QAtlases.ELEMENTS_ATLAS).forGetter(TextureData::atlas),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureData::texture)
    ).apply(instance, TextureData::new));

    public static Codec<TextureData> CODEC = Codec.either(ResourceLocation.CODEC, FULL_CODEC).xmap(
            either -> either.map((l)->new TextureData(QAtlases.ELEMENTS_ATLAS, l), location -> location),
            Either::right
    );

    private final ResourceLocation texture, atlas;

    public TextureData(ResourceLocation atlas, ResourceLocation texture)
    {
        this.texture = texture;
        this.atlas = atlas;
    }

    @Environment(EnvType.CLIENT)
    public TextureAtlasSprite sprite()
    {
        return QAtlases.getAtlasSprite(this);
    }

    public ResourceLocation texture()
    {
        return texture;
    }

    public ResourceLocation atlas()
    {
        return atlas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureData that = (TextureData) o;
        return Objects.equals(texture, that.texture) && Objects.equals(atlas, that.atlas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, atlas);
    }
}