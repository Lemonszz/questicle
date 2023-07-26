package party.lemons.questicle.quest.display.frame;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProviderType;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.QuesticleClient;
import party.lemons.questicle.quest.display.frame.impl.PresetQuestFrame;
import party.lemons.questicle.quest.display.frame.impl.TextureQuestFrame;

import java.util.Optional;

public class QuestFrameTypes
{
    public static final ResourceKey<Registry<QuestFrameType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("frame_type"));
    public static final Registrar<QuestFrameType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new QuestFrameType<?>[0]).build();
    public static final DeferredRegister<QuestFrameType<?>> FRAME_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);
    public static final RegistrySupplier<QuestFrameType<TextureQuestFrame>> TEXTURE = FRAME_TYPES.register(Questicle.id("texture"), ()-> new QuestFrameType<>(TextureQuestFrame.CODEC));
    public static final RegistrySupplier<QuestFrameType<PresetQuestFrame>> PRESET = FRAME_TYPES.register(Questicle.id("preset"), ()-> new QuestFrameType<>(PresetQuestFrame.CODEC));

    public static void init(){
        FRAME_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initFrameRenderers); // :/
    }

    public static final Codec<QuestFrame> CODEC = byNameCodec().dispatch(QuestFrame::type, QuestFrameType::codec);

    public static final Codec<QuestFrame> PRESET_CODEC = Codec.either(
                    ResourceLocation.CODEC,
                    CODEC
            )
            .xmap(
                    either -> either.map(PresetQuestFrame::new, location -> location),
                    Either::right
            );
    public static Codec<QuestFrameType> byNameCodec() {
        Codec<QuestFrameType> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<QuestFrameType> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
