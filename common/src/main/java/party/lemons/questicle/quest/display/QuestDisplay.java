package party.lemons.questicle.quest.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;
import party.lemons.questicle.quest.quest.Quest;

public class QuestDisplay
{
    public static Codec<QuestDisplay> CODEC = RecordCodecBuilder.create(instance->instance.group(
            ResourceLocation.CODEC.fieldOf("quest").forGetter(QuestDisplay::questID),
            Codec.INT.fieldOf("x").forGetter(QuestDisplay::x),
            Codec.INT.fieldOf("y").forGetter(QuestDisplay::y),
            QuestFrameTypes.PRESET_CODEC.optionalFieldOf("frame", QuestFrame.DEFAULT).forGetter(QuestDisplay::frame)
            ).apply(instance, QuestDisplay::new));

    private final ResourceLocation quest;
    private final int x;
    private final int y;
    private final QuestFrame frame;

    private Quest cachedQuest = null;
    private boolean hasSearched = false;

    public QuestDisplay(ResourceLocation quest, int x, int y, QuestFrame frame)
    {
        this.quest = quest;
        this.x = x;
        this.y = y;
        this.frame = frame;
    }

    public ResourceLocation questID()
    {
        return quest;
    }

    public @Nullable Quest quest()  //TODO: make this non nullable to simplify logic elsewhere, have a default quest with a "?" icon or something
    {
        if(!hasSearched){
            hasSearched = true;

            if(Platform.getEnv() == EnvType.CLIENT)     //TODO: create a ClientQuestDisplay instead?
                cachedQuest = ClientStorage.clientQuests.getOrDefault(questID(), null);
            else
                cachedQuest = Quests.quests.getOrDefault(questID(), null);
        }

        return cachedQuest;
    }

    public int x(){
        return x;
    }

    public int y()
    {
        return y;
    }

    public QuestFrame frame()
    {
        return frame;
    }
}
