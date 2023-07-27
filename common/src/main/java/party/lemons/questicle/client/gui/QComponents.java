package party.lemons.questicle.client.gui;

import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.client.gui.renderable.NineSliceTexture;
import party.lemons.questicle.client.gui.renderable.StandardTexture;

public class QComponents
{
    public static final ResourceLocation PANELS = Questicle.id("textures/gui/panels.png");
    public static final ResourceLocation FRAMES = Questicle.id("textures/gui/frames.png");
    public static final ResourceLocation DEFAULT_QUEST_LIST_BACKGROUND = Questicle.id("textures/gui/background_default.png");
    public static final NineSliceTexture PANEL_BG_STANDARD = new NineSliceTexture(PANELS,0,0, 11, 11, 4, 4, 256, 256, true);
    public static final NineSliceTexture PANEL_BG_STANDARD_SQUARE = new NineSliceTexture(PANELS,11,0, 11, 11, 4, 4, 256, 256, true);

    public static final NineSliceTexture PANEL_BLACK_ROUND = new NineSliceTexture(PANELS,0,11, 10, 10, 4, 5, 256, 256, true);
    public static final NineSliceTexture PANEL_WHITE_ROUND = new NineSliceTexture(PANELS,0,21, 10, 10, 4, 5, 256, 256, true);
    public static final NineSliceTexture PANEL_BLUE_SQUARE = new NineSliceTexture(PANELS,0,31, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_GRAY_SQUARE = new NineSliceTexture(PANELS,7,38, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_BLACK_SQUARE = new NineSliceTexture(PANELS,7,31, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_GOLD_SQUARE = new NineSliceTexture(PANELS,7,38, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_WHITE_SQUARE = new NineSliceTexture(PANELS,0,38, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_GREEN_SQUARE = new NineSliceTexture(PANELS,0,45, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_CREAM = new NineSliceTexture(PANELS,0,59, 3, 3, 1, 1, 256, 256, true);
    public static final NineSliceTexture PANEL_CREAM_DARK = new NineSliceTexture(PANELS,3,59, 3, 3, 1, 1, 256, 256, true);
    public static final NineSliceTexture PANEL_PURPLE = new NineSliceTexture(PANELS,6,59, 3, 3, 1, 1, 256, 256, true);

    public static final NineSliceTexture PANEL_FLAT_GOLD = new NineSliceTexture(PANELS,0,62, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_FLAT_GOLD_HOVER = new NineSliceTexture(PANELS,0,69, 7, 7, 3, 3, 256, 256, true);
    public static final NineSliceTexture PANEL_FLAT_DISABLED = new NineSliceTexture(PANELS,0,76, 7, 7, 3, 3, 256, 256, true);

    public static final StandardTexture ICON_CROSS = new StandardTexture(FRAMES,8,249, 7, 7, 256, 256);
    public static final StandardTexture ICON_CROSS_HOVER = new StandardTexture(FRAMES,15,249, 7, 7, 256, 256);
    public static final StandardTexture ICON_CROSS_DISABLED = new StandardTexture(FRAMES,8,242, 7, 7, 256, 256);
    public static final StandardTexture ICON_PIN = new StandardTexture(FRAMES,22,248, 8, 8, 256, 256);
    public static final StandardTexture ICON_PIN_DISABLED = new StandardTexture(FRAMES,22,240, 8, 8, 256, 256);
    public static final StandardTexture ICON_PIN_HOVER = new StandardTexture(FRAMES,30,248, 8, 8, 256, 256);

    public static final StandardTexture ICON_TROPHY = new StandardTexture(FRAMES,0,184, 12, 12, 256, 256);
    public static final StandardTexture ICON_TROPHY_HOVER = new StandardTexture(FRAMES,0,196, 12, 12, 256, 256);
    public static final StandardTexture ICON_TROPHY_DISABLED = new StandardTexture(FRAMES,0,208, 12, 12, 256, 256);

    public static final StandardTexture ICON_NETHER_PORTAL = new StandardTexture(FRAMES,175,0, 80, 80, 256, 256);

    public static final StandardTexture WIDGET_SCROLLBAR = new StandardTexture(FRAMES,48,240, 6, 16, 256, 256);
    public static final StandardTexture WIDGET_SCROLLBAR_HOVER = new StandardTexture(FRAMES,54,240, 6, 16, 256, 256);
}
