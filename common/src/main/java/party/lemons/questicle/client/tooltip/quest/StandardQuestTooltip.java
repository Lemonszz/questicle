package party.lemons.questicle.client.tooltip.quest;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import party.lemons.questicle.client.ClientConfig;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.gui.QComponents;
import party.lemons.questicle.client.gui.renderable.NineSliceTexture;
import party.lemons.questicle.client.tooltip.goal.GoalDisplay;
import party.lemons.questicle.client.tooltip.goal.GoalDisplayRegistry;
import party.lemons.questicle.client.tooltip.quest.QuestTooltip;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.impl.StandardQuest;
import party.lemons.questicle.quest.quest.storage.QuestStorage;

public class StandardQuestTooltip extends QuestTooltip<StandardQuest>
{
    private static final int TITLE_COLOR = 0xFFFFFF;

    @Override
    public void render(GuiGraphics graphics, StandardQuest quest, int drawX, int drawY, float delta)     //TODO: a lot of this should be pulled out into other methods for easy overriding
    {
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        Font font = minecraft.font;


        final NineSliceTexture background = QComponents.PANEL_BLACK_ROUND;
        final NineSliceTexture titleBackground;
        switch (quest.getQuestStatus(ClientStorage.clientParty.getStorage())) {
            case AVAILABLE -> titleBackground = QComponents.PANEL_BLUE_SQUARE;
            case COMPLETE -> titleBackground = QComponents.PANEL_GREEN_SQUARE;
            default -> titleBackground = QComponents.PANEL_BLACK_SQUARE;
        }

        Component title = ClientConfig.applyQuestFont(Component.translatableWithFallback(quest.displayName(), quest.displayName()));

        int lineCount = 1 + quest.goals().size();
        int sizeY = DrawUtils.fontLineHeight() + (titleBackground.borderY() * 2) + 2;
        int titleWidth = font.width(title) + (background.borderX() * 2);

        Vector2i goalsSize = getGoalsSize(quest, background);
        sizeY += goalsSize.y + background.borderY() + 1;

        //Clamping
        int sizeX = Math.max(goalsSize.x, titleWidth);
        int height = sizeY;

        if(drawX > screenWidth - sizeX)
            drawX -= sizeX;
        if(drawY > screenHeight - height)
            drawY -= height;

        drawX = Mth.clamp(drawX, 0, screenWidth - sizeX);
        drawY = Mth.clamp(drawY, 0, screenHeight - height);

        //Background
        background.render(graphics, drawX, drawY, sizeX, sizeY);

        //Title
        drawTextWithBorder(title, titleBackground, graphics, drawX, drawY, font, TITLE_COLOR, sizeX);

        drawY += DrawUtils.fontLineHeight() * 2;

        QuestStorage storage = ClientStorage.clientParty.getStorage().getQuestProgress(quest);
        for(Goal goal : quest.goals())
        {
            GoalDisplay display = GoalDisplayRegistry.getTooltip(goal.type());
            if(display != null)
            {
                GoalDisplay.GoalDisplayContext ctx = GoalDisplay.GoalDisplayContext.create(goal, quest, storage);
                Vector2i size = display.getSize(ctx);
                display.render(graphics, drawX + background.borderX() + 1, drawY, ctx, 0, 0, delta);

                drawY += size.y;
            }
        }
    }

    public Vector2i getGoalsSize(Quest quest, NineSliceTexture background)
    {
        int xSize = 0;
        int ySize = 0;
        QuestStorage storage = ClientStorage.clientParty.getStorage().getQuestProgress(quest);
        for(Goal goal : quest.goals())
        {
            GoalDisplay display = GoalDisplayRegistry.getTooltip(goal.type());
            if(display != null)
            {
                GoalDisplay.GoalDisplayContext ctx = GoalDisplay.GoalDisplayContext.create(goal, quest, storage);
                Vector2i size = display.getSize(ctx);
                xSize = Math.max(xSize, size.x + background.borderX() + 1);
                ySize += size.y;
            }
        }

        return new Vector2i(xSize, ySize);
    }

    private static void drawTextWithBorder(Component text, NineSliceTexture border, GuiGraphics graphics, int drawX, int drawY, Font font, int color, int widthOverride)
    {
        int borderY = border.borderY() + 1;
        int borderX = border.borderX() + 1;

        int titleWidth = font.width(text);
        border.render(graphics, drawX, drawY, widthOverride == -1  ? titleWidth  + (borderY * 2) : widthOverride, DrawUtils.fontLineHeight() + (borderX * 2));
        graphics.drawString(font, text, drawX + border.borderX() + 1, drawY + border.borderY() + 1, color);
    }
}
