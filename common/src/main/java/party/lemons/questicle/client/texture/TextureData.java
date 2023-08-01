package party.lemons.questicle.client.texture;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.Questicle;

import java.util.HashMap;
import java.util.Map;

public record TextureData(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight)
{
    private static final ResourceLocation BUILTIN_TEXTURE = Questicle.id("textures/builtin.png");
    private static final TextureData UNKNOWN_TEXTURE = new TextureData(BUILTIN_TEXTURE, 496, 496, 16, 16, 256, 256);

    public static final Map<ResourceLocation, TextureData> BUILTIN = new ImmutableMap.Builder<ResourceLocation, TextureData>()
            .put(Questicle.id("nether_fortress"), new TextureData(BUILTIN_TEXTURE, 0, 0, 92, 92, 512, 512))
            .put(Questicle.id("nether_portal"), new TextureData(BUILTIN_TEXTURE, 93, 0, 92, 92, 512, 512))
            .put(Questicle.id("wither"), new TextureData(BUILTIN_TEXTURE, 186, 0, 92, 92, 512, 512))
            .put(Questicle.id("end_portal"), new TextureData(BUILTIN_TEXTURE, 279, 0, 92, 92, 512, 512))
            .put(Questicle.id("end_portal_active"), new TextureData(BUILTIN_TEXTURE, 372, 0, 92, 92, 512, 512))
            .build();


    public static Codec<TextureData> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureData::texture),
            Codec.INT.fieldOf("x").forGetter(TextureData::x),
            Codec.INT.fieldOf("y").forGetter(TextureData::y),
            Codec.INT.fieldOf("width").forGetter(TextureData::width),
            Codec.INT.fieldOf("height").forGetter(TextureData::height),
            Codec.INT.optionalFieldOf("texture_width", 256).forGetter(TextureData::textureWidth),
            Codec.INT.optionalFieldOf("texture_height", 256).forGetter(TextureData::textureHeight)
    ).apply(instance, TextureData::new));

    public static Codec<TextureData> CODEC = Codec.either(
            ResourceLocation.CODEC,
            FULL_CODEC
    ).xmap(
            r->r.map(rl -> BUILTIN.getOrDefault(rl, UNKNOWN_TEXTURE), location -> location), Either::right
    );


}