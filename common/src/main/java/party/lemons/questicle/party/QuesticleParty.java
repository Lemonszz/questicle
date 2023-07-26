package party.lemons.questicle.party;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import party.lemons.questicle.party.storage.PartyStorage;
import party.lemons.questicle.quest.quest.Quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class QuesticleParty implements QuestParty
{
    public static Codec<QuesticleParty> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.STRING_CODEC.fieldOf("party_id").forGetter(QuesticleParty::partyID),
                    UUIDUtil.STRING_CODEC.listOf().fieldOf("members").forGetter(QuesticleParty::partyMemberList),
                    UUIDUtil.STRING_CODEC.fieldOf("owner").forGetter(QuesticleParty::partyOwner)
            ).apply(instance, QuesticleParty::new));

    private final Set<UUID> members;
    private UUID owner;
    private final UUID partyID;

    private PartyStorage storage;

    public QuesticleParty(UUID partyID, List<UUID> members, UUID owner)
    {
        this.members = new HashSet<>(members);
        this.owner = owner;
        this.partyID = partyID;
    }

    public QuesticleParty(MinecraftServer server, UUID partyID, UUID owner)
    {
        this(partyID, List.of(owner), owner);
        setStorage(PartyStorage.load(this, server));
    }

    @Override
    public void setStorage(PartyStorage storage)
    {
        this.storage = storage;
    }

    @Override
    public Set<UUID> partyMembers() {
        return members;
    }

    @Override
    public UUID partyOwner() {
        return owner;
    }

    @Override
    public UUID partyID() {
        return partyID;
    }

    @Override
    public PartyStorage getStorage() {
        return storage;
    }

    @Override
    public void setPartyOwner(UUID newOwner) {
        this.owner = newOwner;
    }

    @Override
    public QuestPartyType<?> type() {
        return QuestPartyTypes.QUESTICLE.get();
    }
}