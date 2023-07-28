package party.lemons.questicle.client.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record TextureData(ResourceLocation texture, int x, int y, int width, int height)
{
    public static Codec<TextureData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureData::texture),
            Codec.INT.fieldOf("x").forGetter(TextureData::x),
            Codec.INT.fieldOf("y").forGetter(TextureData::y),
            Codec.INT.fieldOf("width").forGetter(TextureData::width),
            Codec.INT.fieldOf("height").forGetter(TextureData::height)
    ).apply(instance, TextureData::new));
}