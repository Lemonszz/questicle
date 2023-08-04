package party.lemons.questicle.client.frame;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import party.lemons.questicle.quest.QuestList;
import party.lemons.questicle.quest.QuestLists;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;

import java.util.Map;

public class PresetFrameLoader extends SimpleJsonResourceReloadListener
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    public PresetFrameLoader()
    {
        super(GSON, "quest_frame_presets");
    }

    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler)
    {
        PresetFrames.frames.clear();

        elements.forEach((resourceLocation, jsonElement) ->
        {
            if(jsonElement.isJsonObject())
            {
                if(jsonElement.getAsJsonObject().has("type"))
                {
                    if(jsonElement.getAsJsonObject().get("type").getAsString().equals("questicle:preset"))
                        return;
                }
            }

            QuestFrame frame = QuestFrameTypes.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow(false, LOGGER::error).getFirst();

            PresetFrames.frames.put(resourceLocation, frame);
        });
    }
}