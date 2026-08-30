package net.saderlane.pixeltrance.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.saderlane.pixeltrance.hypno.HypnoData;

import java.util.Collection;
import java.util.List;

public class TranceCommand {

    public TranceCommand(CommandDispatcher<CommandSourceStack> dispatcher) {

        // /trance
        dispatcher.register(Commands.literal("trance")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    // set
                    .then(Commands.literal("set")
                            // #
                            .then(Commands.argument("trance", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdSetTrance)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdSetTranceOther)
                                    )
                            )
                    )
                    .then(Commands.literal("add")
                            .then(Commands.argument("trance", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdAddTrance)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdAddTranceOther)
                                    )
                            )
                    )
                    .then(Commands.literal("sub")
                            .then(Commands.argument("trance", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdSubtractTrance)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdSubtractTranceOther)
                                    )
                            )
                    )
        );
    }

    private int cmdSetTrance(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        HypnoData.setTrance(player, tranceValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Set " + player.getName().getString() + "'s trance to " + tranceValue), true);
        return 1;
    }

    private int cmdSetTranceOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.setTrance(serverPlayer, tranceValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Set " + serverPlayer.getName().getString() + "'s trance to " + tranceValue), true);
            }
        });

        return targets.size();
    }

    private int cmdSubtractTrance(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        HypnoData.subTrance(player, tranceValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Subtracted " + tranceValue + " from " + player.getName().getString() + "'s trance."), true);

        return 1;
    }

    private int cmdSubtractTranceOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.subTrance(serverPlayer, tranceValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Subtracted " + tranceValue + " from " + serverPlayer.getName().getString() + "'s trance."), true);
            }
        });

        return targets.size();
    }

    private int cmdAddTrance(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        HypnoData.addTrance(player, tranceValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Added " + tranceValue + " to " + player.getName().getString() + "'s trance."), true);
        return 1;
    }

    private int cmdAddTranceOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int tranceValue = IntegerArgumentType.getInteger(context, "trance");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.addTrance(serverPlayer, tranceValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Added " + tranceValue + " to " + serverPlayer.getName().getString() + "'s trance."), true);
            }
        });

        return targets.size();
    }


}
