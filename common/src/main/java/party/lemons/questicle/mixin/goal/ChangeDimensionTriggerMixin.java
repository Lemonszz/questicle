package party.lemons.questicle.mixin.goal;

import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.questicle.quest.goal.GoalTracker;

@Mixin(ChangeDimensionTrigger.class)
public class ChangeDimensionTriggerMixin
{
    @Inject(at = @At("HEAD"), method = "trigger")
    private void trigger(ServerPlayer serverPlayer, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel, CallbackInfo cbi)
    {
        GoalTracker.onDimensionChanged(serverPlayer, newLevel.location());
    }
}
