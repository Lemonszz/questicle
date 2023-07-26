package party.lemons.questicle.quest;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestTypes;

import java.util.HashMap;
import java.util.Map;

/***
 * Storage for Server side quest lists, loaded from {@link QuestListLoader}
 */
public class QuestLists
{
        /*
        TODO:
            this probably shouldn't be static be so it can be split into Client and Server Quest Lists
            Methods for getting list by ID and vice versa.
     */

    public static Map<ResourceLocation, QuestList> lists = new HashMap<>();

    public static void clear()
    {
        lists = new HashMap<>();
    }

    public static Codec<Map<ResourceLocation, QuestList>> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, QuestList.CODEC);
}
