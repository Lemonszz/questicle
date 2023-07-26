package party.lemons.questicle.client.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TextureDef(int x, int y, int width, int height)
{
    public static Codec<TextureDef> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(TextureDef::x),
            Codec.INT.fieldOf("y").forGetter(TextureDef::y),
            Codec.INT.fieldOf("width").forGetter(TextureDef::width),
            Codec.INT.fieldOf("height").forGetter(TextureDef::height)
    ).apply(instance, TextureDef::new));
}