package party.lemons.questicle.party;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.QuesticleClient;
import party.lemons.questicle.quest.quest.Quest;
import party.lemons.questicle.quest.quest.impl.StandardQuest;

import java.util.Optional;

public class QuestPartyTypes
{
    public static final ResourceKey<Registry<QuestPartyType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("party_type"));
    public static final Registrar<QuestPartyType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new QuestPartyType<?>[0]).build();
    public static final DeferredRegister<QuestPartyType<?>> PARTY_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<QuestPartyType<QuesticleParty>> QUESTICLE = PARTY_TYPES.register(Questicle.id("questicle"), ()-> new QuestPartyType<>(QuesticleParty.CODEC));

    public static void init(){
        PARTY_TYPES.register();
    }

    public static final Codec<QuestParty> CODEC = byNameCodec().dispatch(QuestParty::type, QuestPartyType::codec);

    public static Codec<QuestPartyType<?>> byNameCodec() {
        Codec<QuestPartyType<?>> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<QuestPartyType<?>> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
