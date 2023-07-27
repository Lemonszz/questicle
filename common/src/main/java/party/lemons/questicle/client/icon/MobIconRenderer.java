package party.lemons.questicle.client.icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.quest.icon.impl.ItemQuestIcon;
import party.lemons.questicle.quest.icon.impl.MobQuestIcon;

import java.util.List;

public class MobIconRenderer implements IconRenderer<MobQuestIcon> {
    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, IconRendererContext<MobQuestIcon> context, int mouseX, int mouseY, float delta)
    {
        List<EntityType<?>> validMobs = List.of(context.icon().entityType());
        Entity e = context.icon().entityType().create(Minecraft.getInstance().level);
        float scale = context.icon().scale();

        float mobHeight = e.getBbHeight();
        if(e.getBbHeight() >= DrawUtils.MOB_DOWNSCALE_SIZE)
            mobHeight /= 3;

        int yOffset = (int) ((context.getFrameHeight() / 2) + ((mobHeight * 2) * scale));

        DrawUtils.drawEntityIcon(graphics, validMobs, context.icon().tag(), drawX + context.getFrameWidth() / 2, drawY + yOffset, context.icon().scale());
    }
}
