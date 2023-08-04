package party.lemons.questicle.mixin.client;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MobEffectTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.questicle.client.QAtlases;

@Mixin(MobEffectTextureManager.class)
public class MobEffectTextureManagerMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(TextureManager textureManager, CallbackInfo cbi)
    {
        QAtlases.init();
    }
}
