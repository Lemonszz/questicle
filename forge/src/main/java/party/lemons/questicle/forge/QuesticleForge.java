package party.lemons.questicle.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.QuesticleClient;

@Mod(Questicle.MODID)
public class QuesticleForge {
    public QuesticleForge() {
        EventBuses.registerModEventBus(Questicle.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Questicle.init();

        EnvExecutor.runInEnv(Dist.CLIENT, ()-> QuesticleClient::init);
    }
}
