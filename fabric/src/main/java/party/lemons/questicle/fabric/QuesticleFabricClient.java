package party.lemons.questicle.fabric;

import net.fabricmc.api.ClientModInitializer;
import party.lemons.questicle.QuesticleClient;

public class QuesticleFabricClient  implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        QuesticleClient.init();
    }
}
