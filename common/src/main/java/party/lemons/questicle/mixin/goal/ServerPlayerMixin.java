package party.lemons.questicle.mixin.goal;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.questicle.quest.goal.GoalTracker;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player
{
    private ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(at = @At("TAIL"), method = "doTick")
    private void onDoTick(CallbackInfo cbi)
    {
        if(tickCount % 40 == 0)            //TODO: Config how often this checks
        {
            GoalTracker.checkLocation((ServerPlayer)(Object)this);

        }
    }
}
