package party.lemons.questicle.mixin.client;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.questicle.client.QAtlases;
import party.lemons.questicle.client.texture.TextureData;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin
{
    //TODO: it's (currently) impossible to register a custom atlas for some reason we just hack it :)

    @Shadow @Final @Mutable private static Map<ResourceLocation, ResourceLocation> VANILLA_ATLASES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onInit(CallbackInfo cbi)
    {
    //    QAtlases.init();

       // VANILLA_ATLASES = new HashMap<>(VANILLA_ATLASES);
       // VANILLA_ATLASES.put(TextureData.FRAMES_SHEET, TextureData.FRAME_ATLAS);
       // VANILLA_ATLASES.put(TextureData.ELEMENTS_SHEET, TextureData.ELEMENTS_ATLAS);
    }

}
