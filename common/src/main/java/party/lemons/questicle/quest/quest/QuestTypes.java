package party.lemons.questicle.quest.quest;

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
import party.lemons.questicle.quest.quest.impl.StandardQuest;

import java.util.Optional;

public class QuestTypes
{
    private static final ResourceLocation DEFAULT_TYPE = Questicle.id("standard");

    public static final ResourceKey<Registry<QuestType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("quest_type"));
    public static final Registrar<QuestType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new QuestType<?>[0]).build();
    public static final DeferredRegister<QuestType<?>> QUEST_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<QuestType<StandardQuest>> STANDARD = QUEST_TYPES.register(Questicle.id("standard"), ()-> new QuestType<>(StandardQuest.CODEC));

    public static void init(){
        QUEST_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initTooltips); // :/
    }

    public static final Codec<Quest> CODEC = byNameCodec().dispatch(Quest::type, QuestType::codec);

    public static Codec<QuestType<?>> byNameCodec() {
        Codec<QuestType<?>> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<QuestType<?>> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
