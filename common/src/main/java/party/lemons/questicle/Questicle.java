package party.lemons.questicle;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.LevelResource;
import party.lemons.questicle.network.QuesticleNetwork;
import party.lemons.questicle.network.S2cSyncParty;
import party.lemons.questicle.network.S2cSyncQuests;
import party.lemons.questicle.party.PartyManager;
import party.lemons.questicle.party.QuestPartyTypes;
import party.lemons.questicle.quest.QuestListLoader;
import party.lemons.questicle.quest.QuestLoader;
import party.lemons.questicle.quest.display.frame.QuestFrameTypes;
import party.lemons.questicle.quest.goal.GoalTracker;
import party.lemons.questicle.quest.goal.GoalTypes;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.quest.QuestTypes;
import party.lemons.questicle.quest.reward.RewardTypes;
import party.lemons.questicle.quest.widget.WidgetTypes;
import party.lemons.questicle.resource.GlobalDataPackHandler;
import party.lemons.questicle.server.command.QuesticleCommand;

import java.nio.file.Path;

public class Questicle {

    public static final String MODID = "questicle";
    public static final LevelResource QUESTICLE_DIR = new LevelResource("questicle");   //Directory in the world folder to save data
    public static final ResourceLocation INVALID_RESOURCE = new ResourceLocation("unknown", "nope");

    public static void init()
	{
        GoalTypes.init();
        QuestIconTypes.init();
        QuestTypes.init();
        QuestFrameTypes.init();
        WidgetTypes.init();
        QuesticleNetwork.init();
        QuestPartyTypes.init();
        RewardTypes.init();

        PartyManager.init();
        GoalTracker.init();
        QuesticleCommand.init();

        GlobalDataPackHandler.init();

        //Loaders
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestListLoader());
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestLoader());

        //TODO: find somewhere better for this
        PlayerEvent.PLAYER_JOIN.register((p)->{
            if(p.connection != null) {
                new S2cSyncQuests().sendTo(p);
            }
        });
    }

    public static ResourceLocation id(String path){
        return new ResourceLocation(MODID, path);
    }

    /***
     * Gets the path to the world/questicle directory
     * @param server
     * @return questicle save directory
     */
    public static Path getQuesticleDir(MinecraftServer server)
    {
        return server.getWorldPath(Questicle.QUESTICLE_DIR);
    }
}
