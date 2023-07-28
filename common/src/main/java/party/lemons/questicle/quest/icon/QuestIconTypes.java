package party.lemons.questicle.quest.icon;

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
import party.lemons.questicle.quest.icon.impl.BlankQuestIcon;
import party.lemons.questicle.quest.icon.impl.ItemQuestIcon;
import party.lemons.questicle.quest.icon.impl.MobQuestIcon;
import party.lemons.questicle.quest.icon.impl.TextureQuestIcon;

import java.util.Optional;

public class QuestIconTypes
{
    public static final ResourceKey<Registry<QuestIconType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("quest_icon_type"));
    public static final Registrar<QuestIconType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new QuestIconType<?>[0]).build();
    public static final DeferredRegister<QuestIconType<?>> ICON_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<QuestIconType<BlankQuestIcon>> BLANK = ICON_TYPES.register(Questicle.id("blank"), ()-> new QuestIconType<>(BlankQuestIcon.CODEC));
    public static final RegistrySupplier<QuestIconType<ItemQuestIcon>> ITEM = ICON_TYPES.register(Questicle.id("item"), ()-> new QuestIconType<>(ItemQuestIcon.CODEC));
    public static final RegistrySupplier<QuestIconType<MobQuestIcon>> MOB = ICON_TYPES.register(Questicle.id("mob"), ()-> new QuestIconType<>(MobQuestIcon.CODEC));
    public static final RegistrySupplier<QuestIconType<TextureQuestIcon>> TEXTURE = ICON_TYPES.register(Questicle.id("texture"), ()-> new QuestIconType<>(TextureQuestIcon.CODEC));

    public static void init(){
        ICON_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initIconRenderers); // :/

    }

    public static final Codec<QuestIcon> CODEC = byNameCodec().dispatch(QuestIcon::type, QuestIconType::codec);

    public static Codec<QuestIconType> byNameCodec() {
        Codec<QuestIconType> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<QuestIconType> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
