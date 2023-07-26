package party.lemons.questicle.network;

import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Logger;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.party.QuestPartyTypes;
import party.lemons.questicle.party.storage.PartyStorage;

public class S2cSyncParty extends BaseS2CMessage {
    private static final Logger LOGGER = LogUtils.getLogger();

    private QuestParty party;

    public S2cSyncParty(QuestParty party)
    {
        this.party = party;
    }

    public S2cSyncParty(FriendlyByteBuf byteBuf)
    {

        CompoundTag tag = byteBuf.readNbt();
        party = QuestPartyTypes.CODEC.decode(NbtOps.INSTANCE, tag.get("Party")).getOrThrow(false, LOGGER::error).getFirst();
        PartyStorage storage = PartyStorage.CODEC.decode(NbtOps.INSTANCE, tag.get("Storage")).getOrThrow(false, LOGGER::error).getFirst();
        party.setStorage(storage);
    }

    @Override
    public MessageType getType() {
        return QuesticleNetwork.SYNC_PARTY;
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        Tag party = QuestPartyTypes.CODEC.encodeStart(NbtOps.INSTANCE, this.party).getOrThrow(false, LOGGER::error);
        Tag storage = PartyStorage.CODEC.encodeStart(NbtOps.INSTANCE, this.party.getStorage()).getOrThrow(false, LOGGER::error);

        CompoundTag tag = new CompoundTag();
        tag.put("Party", party);
        tag.put("Storage", storage);

        buf.writeNbt(tag);
    }

    @Override
    public void handle(NetworkManager.PacketContext context)
    {
        context.queue(()->{
            ClientStorage.clientParty = party;
        });
    }
}
