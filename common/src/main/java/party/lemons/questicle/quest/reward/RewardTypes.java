package party.lemons.questicle.quest.reward;

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
import party.lemons.questicle.quest.reward.impl.ItemReward;
import party.lemons.questicle.quest.reward.impl.XpReward;

import java.util.Optional;

public class RewardTypes
{
    public static final ResourceKey<Registry<RewardType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("reward_type"));
    public static final Registrar<RewardType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new RewardType<?>[0]).build();
    public static final DeferredRegister<RewardType<?>> REWARD_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<RewardType<ItemReward>> ITEM = REWARD_TYPES.register(Questicle.id("item"), ()-> new RewardType<>(ItemReward.CODEC));
    public static final RegistrySupplier<RewardType<XpReward>> XP = REWARD_TYPES.register(Questicle.id("xp"), ()-> new RewardType<>(XpReward.CODEC));

    public static void init(){
        REWARD_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initRewardRenderers); // :/

    }

    public static final Codec<Reward> CODEC = byNameCodec().dispatch(Reward::type, RewardType::codec);

    public static Codec<RewardType> byNameCodec() {
        Codec<RewardType> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<RewardType> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
