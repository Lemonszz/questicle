package party.lemons.questicle.quest.display.frame.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.client.texture.TextureDef;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;

public record TextureQuestFrame(ResourceLocation texture, int width, int height, TextureDef standardDef, TextureDef hoverDef, TextureDef completeDef, TextureDef disabledDef, int textureWidth, int textureHeight) implements QuestFrame {

    public static Codec<TextureQuestFrame> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureQuestFrame::texture),
                Codec.INT.optionalFieldOf("width", 26).forGetter(TextureQuestFrame::width),
                Codec.INT.optionalFieldOf("height", 26).forGetter(TextureQuestFrame::height),
                TextureDef.CODEC.fieldOf("standard").forGetter(TextureQuestFrame::standardDef),
                TextureDef.CODEC.fieldOf("hover").forGetter(TextureQuestFrame::hoverDef),
                TextureDef.CODEC.fieldOf("complete").forGetter(TextureQuestFrame::completeDef),
                TextureDef.CODEC.fieldOf("disabled").forGetter(TextureQuestFrame::disabledDef),
                Codec.INT.optionalFieldOf("texture_width", 255).forGetter(TextureQuestFrame::textureWidth),
                Codec.INT.optionalFieldOf("texture_height", 255).forGetter(TextureQuestFrame::textureHeight)
        ).apply(instance, TextureQuestFrame::new));

    @Override
    public QuestFrameType<?> type() {
        return QuestFrameTypes.TEXTURE.get();
    }
}
