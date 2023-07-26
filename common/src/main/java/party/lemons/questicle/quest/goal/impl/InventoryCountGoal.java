package party.lemons.questicle.quest.goal.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface InventoryCountGoal
{
    default int getCount(List<ServerPlayer> players)
    {
        int count = 0;
        for(Player pl : players)
        {
            for(NonNullList<ItemStack> list : pl.getInventory().compartments)
            {
                for(ItemStack stack : list)
                {
                    if(itemMatches(stack)) {
                        count += stack.getCount();
                        if(count >= getRequiredCount())
                            return getRequiredCount();
                    }

                }
            }
        }
        return count;
    }

    int getRequiredCount();
    boolean itemMatches(ItemStack stack);
}
