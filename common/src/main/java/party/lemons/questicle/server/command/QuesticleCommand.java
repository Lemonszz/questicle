package party.lemons.questicle.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import party.lemons.questicle.party.PartyManager;

public class QuesticleCommand
{
    public static void init()
    {
        CommandRegistrationEvent.EVENT.register(
                (dispatcher, registry, selection) -> dispatcher.register(
                        Commands.literal("qparty")
                                    .then(
                                            Commands.literal("invite")
                                                    .then(
                                                            Commands.argument("target", EntityArgument.player())
                                                                    .executes((ctx)->QuesticleCommand.invitePlayer(EntityArgument.getPlayer(ctx, "target"), ctx))
                                                    )
                                    )
                                    .then(
                                            Commands.literal("cancel_invite")
                                                    .then(
                                                            Commands.argument("target", EntityArgument.player())
                                                                    .executes((ctx)->QuesticleCommand.cancelInvite(EntityArgument.getPlayer(ctx, "target"), ctx))
                                                    ))
                                    .then(
                                            Commands.literal("accept").executes(QuesticleCommand::acceptInvite)
                                            )
                                   // .then("leave")
                                  //  .then("list")
                )
        );
    }

    private static int acceptInvite(CommandContext<CommandSourceStack> ctx)
    {
        if(ctx.getSource().getPlayer() == null)
            return 0;

        if(PartyManager.acceptInvitation(ctx.getSource().getServer(), ctx.getSource().getPlayer())) {
            ctx.getSource().sendSuccess(()->Component.translatable("questicle.text.accept_invite.success"), false);
            return 1;
        }

        ctx.getSource().sendFailure(Component.translatable("questicle.text.accept_invite.fail"));

        return 0;
    }

    private static int cancelInvite(ServerPlayer player, CommandContext<CommandSourceStack> ctx)
    {
        ServerPlayer src;
        if((src = ctx.getSource().getPlayer()) != null)
        {
            if(PartyManager.cancelInvitePlayer(src.getServer(), src.getUUID(), player)) {
                ctx.getSource().sendSuccess(()->Component.translatable("questicle.text.cancel_invite.success", player.getDisplayName()), false);
                return 1;
            }
        }
        ctx.getSource().sendFailure(Component.translatable("questicle.text.cancel_invite.fail", player.getDisplayName()));
        return 0;
    }

    private static int invitePlayer(ServerPlayer player, CommandContext<CommandSourceStack> ctx)
    {
        ServerPlayer src;
        if((src = ctx.getSource().getPlayer()) != null)
        {
            if(PartyManager.invitePlayer(src.getServer(), src, player)) {
                ctx.getSource().sendSuccess(()->Component.translatable("questicle.text.invite.success", player.getDisplayName()), false);
                return 1;
            }
        }

        ctx.getSource().sendFailure(Component.translatable("questicle.text.invite.fail", player.getDisplayName()));
        return 0;
    }
}
