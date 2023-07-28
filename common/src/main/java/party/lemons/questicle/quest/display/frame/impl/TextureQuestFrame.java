package party.lemons.questicle.quest.display.frame.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.client.texture.TextureData;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;

public record TextureQuestFrame(int width, int height, TextureData standardDef, TextureData hoverDef, TextureData completeDef, TextureData disabledDef, int textureWidth, int textureHeight) implements QuestFrame {

    public static Codec<TextureQuestFrame> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.INT.optionalFieldOf("width", 26).forGetter(TextureQuestFrame::width),
                Codec.INT.optionalFieldOf("height", 26).forGetter(TextureQuestFrame::height),
                TextureData.CODEC.fieldOf("standard").forGetter(TextureQuestFrame::standardDef),
                TextureData.CODEC.fieldOf("hover").forGetter(TextureQuestFrame::hoverDef),
                TextureData.CODEC.fieldOf("complete").forGetter(TextureQuestFrame::completeDef),
                TextureData.CODEC.fieldOf("disabled").forGetter(TextureQuestFrame::disabledDef),
                Codec.INT.optionalFieldOf("texture_width", 255).forGetter(TextureQuestFrame::textureWidth),
                Codec.INT.optionalFieldOf("texture_height", 255).forGetter(TextureQuestFrame::textureHeight)
        ).apply(instance, TextureQuestFrame::new));

    @Override
    public QuestFrameType<?> type() {
        return QuestFrameTypes.TEXTURE.get();
    }
}
