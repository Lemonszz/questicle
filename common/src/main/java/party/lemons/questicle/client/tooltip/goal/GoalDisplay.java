package party.lemons.questicle.client.tooltip.goal;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import org.joml.Vector2i;
import party.lemons.questicle.client.gui.GuiObject;
import party.lemons.questicle.client.tooltip.TextTooltip;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

public interface GoalDisplay<T extends Goal> extends GuiObject<GoalDisplay.GoalDisplayContext<T>>
{
    Vector2i getSize(GoalDisplayContext<T> context);

    default int getHeight(GoalDisplayContext<T> context)
    {
        return getSize(context).y;
    }

    default int getWidth(GoalDisplayContext<T> context) {
        return getSize(context).x;
    }

    record GoalDisplayContext<T extends Goal>(T goal, Quest quest, QuestStorage questStorage)
    {
        public static <T extends Goal> GoalDisplayContext<T> create(T goal, Quest quest, QuestStorage questStorage)
        {
            return new GoalDisplayContext<>(goal, quest, questStorage);
        }
    }
    default Renderable getTooltip(GoalDisplayContext<T> context, int drawX, int drawY, int mouseX, int mouseY) {
        T goal = context.goal();
        return new TextTooltip(goal.getHoverTooltip(context.questStorage()));
    }
}
