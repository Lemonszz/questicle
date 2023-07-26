package party.lemons.questicle.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import party.lemons.questicle.client.gui.QComponents;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.icon.QuestIcon;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.icon.impl.BlankQuestIcon;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.widget.Widget;
import party.lemons.questicle.quest.widget.WidgetTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * A QuestList is a collection of {@link QuestDisplay} that will be displayed together.
 * The QuestList is responsible for holding:
 *  - The background of the panel display
 *  - It's display name
 *  - An icon
 *  - The list of {@link QuestDisplay}
 *
 *  A QuestList can hold many different {@link QuestDisplay}. A quest display can hold ANY quest.
 *  A quest can be in many Quest Displays.
 *
 * @see QuestDisplay
 *
 * @param name  the name of this list
 * @param priority used for sorting in the quest menu
 * @param backgroundImage   the background image of this list
 * @param icon  an icon to represent this display
 * @param quests the list of quest displays
 */
public record QuestList(String name, int priority, ResourceLocation backgroundImage, QuestIcon icon, List<QuestDisplay> quests, List<Widget> backgroundWidgets, List<Widget> foregroundWidgets) implements Comparable<QuestList>
{
    //TODO: should "quests" be renamed to "quest_displays" or "displays". This may reduce confusion between the two concepts

    public static Codec<QuestList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(QuestList::name),
                    Codec.INT.optionalFieldOf("priority", 99).forGetter(QuestList::priority),
                    ResourceLocation.CODEC.optionalFieldOf("background_image", QComponents.DEFAULT_QUEST_LIST_BACKGROUND).forGetter(QuestList::backgroundImage),
                    QuestIconTypes.CODEC.optionalFieldOf("icon", BlankQuestIcon.INSTANCE).forGetter(QuestList::icon),
                    QuestDisplay.CODEC.listOf().fieldOf("quests").forGetter(QuestList::quests),
                    WidgetTypes.CODEC.listOf().optionalFieldOf("background_widgets", new ArrayList<>()).forGetter(QuestList::backgroundWidgets),
                    WidgetTypes.CODEC.listOf().optionalFieldOf("foreground_widgets", new ArrayList<>()).forGetter(QuestList::foregroundWidgets)
            ).apply(instance, QuestList::new)
    );

    /**
     * Gets a {@link QuestDisplay} from the quest display list.
     * @param location
     * @return display or null
     */
    public QuestDisplay getOrNull(ResourceLocation location)
    {
        for(QuestDisplay display : quests)  //TODO: Cache?
        {
            if(display.questID().equals(location))
                return display;
        }

        return null;
    }

    /**
     * Gets a {@link QuestDisplay} from the quest display list.
     * @param quest
     * @return display or null
     */
    public QuestDisplay getOrNull(Quest quest)
    {
        return getOrNull(quest.id());
    }

    @Override
    public int compareTo(@NotNull QuestList o) {
        return priority - o.priority;
    }
}
