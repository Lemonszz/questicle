package party.lemons.questicle.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import party.lemons.questicle.party.PartyManager;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.quest.Quest;

public class C2sSendClaimRequest extends BaseC2SMessage
{
    private static final ResourceLocation ALL_LOCATION = new ResourceLocation("questicle", "ca");

    private final Quest quest;
    private final boolean readFailed;

    public C2sSendClaimRequest(Quest quest)
    {
        this.quest = quest;
        readFailed = false;
    }

    public C2sSendClaimRequest() //Request claim all
    {
        this.quest = null;
        readFailed = false;
    }

    public C2sSendClaimRequest(FriendlyByteBuf buf)
    {
        ResourceLocation location = buf.readResourceLocation();
        if(location.equals(ALL_LOCATION)) {
            quest = null;
            readFailed = false;
        }
        else {
            quest = Quests.quests.getOrDefault(location, null);
            readFailed = quest == null;
        }
    }

    @Override
    public MessageType getType() {
        return QuesticleNetwork.CL_CLAIM_REQUEST;
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        buf.writeResourceLocation(quest == null ? ALL_LOCATION : quest.id());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            if(readFailed)
                return;

            ServerPlayer pl = (ServerPlayer) context.getPlayer();
            QuestParty party = PartyManager.getPlayerParty(pl);
            PartyStorage partyStorage = party.getStorage();

            if(quest == null)
            {
                partyStorage.requestAllRewardClaims(pl);
            }
            else {
                partyStorage.requestRewardClaim(pl, quest);
            }

            new S2cSyncParty(party).sendTo(pl);
        });
    }
}
