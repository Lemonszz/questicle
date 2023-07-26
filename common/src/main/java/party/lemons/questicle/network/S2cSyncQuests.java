package party.lemons.questicle.network;

import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import party.lemons.questicle.client.ClientStorage;
import party.lemons.questicle.quest.QuestList;
import party.lemons.questicle.quest.QuestLists;
import party.lemons.questicle.quest.Quests;
import party.lemons.questicle.quest.quest.Quest;

import java.util.Map;

public class S2cSyncQuests extends BaseS2CMessage {
    private static final Logger LOGGER = LogUtils.getLogger();

    public S2cSyncQuests()
    {

    }

    public S2cSyncQuests(FriendlyByteBuf byteBuf)
    {

        CompoundTag tag = byteBuf.readNbt();

        Map<ResourceLocation, Quest> quests = Quests.CODEC.decode(NbtOps.INSTANCE, tag.get("Quests")).getOrThrow(false, LOGGER::error).getFirst();
        Map<ResourceLocation, QuestList> lists = QuestLists.CODEC.decode(NbtOps.INSTANCE, tag.get("Lists")).getOrThrow(false, LOGGER::error).getFirst();

        //TODO: move these to handle.
        ClientStorage.clientQuests = quests;
        ClientStorage.setClientQuestLists(lists);
    }

    @Override
    public MessageType getType() {
        return QuesticleNetwork.SYNC_QUESTS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        Tag quests = Quests.CODEC.encodeStart(NbtOps.INSTANCE, Quests.quests).getOrThrow(false, LOGGER::error);
        Tag lists = QuestLists.CODEC.encodeStart(NbtOps.INSTANCE, QuestLists.lists).getOrThrow(false, LOGGER::error);

        CompoundTag tag = new CompoundTag();
        tag.put("Quests", quests);
        tag.put("Lists", lists);

        buf.writeNbt(tag);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {

    }
}
