package party.lemons.questicle.quest.widget.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import party.lemons.questicle.client.texture.TextureMode;
import party.lemons.questicle.quest.widget.Widget;
import party.lemons.questicle.quest.widget.WidgetType;
import party.lemons.questicle.quest.widget.WidgetTypes;

public record RepeatingTextureWidget(int x, int y, int width, int height, ResourceLocation texture, float textureWidth, float textureHeight, TextureMode textureMode) implements Widget {

    public static Codec<RepeatingTextureWidget> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("x").forGetter(RepeatingTextureWidget::x),
                    Codec.INT.fieldOf("y").forGetter(RepeatingTextureWidget::y),
                    Codec.INT.fieldOf("width").forGetter(RepeatingTextureWidget::width),
                    Codec.INT.fieldOf("height").forGetter(RepeatingTextureWidget::height),
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(RepeatingTextureWidget::texture),
                    Codec.FLOAT.optionalFieldOf("texture_width", -1F).forGetter(RepeatingTextureWidget::textureWidth),
                    Codec.FLOAT.optionalFieldOf("texture_height", -1F).forGetter(RepeatingTextureWidget::textureHeight),
                    StringRepresentable.fromEnum(TextureMode::values).optionalFieldOf("mode", TextureMode.DIRECT).forGetter(RepeatingTextureWidget::textureMode)
            ).apply(instance, RepeatingTextureWidget::new));

    @Override
    public WidgetType<?> type() {
        return WidgetTypes.REPEATING.get();
    }
}
