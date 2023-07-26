package party.lemons.questicle.client.tooltip.quest;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.quest.quest.Quest;

public abstract class QuestTooltip<T extends Quest>
{
    public abstract void render(GuiGraphics graphics, T quest, int drawX, int drawY, float delta);
}
