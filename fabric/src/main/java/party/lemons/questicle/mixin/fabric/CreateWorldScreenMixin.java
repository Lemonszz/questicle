package party.lemons.questicle.mixin.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.repository.PackRepository;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.questicle.resource.GlobalDataPackHandler;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createDefaultLoadConfig(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;)Lnet/minecraft/server/WorldLoader$InitConfig;"),
            method = "openFresh", locals = LocalCapture.CAPTURE_FAILHARD)
    private static void openFresh(Minecraft minecraft, @Nullable Screen screen, CallbackInfo cbi, PackRepository packRepository) {
        GlobalDataPackHandler.injectData(packRepository);
    }
}

