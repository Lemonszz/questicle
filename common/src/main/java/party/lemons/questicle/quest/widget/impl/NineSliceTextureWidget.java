package party.lemons.questicle.quest.widget.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import party.lemons.questicle.client.texture.TextureMode;
import party.lemons.questicle.quest.widget.Widget;
import party.lemons.questicle.quest.widget.WidgetType;
import party.lemons.questicle.quest.widget.WidgetTypes;

public record NineSliceTextureWidget(int x, int y, int width, int height, boolean fill, int border, int textureX, int textureY, int textureWidth, int textureHeight, ResourceLocation texture, int sheetWidth, int sheetHeight) implements Widget {

    public static Codec<NineSliceTextureWidget> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("x").forGetter(NineSliceTextureWidget::x),
                    Codec.INT.fieldOf("y").forGetter(NineSliceTextureWidget::y),
                    Codec.INT.fieldOf("width").forGetter(NineSliceTextureWidget::width),
                    Codec.INT.fieldOf("height").forGetter(NineSliceTextureWidget::height),
                    Codec.BOOL.optionalFieldOf("fill", true).forGetter(NineSliceTextureWidget::fill),
                    Codec.INT.fieldOf("border").forGetter(NineSliceTextureWidget::border),
                    Codec.INT.fieldOf("texture_x").forGetter(NineSliceTextureWidget::textureX),
                    Codec.INT.fieldOf("texture_y").forGetter(NineSliceTextureWidget::textureY),
                    Codec.INT.fieldOf("texture_width").forGetter(NineSliceTextureWidget::textureWidth),
                    Codec.INT.fieldOf("texture_height").forGetter(NineSliceTextureWidget::textureHeight),
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(NineSliceTextureWidget::texture),
                    Codec.INT.optionalFieldOf("sheet_width", 256).forGetter(NineSliceTextureWidget::sheetWidth),
                    Codec.INT.optionalFieldOf("sheet_height", 255).forGetter(NineSliceTextureWidget::sheetHeight)
            ).apply(instance, NineSliceTextureWidget::new));

    @Override
    public WidgetType<?> type() {
        return WidgetTypes.NINE_SLICE.get();
    }
}
