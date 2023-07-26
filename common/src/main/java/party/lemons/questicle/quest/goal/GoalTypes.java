package party.lemons.questicle.quest.goal;

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
import party.lemons.questicle.quest.goal.impl.ChangeDimensionGoal;
import party.lemons.questicle.quest.goal.impl.CollectGoal;
import party.lemons.questicle.quest.goal.impl.KillMobGoal;

import java.util.Optional;

public class GoalTypes
{
    public static final ResourceKey<Registry<GoalType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("goal_type"));
    public static final Registrar<GoalType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new GoalType<?>[0]).build();
    public static final DeferredRegister<GoalType<?>> GOAL_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<GoalType<CollectGoal>> COLLECT_GOAL = GOAL_TYPES.register(Questicle.id("collect"), ()-> new GoalType<>(CollectGoal.CODEC));
    public static final RegistrySupplier<GoalType<KillMobGoal>> KILL_MOB = GOAL_TYPES.register(Questicle.id("kill_mob"), ()-> new GoalType<>(KillMobGoal.CODEC));
    public static final RegistrySupplier<GoalType<ChangeDimensionGoal>> CHANGE_DIMENSION = GOAL_TYPES.register(Questicle.id("change_dimension"), ()-> new GoalType<>(ChangeDimensionGoal.CODEC));

    public static void init(){
        GOAL_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initGoalDisplays); // :/

    }

    public static final Codec<Goal> CODEC = byNameCodec().dispatch(Goal::type, GoalType::codec);

    public static Codec<GoalType<?>> byNameCodec() {
        Codec<GoalType<?>> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<GoalType<?>> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
