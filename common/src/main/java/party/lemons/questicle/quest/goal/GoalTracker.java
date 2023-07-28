package party.lemons.questicle.quest.goal;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import party.lemons.questicle.party.PartyManager;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.goal.impl.LocationGoal;

public class GoalTracker
{
    public static void init()
    {
        EntityEvent.LIVING_DEATH.register(GoalTracker::onLivingDeath);
    }

    private static EventResult onLivingDeath(LivingEntity entity, DamageSource damageSource)
    {
        if(damageSource.getEntity() instanceof ServerPlayer player)
        {
            PartyManager.getPlayerParty(player).onLivingDeath(player, entity);
        }
        return EventResult.pass();
    }

    public static void onInventoryChanged(ServerPlayer player, ItemStack stack)
    {
        QuestParty party = PartyManager.getPlayerParty(player);
        if(party != null)
            party.onInventoryChanged(player, stack);
    }

    public static void onDimensionChanged(ServerPlayer player, ResourceLocation location)
    {
        QuestParty party = PartyManager.getPlayerParty(player);
        if(party != null)
            party.onDimensionChanged(player, location);
    }

    public static void checkLocation(ServerPlayer player)
    {
        QuestParty party = PartyManager.getPlayerParty(player);
        if(party != null) {
            LocationGoal.LocationContext ctx = LocationGoal.LocationContext.create(player);

            party.checkLocation(ctx);
        }
    }
}
