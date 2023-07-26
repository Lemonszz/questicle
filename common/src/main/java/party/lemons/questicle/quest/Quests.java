package party.lemons.questicle.quest;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestTypes;

import java.util.HashMap;
import java.util.Map;

/***
 * Storage for Server side quests, loaded from {@link QuestLoader}
 */
public class Quests
{
    /*
        TODO:
            this probably shouldn't be static be so it can be split into Client and Server Quests
            Methods for getting quests by ID and vice versa.
     */

    public static Map<ResourceLocation, Quest> quests = new HashMap<>();
    public static boolean isDirty = false;

    public static void clear()
    {
        quests = new HashMap<>();
    }

    public static Codec<Map<ResourceLocation, Quest>> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, QuestTypes.CODEC);
}
