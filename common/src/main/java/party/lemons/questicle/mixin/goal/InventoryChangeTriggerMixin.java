package party.lemons.questicle.mixin.goal;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.questicle.quest.goal.GoalTracker;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin
{
    @Inject(at = @At("HEAD"), method = "trigger")
    private void trigger(ServerPlayer serverPlayer, Inventory inventory, ItemStack itemStack, CallbackInfo cbi)
    {
        GoalTracker.onInventoryChanged(serverPlayer, itemStack);
    }
}
