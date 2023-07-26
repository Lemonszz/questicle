package party.lemons.questicle.quest;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestTypes;

import java.util.Map;

/***
 * Loads quests from datapacks, stores in {@link Quests}
 */
public class QuestLoader extends SimpleJsonResourceReloadListener
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public QuestLoader()
    {
        super(GSON, "quests");
    }

    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler)
    {
        //Loop through each quest json, decode it and add it to the builders
        ImmutableMap.Builder<ResourceLocation, Quest> idToQuests = new ImmutableMap.Builder<>();
        elements.forEach((resourceLocation, jsonElement) ->
        {
            Quest quest = QuestTypes.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow(false, LOGGER::error).getFirst();
            if(quest.id().equals(Questicle.INVALID_RESOURCE))
                quest.setID(resourceLocation);

            idToQuests.put(resourceLocation, quest);
        });

        Quests.quests = idToQuests.build();
        Quests.isDirty = true;
    }
}