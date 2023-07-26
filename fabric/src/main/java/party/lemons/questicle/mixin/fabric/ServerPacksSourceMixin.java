package party.lemons.questicle.mixin.fabric;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.questicle.resource.GlobalDataPackHandler;

import java.nio.file.Path;

@Mixin(ServerPacksSource.class)
public class ServerPacksSourceMixin
{
    @Inject(at = @At("RETURN"), method = "createPackRepository")
    private static void createPackRepository(Path path, CallbackInfoReturnable<PackRepository> cbi)
    {
        GlobalDataPackHandler.injectData(cbi.getReturnValue());
    }
}
