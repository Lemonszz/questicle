package party.lemons.questicle.client.widget;

import net.minecraft.client.gui.components.Renderable;
import org.jetbrains.annotations.Nullable;

public interface TooltipProvider
{
    @Nullable Renderable getWidgetTooltip();
}
