package party.lemons.questicle;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import party.lemons.questicle.client.reward.ItemRewardDisplay;
import party.lemons.questicle.client.reward.RewardDisplayRegistry;
import party.lemons.questicle.client.reward.XpRewardDisplay;
import party.lemons.questicle.client.tooltip.quest.StandardQuestTooltip;
import party.lemons.questicle.client.tooltip.TooltipRegistry;
import party.lemons.questicle.client.frame.PresetFrameLoader;
import party.lemons.questicle.client.frame.PresetFrameRenderer;
import party.lemons.questicle.client.frame.TextureFrameRenderer;
import party.lemons.questicle.client.gui.QuestScreen;
import party.lemons.questicle.client.icon.BlankFrameRenderer;
import party.lemons.questicle.client.icon.IconRenderers;
import party.lemons.questicle.client.icon.ItemIconRenderer;
import party.lemons.questicle.client.shader.QShaders;
import party.lemons.questicle.client.tooltip.goal.CollectGoalDisplay;
import party.lemons.questicle.client.tooltip.goal.GoalDisplayRegistry;
import party.lemons.questicle.client.tooltip.goal.KillMobDisplayGoal;
import party.lemons.questicle.client.widget.NineSliceWidgetRenderer;
import party.lemons.questicle.client.widget.RepeatingTextureWidgetRenderer;
import party.lemons.questicle.client.widget.WidgetRenderers;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;
import party.lemons.questicle.client.frame.FrameRenderers;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.quest.QuestTypes;
import party.lemons.questicle.quest.reward.RewardTypes;
import party.lemons.questicle.quest.widget.WidgetTypes;
import party.lemons.questicle.resource.GlobalResourcePack;

public class QuesticleClient
{
    public static int tick = Integer.MIN_VALUE;
    public static ResourceLocation FRAMES_TEXTURE = Questicle.id("textures/gui/frames.png");

    public static void init()
    {
        QShaders.init();

        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new PresetFrameLoader());

        GlobalResourcePack.init();

        //TODO: TEST : REMOVE THIS
        KeyMapping mapping = new KeyMapping("key_test", InputConstants.KEY_DELETE, "key_test");
        KeyMappingRegistry.register(mapping);
        ClientTickEvent.CLIENT_POST.register(c->
        {
            tick++;
            if(mapping.isDown() && !(c.screen instanceof QuestScreen))
                c.setScreen(new QuestScreen());
        });
    }

    /***
     * Register Quest Frame Renderers
     * TODO: Call this from some event so other mods can access it.
     */
    public static void initFrameRenderers()
    {
        FrameRenderers.registerRenderer(QuestFrameTypes.TEXTURE.get(), new TextureFrameRenderer());
        FrameRenderers.registerRenderer(QuestFrameTypes.PRESET.get(), new PresetFrameRenderer());
    }

    /***
     * Register Icon Renderers
     * TODO: Call this from some event so other mods can access it.
     */
    public static void initIconRenderers()
    {
        IconRenderers.registerRenderer(QuestIconTypes.ITEM.get(), new ItemIconRenderer());
        IconRenderers.registerRenderer(QuestIconTypes.BLANK.get(), new BlankFrameRenderer());
    }

    public static void initWidgetRenderers()
    {
        WidgetRenderers.registerRenderer(WidgetTypes.REPEATING.get(), new RepeatingTextureWidgetRenderer());
        WidgetRenderers.registerRenderer(WidgetTypes.NINE_SLICE.get(), new NineSliceWidgetRenderer());
    }

    public static void initTooltips() {
        TooltipRegistry.register(QuestTypes.STANDARD.get(), new StandardQuestTooltip());
    }

    public static void initGoalDisplays()
    {
        GoalDisplayRegistry.register(GoalTypes.COLLECT_GOAL.get(), new CollectGoalDisplay());
        GoalDisplayRegistry.register(GoalTypes.KILL_MOB.get(), new KillMobDisplayGoal());
    }

    public static void initRewardRenderers()
    {
        RewardDisplayRegistry.register(RewardTypes.ITEM.get(), new ItemRewardDisplay());
        RewardDisplayRegistry.register(RewardTypes.XP.get(), new XpRewardDisplay());
    }
}
