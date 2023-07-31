package party.lemons.questicle.client;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class ClientConfig
{
    public static ResourceLocation questFont = new ResourceLocation("uniform");

    public static MutableComponent applyQuestFont(MutableComponent component)
    {
        return component.withStyle(component.getStyle().withFont(questFont));
    }

    public static Style questFontStyle()
    {
        return Style.EMPTY.withFont(questFont);
    }
}
