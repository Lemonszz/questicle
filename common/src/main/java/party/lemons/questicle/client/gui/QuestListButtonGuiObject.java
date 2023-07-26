package party.lemons.questicle.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import party.lemons.questicle.client.gui.renderable.RenderComponent;
import party.lemons.questicle.client.icon.IconRenderer;
import party.lemons.questicle.client.icon.IconRenderers;
import party.lemons.questicle.quest.QuestList;

public class QuestListButtonGuiObject implements GuiObject<QuestListButtonGuiObject.QuestListButtonContext>, ClickableObject<QuestListButtonGuiObject.QuestListButtonContext>
{
    private final int width, height;
    private final RenderComponent standard, hover, selected;

    public QuestListButtonGuiObject(int width, int height, RenderComponent standard, RenderComponent hover, RenderComponent selected)
    {
        this.width = width;
        this.height = height;
        this.standard = standard;
        this.hover = hover;
        this.selected = selected;
    }


    @Override
    public void render(GuiGraphics graphics, int drawX, int drawY, QuestListButtonContext context, int mouseX, int mouseY, float delta)
    {
        RenderComponent background;
        if(isMouseOver(drawX, drawY, mouseX, mouseY, context))
            background = hover;
        else if(context.list().equals(QuestScreen.displayList))
            background = selected;
        else
            background = standard;

        background.render(graphics, drawX, drawY, width, height);

        IconRenderer iconRenderer = IconRenderers.getRenderer(context.list().icon().type());
        int iconWidth = 0;
        if(iconRenderer != null) {
            IconRenderer.IconRendererContext iconCtx = IconRenderer.IconRendererContext.create(context.list().icon(), null);
            int iconY = drawY + 4;

            iconRenderer.render(graphics, drawX + 3, iconY,iconCtx, mouseX, mouseY, delta);
            iconWidth = iconRenderer.getWidth(iconCtx);
        }
        graphics.drawString(Minecraft.getInstance().font, Component.translatable(context.list().name()), drawX + 4 + iconWidth, drawY + (height / 2) - (Minecraft.getInstance().font.lineHeight / 2), 0xFFFFFF);


    }

    @Override
    public int getHeight(QuestListButtonContext context) {
        return height;
    }

    @Override
    public int getWidth(QuestListButtonContext context) {
        return width;
    }

    @Override
    public void onClick(QuestListButtonContext context) {
        //TODO: play click sound
        QuestScreen.setQuestList(context.list());
    }

    public record QuestListButtonContext(QuestList list){

    }
}
