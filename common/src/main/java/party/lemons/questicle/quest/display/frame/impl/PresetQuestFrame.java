package party.lemons.questicle.quest.display.frame.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.client.frame.PresetFrames;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;

public class PresetQuestFrame implements QuestFrame {

    public static Codec<PresetQuestFrame> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(PresetQuestFrame::location)
        ).apply(instance, PresetQuestFrame::new));

    private ResourceLocation location;
    private QuestFrame presetFrame = null;

    public PresetQuestFrame(ResourceLocation presetLocation)
    {
        this.location = presetLocation;
    }

    public QuestFrame getPreset()
    {
        if(presetFrame == null)
        {
            presetFrame = PresetFrames.frames.getOrDefault(location, QuestFrame.DEFAULT);
        }

        return presetFrame;
    }

    public ResourceLocation location()
    {
        return this.location;
    }

    @Override
    public int width() {
        return getPreset().width();
    }

    @Override
    public int height() {
        return getPreset().height();
    }

    @Override
    public QuestFrameType<?> type() {
        return QuestFrameTypes.PRESET.get();
    }
}
