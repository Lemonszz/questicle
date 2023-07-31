package party.lemons.questicle.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import party.lemons.questicle.client.ClientConfig;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.client.DrawUtils;
import party.lemons.questicle.client.frame.FrameRenderer;
import party.lemons.questicle.client.frame.FrameRenderers;
import party.lemons.questicle.client.gui.renderable.NineSliceTexture;
import party.lemons.questicle.client.gui.renderable.TextureWidget;
import party.lemons.questicle.client.gui.widget.GuiObjectScrollWidget;
import party.lemons.questicle.client.gui.widget.button.IconButton;
import party.lemons.questicle.client.gui.widget.ListGuiObjectScrollWidget;
import party.lemons.questicle.client.gui.widget.button.TextButton;
import party.lemons.questicle.client.icon.IconRenderer;
import party.lemons.questicle.client.icon.IconRenderers;
import party.lemons.questicle.client.reward.RewardDisplay;
import party.lemons.questicle.client.reward.RewardDisplayRegistry;
import party.lemons.questicle.client.tooltip.TooltipRegistry;
import party.lemons.questicle.client.tooltip.goal.GoalDisplay;
import party.lemons.questicle.client.tooltip.goal.GoalDisplayRegistry;
import party.lemons.questicle.client.tooltip.quest.QuestTooltip;
import party.lemons.questicle.client.widget.*;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.QuestList;
import party.lemons.questicle.quest.QuestLists;
import party.lemons.questicle.quest.display.QuestDisplay;
import party.lemons.questicle.quest.display.frame.QuestFrame;
import party.lemons.questicle.quest.goal.Goal;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.QuestDependency;
import party.lemons.questicle.quest.quest.QuestStatus;
import party.lemons.questicle.quest.quest.storage.QuestStorage;
import party.lemons.questicle.quest.reward.Reward;
import party.lemons.questicle.quest.widget.Widget;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class QuestScreen extends Screen
{
    private static final int LEFT = 10, TOP = 10, RIGHT = -10, BOTTOM = -10;
    private static final int CATEGORY_SECTION_WIDTH = 100;
    private static float zoom = 1.0F;
    private static final float ZOOM_CHANGE = 0.1F, ZOOM_MIN = 0.5F, ZOOM_MAX = 3.0F;


    private int questsX, questsY, questsWidth, questsHeight, questsEndX, questsEndY;
    private int displayX, displayY, displayWidth;
    private int maxX = 1000, maxY = 1000, minX = -1000, minY = -1000;
    private static double scrollX, scrollY;
    private boolean isScrolling;

    public static QuestList displayList;
    public static QuestList sidebarList = null;
    private QuestTooltip questTooltip = null;
    private Quest hoverQuest = null;
    private Quest displayQuest = null;

    public QuestScreen() {
        super(GameNarrator.NO_TITLE);
    }

    public static void setQuestList(QuestList list)
    {
        displayList = list;
        scrollX = 0;
        scrollY = 0;
    }

    public static void resetPersistentData()
    {
        scrollX = 0;
        scrollY = 0;
        zoom = 1.0F;

        List<QuestList> sortedLists = ClientStorage.getSortedQuestLists();
        if(!sortedLists.isEmpty())
            displayList = sortedLists.get(0);
    }

    @Override
    protected void init()
    {
        List<QuestList> sortedLists = ClientStorage.getSortedQuestLists();
        if(sortedLists.isEmpty())
        {
            Minecraft.getInstance().setScreen(null);
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("questicle.error.no_quests"));
            return;
        }
        if(displayList == null)
        {
            displayList = sortedLists.get(0);
        }


        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();

        int panelWidth = (screenWidth - LEFT) + RIGHT;
        int panelHeight = (screenHeight - TOP) + BOTTOM;

        int categoryX = 5;
        int sectionTop = 5;
        int sectionHeight = panelHeight - (sectionTop * 2);
        int categoryWidth = CATEGORY_SECTION_WIDTH;

        int questsX = 5 + categoryWidth + 3;
        int questsRightOffset = displayQuest == null ? 5 : 150;
        int questsWidth = panelWidth - questsX - questsRightOffset;

        boolean hasListList = QuestLists.lists.size() > 1;



        addRenderableOnly(new TextureWidget(QComponents.PANEL_BG_STANDARD_SQUARE, LEFT, TOP, panelWidth, panelHeight));

        if(hasListList) {
            addRenderableOnly(new TextureWidget(QComponents.PANEL_BLACK_SQUARE, LEFT + categoryX, TOP + sectionTop, categoryWidth, sectionHeight)); //Quest list button panel

            ListGuiObjectScrollWidget<QuestListButtonGuiObject.QuestListButtonContext> lists = addRenderableWidget(new ListGuiObjectScrollWidget<>(LEFT + categoryX, TOP + sectionTop, categoryWidth, sectionHeight, 0));

            for(QuestList list : sortedLists)
            {
                QuestListButtonGuiObject.QuestListButtonContext ctx = new QuestListButtonGuiObject.QuestListButtonContext(list);
                lists.add(new QuestListButtonGuiObject(categoryWidth, 24, QComponents.PANEL_BLUE_SQUARE, QComponents.PANEL_GOLD_SQUARE, QComponents.PANEL_GOLD_SQUARE), ctx);
            }
        }
        else {
            questsX = categoryX;    //No list buttons, so we can move the quests across
            questsWidth += categoryWidth;
        }
        addRenderableOnly(new TextureWidget(QComponents.PANEL_BLACK_SQUARE, LEFT + questsX, TOP + sectionTop, questsWidth, sectionHeight));

        this.questsX = LEFT + questsX;
        this.questsY = TOP + sectionTop;
        this.questsWidth = questsWidth;
        this.questsHeight = sectionHeight;
        this.questsEndX = this.questsX + this.questsWidth;
        this.questsEndY = this.questsY + this.questsHeight;

        if(displayQuest != null)    //TODO: split this out
        {
            displayX = questsEndX + 3;
            displayY = TOP + sectionTop;
            this.displayWidth = panelWidth - displayX + 5;

            addRenderableOnly(new TextureWidget(QComponents.PANEL_BLACK_SQUARE, displayX, displayY, displayWidth, sectionHeight));

            //Description
            int remainingHeight = screenHeight - 45;

            int descriptionHeight = (int) (remainingHeight * 0.17F);
            int goalsHeight = (int) (remainingHeight * 0.43F);


            MutableComponent description = ClientConfig.applyQuestFont(Component.translatableWithFallback(displayQuest.description(), displayQuest.description()));
            int wrapWidth = displayWidth - 7;
            WrappedStringGuiObject.StringContext descContext = new WrappedStringGuiObject.StringContext(description, wrapWidth- 8, 0xFFFFFF, Minecraft.getInstance().font);
            GuiObjectScrollWidget<WrappedStringGuiObject.StringContext> descriptionWidget = addRenderableWidget(new GuiObjectScrollWidget<>(displayX + 5, displayY + 45, wrapWidth, descriptionHeight, new WrappedStringGuiObject(), descContext));

            int goalY = displayY + 45 + Math.min(descriptionWidget.getHeight(), descriptionWidget.getInnerHeight()) + 5;
            ListGuiObjectScrollWidget<GoalDisplay.GoalDisplayContext> goalList = addRenderableWidget(new ListGuiObjectScrollWidget<>(displayX + 5, goalY, wrapWidth, goalsHeight, 3));

            QuestStorage storage = ClientStorage.clientParty.getStorage().getQuestProgress(displayQuest);
            for(Goal goal : displayQuest.goals())
            {
                GoalDisplay goalDisplay = GoalDisplayRegistry.getTooltip(goal.type());
                GoalDisplay.GoalDisplayContext<?> ctx = GoalDisplay.GoalDisplayContext.create(goal, displayQuest, storage);
                goalList.add(goalDisplay, ctx);
            }

            //Rewards
            int rewardY = sectionHeight - 4 - 20;

            addRenderableOnly(new TextureWidget(QComponents.PANEL_BLACK_SQUARE, displayX, rewardY - 4, displayWidth, 43));

            int rewardX = displayX + 5;
            for(Reward reward : displayQuest.rewards())
            {
                RewardDisplay rewardDisplay = RewardDisplayRegistry.getDisplay(reward.type());
                if(rewardDisplay != null) {

                    RewardDisplay.RewardDisplayContext ctx = new RewardDisplay.RewardDisplayContext<>(reward);
                    addRenderableWidget(new GuiObjectWidget<RewardDisplay.RewardDisplayContext<?>>(rewardDisplay, ctx, rewardX, rewardY));
                    rewardX += rewardDisplay.getWidth(ctx);
                }
            }

            //Claim
            TextButton claimButton = addRenderableWidget(new TextButton(
                    Component.translatable("questicle.text.claim_rewards"),
                    QComponents.PANEL_FLAT_GOLD, QComponents.PANEL_FLAT_GOLD_HOVER, QComponents.PANEL_FLAT_DISABLED,
                    displayX + 3, sectionHeight - 4, displayWidth - 6, 16,
                        (b)->{
                            ClientStorage.sendClaimRequest(displayQuest);
                            b.active = false;
                        }
                    )
            );
            claimButton.active = displayQuest.getQuestStatus(ClientStorage.clientParty.getStorage()) == QuestStatus.COMPLETE && !storage.hasPlayerClaimed(Minecraft.getInstance().player);

            //Icons
            addRenderableWidget(
                    new IconButton(QComponents.ICON_CROSS, QComponents.ICON_CROSS_HOVER, QComponents.ICON_CROSS_DISABLED,
                            displayX + 5, displayY + 5, 7, 7,
                            (b)->{
                                displayQuest = null;
                                init(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
                            }
            ));

            addRenderableWidget(
                    new IconButton(QComponents.ICON_PIN, QComponents.ICON_PIN_HOVER, QComponents.ICON_PIN_DISABLED,
                            displayX + displayWidth - 12, displayY + 5, 7, 7,
                            (b)->{
                                //pin displayed
                            }
                    ));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, delta);

        renderInside(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY, delta);
    }

    private void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        if(hoverQuest != null && questTooltip != null)
        {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 1000);

            questTooltip.render(guiGraphics, hoverQuest, mouseX + 5, mouseY - 5, delta);

            guiGraphics.pose().popPose();
        }

        for (GuiEventListener listener : children())
        {
            if(listener instanceof TooltipProvider tooltipProvider) {
                Renderable tooltip = tooltipProvider.getWidgetTooltip();
                if (tooltip != null) {
                    tooltip.render(guiGraphics, mouseX, mouseY, delta);
                    break;
                }
            }
        }
    }

    private void renderInside(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        final int TILE_TEXTURE_SIZE = 26;

        g.enableScissor(2 + questsX, 2 + questsY, questsEndX - 2, questsEndY - 2);
        g.pose().pushPose();

        ResourceLocation backgroundTexture = displayList.backgroundImage();

        int scrollX = Mth.floor(QuestScreen.scrollX);
        int scrollY = Mth.floor(QuestScreen.scrollY);
        int startX =  (scrollX % TILE_TEXTURE_SIZE);
        int startY = (scrollY % TILE_TEXTURE_SIZE);

        g.drawString(Minecraft.getInstance().font, minecraft.fpsString.split(" ")[0], questsX + 10, questsY + 10, 0xFFFFFF);

        DrawUtils.blitRepeating(g, backgroundTexture,
                -TILE_TEXTURE_SIZE + questsX + startX,
                -TILE_TEXTURE_SIZE + questsY + startY,
                questsWidth + TILE_TEXTURE_SIZE * 2,
                questsHeight + TILE_TEXTURE_SIZE * 2,
                0, 0,
                TILE_TEXTURE_SIZE, TILE_TEXTURE_SIZE
        );

        g.pose().pushPose();
        g.pose().scale(zoom, zoom, zoom);

        renderWidgets(displayList.backgroundWidgets(), g, mouseX, mouseY, delta);

        renderQuestConnections(g);
        renderQuestDisplays(g, mouseX / zoom, mouseY / zoom, mouseX, mouseY, delta);

        renderWidgets(displayList.foregroundWidgets(), g, mouseX, mouseY, delta);

        g.pose().popPose();

        g.pose().popPose();
        g.disableScissor();

        renderSideDisplay(g, mouseX, mouseY, delta);
    }

    private void renderSideDisplay(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        if(displayQuest == null)
            return;

        NineSliceTexture titleBGTexture = QComponents.PANEL_BLUE_SQUARE;
        titleBGTexture.render(g, displayX, displayY + 19, displayWidth, 17);

        Component title = ClientConfig.applyQuestFont(Component.translatable(displayQuest.displayName()));
        renderDisplayAt(g, sidebarList.getOrNull(displayQuest), displayX + 4, displayY + 15, mouseX, mouseY, delta, false, false);

        g.drawString(Minecraft.getInstance().font, title, displayX + 30, displayY + 23, 0xFFFFFF);  //TODO: scrolling string
    }

    private void renderWidgets(List<Widget> widgets, GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        int startX = questsCenterX();
        int startY = questsCenterY();

        for(Widget widget : widgets)
        {
            WidgetRenderer renderer =  WidgetRenderers.getRenderer(widget.type());
            renderer.render(g, widget, startX, startY, mouseX, mouseY, delta);
        }
    }

    private void renderQuestConnections(GuiGraphics g)
    {
        //TODO: Connection types

        if(displayList == null)
            return;

        int startX = questsCenterX();
        int startY = questsCenterY();

        for(QuestDisplay display : displayList.quests())
        {
            if(shouldRender(display))
            {
                Quest quest = display.quest();

                if(quest == null)
                    continue;

                int endX = startX + (26 * display.x()) + (display.frame().width() / 2);
                int endY = startY + (26 * display.y()) + (display.frame().width() / 2);

                for(QuestDependency dependency : quest.dependencies())
                {
                    QuestDisplay parentDisplay = displayList.getOrNull(dependency.quest());
                    if(parentDisplay != null)
                    {
                        int parentX = startX + (26 * parentDisplay.x()) + (parentDisplay.frame().width() / 2);
                        int parentY = startY + (26 * parentDisplay.y()) + (parentDisplay.frame().width() / 2);

                        DrawUtils.drawLine(g, parentX, parentY, endX, endY, 7);
                    }
                }
            }
        }
    }

    private void renderQuestDisplays(GuiGraphics g, double zoomMouseX, double zoomMouseY, double mouseX, double mouseY, float delta)
    {
        questTooltip = null;
        hoverQuest = null;

        if(displayList == null)
            return;

        for(QuestDisplay display : displayList.quests())
        {
            if(shouldRender(display))
            {
                renderDisplay(g, display, zoomMouseX, zoomMouseY, mouseX, mouseY, delta, true, true);
            }
        }
    }

    private void renderDisplay(GuiGraphics g, QuestDisplay display, double zoomMouseX, double zoomMouseY, double mouseX, double mouseY, float delta, boolean checkHover, boolean checkStatus)
    {
        int startX = questsCenterX();
        int startY = questsCenterY();

        int drawX = startX + (26 * display.x());
        int drawY = startY + (26 * display.y());

        Quest quest = display.quest();
        if(quest == null)
            return;

        boolean hover = checkHover && isOverQuestArea(mouseX, mouseY) && zoomMouseX >= drawX && zoomMouseY >= drawY && zoomMouseX < drawX + display.frame().width() && zoomMouseY < drawY + display.frame().height();

        renderDisplayAt(g, display, drawX, drawY, (int)mouseX, (int)mouseY, delta, hover, checkStatus);

        if(hover)
        {
            questTooltip = TooltipRegistry.getTooltip(quest.type());
            hoverQuest = quest;
        }
    }

    private void renderDisplayAt(GuiGraphics g, QuestDisplay display, int drawX, int drawY, int mouseX, int mouseY, float delta, boolean hover, boolean checkStatus)
    {
        //Frame
        QuestFrame frame = display.frame();
        FrameRenderer frameRenderer = FrameRenderers.getRenderer(frame.type());
        frameRenderer.render(g, display.frame(), display, drawX, drawY, delta, hover);

        //Icon
        Quest quest = display.quest();
        if(quest == null)
            return;

        PartyStorage partyStorage = ClientStorage.clientParty.getStorage();
        if(checkStatus && quest.getQuestStatus(partyStorage) == QuestStatus.UNAVAILABLE)
            g.setColor(0.2F, 0.2F, 0.2F, 1.0F);


        IconRenderer iconRenderer = IconRenderers.getRenderer(quest.icon().type());
        iconRenderer.render(g, drawX, drawY, new IconRenderer.IconRendererContext<>(quest.icon(), display.frame()), mouseX, mouseY, delta);

        g.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        //Decoration
        if(partyStorage.isQuestCompleted(quest) && partyStorage.hasPendingRewards(Minecraft.getInstance().player, quest))
        {
            frameRenderer.renderRewardPending(g, display.frame(), drawX, drawY, delta, hover);
        }
    }

    //TODO: these could be precalculated on move/zoom
    public int questsCenterX()
    {
        return questsX + (int)((questsWidth / 2) / zoom) + (int)scrollX;
    }

    public int questsCenterY()
    {
        return questsY + (int)((questsHeight / 2) / zoom) + (int)scrollY;
    }

    private boolean shouldRender(QuestDisplay display)
    {
        //TODO: is in bounds
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {

        if(!super.mouseScrolled(mouseX, mouseY, scroll) && isOverQuestArea(mouseX, mouseY))
        {
            int mX = (int)(mouseX / zoom) - questsCenterX();
            int mY = (int)(mouseY / zoom) - questsCenterY();

            zoom = (float)Mth.clamp(zoom + (scroll * ZOOM_CHANGE), ZOOM_MIN, ZOOM_MAX);

            int newMx = (int)(mouseX / zoom) - questsCenterX();
            int newMy = (int)(mouseY / zoom) - questsCenterY();

            scrollX += newMx - mX;
            scrollY += newMy - mY;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double changeX, double changeY) {
        if(super.mouseDragged(mouseX, mouseY, button, changeX, changeY))
            return true;

        if (button != InputConstants.MOUSE_BUTTON_LEFT || (!isOverQuestArea(mouseX, mouseY) && !isScrolling))
        {
            this.isScrolling = false;
            return false;
        }
        else
        {
            if (!this.isScrolling)
            {
                this.isScrolling = true;
            }
            else
            {
                addScroll(changeX / zoom, changeY / zoom);
            }
            return true;
        }

    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        isScrolling = false;
        return super.mouseReleased(d, e, i);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!super.mouseClicked(mouseX, mouseY, button) && button == InputConstants.MOUSE_BUTTON_LEFT)
        {
            if(hoverQuest != null && displayQuest != hoverQuest)
            {
                sidebarList = displayList;
                displayQuest = hoverQuest;
                init(minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
                return true;
            }
        }
        return false;
    }

    public void addScroll(double scrollX, double scrollY) {
        setScroll(QuestScreen.scrollX + scrollX, QuestScreen.scrollY + scrollY);
    }

    public void setScroll(double scrollX, double scrollY)
    {
        //TODO: bounds
        QuestScreen.scrollX = scrollX;
        QuestScreen.scrollY = scrollY;
    }

    public boolean isOverQuestArea(double mouseX, double mouseY)
    {
        return
                mouseX >= questsX &&
                mouseX <  questsEndX &&
                mouseY >= questsY &&
                mouseY <= questsEndY;

    }
}