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

import java.util.Map;

/***
 * Loads QuestLists from data packs, stores in {@link QuestLists}
 */
public class QuestListLoader extends SimpleJsonResourceReloadListener
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public QuestListLoader()
    {
        super(GSON, "quest_list");
    }

    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler)
    {
        //Loop through each quest list json, decode it and add it to the builder
        ImmutableMap.Builder<ResourceLocation, QuestList> builder = new ImmutableMap.Builder<>();
        elements.forEach((resourceLocation, jsonElement) ->
        {
            QuestList list = QuestList.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow(false, LOGGER::error).getFirst();
            builder.put(resourceLocation, list);
        });

        QuestLists.lists = builder.build();
    }
}