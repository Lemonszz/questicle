package party.lemons.questicle.quest.widget;

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
import party.lemons.questicle.quest.widget.impl.NineSliceTextureWidget;
import party.lemons.questicle.quest.widget.impl.RepeatingTextureWidget;

import java.util.Optional;

public class WidgetTypes
{
    public static final ResourceKey<Registry<WidgetType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Questicle.id("widget"));
    public static final Registrar<WidgetType<?>> REGISTRY = RegistrarManager.get(Questicle.MODID).builder(REGISTRY_KEY.location(), new WidgetType<?>[0]).build();
    public static final DeferredRegister<WidgetType<?>> WIDGET_TYPES = DeferredRegister.create(Questicle.MODID, REGISTRY_KEY);

    public static final RegistrySupplier<WidgetType<RepeatingTextureWidget>> REPEATING = WIDGET_TYPES.register(Questicle.id("repeating"), ()-> new WidgetType<>(RepeatingTextureWidget.CODEC));
    public static final RegistrySupplier<WidgetType<NineSliceTextureWidget>> NINE_SLICE = WIDGET_TYPES.register(Questicle.id("nine_slice"), ()-> new WidgetType<>(NineSliceTextureWidget.CODEC));

    public static void init(){
        WIDGET_TYPES.register();

        EnvExecutor.runInEnv(Env.CLIENT, ()-> QuesticleClient::initWidgetRenderers); // :/

    }

    public static final Codec<Widget> CODEC = byNameCodec().dispatch(Widget::type, WidgetType::codec);

    public static Codec<WidgetType> byNameCodec() {
        Codec<WidgetType> codec = ResourceLocation.CODEC
                .flatXmap(
                        resourceLocation -> Optional.ofNullable(REGISTRY.get(resourceLocation))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + REGISTRY.key() + ": " + resourceLocation)),
                        object -> REGISTRY.getKey(object)
                                .map(ResourceKey::location)
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + REGISTRY.key() + ":" + object))
                );
        Codec<WidgetType> codec2 = ExtraCodecs.idResolverCodec(object -> REGISTRY.getKey(object).isPresent() ? REGISTRY.getRawId(object) : -1, REGISTRY::byRawId, -1);
        return ExtraCodecs.overrideLifecycle(ExtraCodecs.orCompressed(codec, codec2), (e)-> Lifecycle.stable(),  (e)->Lifecycle.stable());
    }
}
