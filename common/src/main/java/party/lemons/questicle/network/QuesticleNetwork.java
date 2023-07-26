package party.lemons.questicle.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import party.lemons.questicle.Questicle;

public class QuesticleNetwork
{
    public static final SimpleNetworkManager NET = SimpleNetworkManager.create(Questicle.MODID);
    public static final MessageType SYNC_QUESTS = NET.registerS2C("sync_quests", S2cSyncQuests::new);
    public static final MessageType SYNC_PARTY = NET.registerS2C("sync_party", S2cSyncParty::new);

    public static final MessageType CL_CLAIM_REQUEST = NET.registerC2S("claim_quests", C2sSendClaimRequest::new);


    public static void init()
    {
        //NOFU
    }
}
