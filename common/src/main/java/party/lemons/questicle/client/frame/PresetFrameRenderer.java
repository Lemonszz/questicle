package party.lemons.questicle.client.frame;

import net.minecraft.client.gui.GuiGraphics;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.QuestFrameType;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;
import party.lemons.questicle.quest.display.frame.impl.PresetQuestFrame;

public class PresetFrameRenderer implements FrameRenderer<PresetQuestFrame> {
    @Override
    public void render(GuiGraphics g, PresetQuestFrame frame, QuestDisplay display, int drawX, int drawY, float delta, boolean hovered) {
        QuestFrameType<?> otherType = frame.getPreset().type();
        if(otherType == QuestFrameTypes.PRESET.get())
            return;

        FrameRenderer renderer = FrameRenderers.getRenderer(otherType);
        renderer.render(g, frame.getPreset(), display, drawX, drawY, delta, hovered);
    }
}
