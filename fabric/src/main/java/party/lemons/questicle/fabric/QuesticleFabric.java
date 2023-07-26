package party.lemons.questicle.fabric;

import net.fabricmc.api.ModInitializer;
import party.lemons.questicle.Questicle;

public class QuesticleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Questicle.init();
    }
}
