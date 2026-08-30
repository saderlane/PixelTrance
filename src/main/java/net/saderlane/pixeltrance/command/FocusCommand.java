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
import net.saderlane.pixeltrance.hypno.HypnoData;

import java.util.Collection;

public class FocusCommand {

    public FocusCommand(CommandDispatcher<CommandSourceStack> dispatcher) {

        // /focus
        dispatcher.register(Commands.literal("focus")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    // set
                    .then(Commands.literal("set")
                            // #
                            .then(Commands.argument("focus", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdSetFocus)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdSetFocusOther)
                                    )
                            )
                    )
                    .then(Commands.literal("add")
                            .then(Commands.argument("focus", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdAddFocus)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdAddFocusOther)
                                    )
                            )
                    )
                    .then(Commands.literal("sub")
                            .then(Commands.argument("focus", IntegerArgumentType.integer(0,100))
                                    .executes(this::cmdSubtractFocus)
                                    .then(Commands.argument("targets", EntityArgument.entities())
                                            .executes(this::cmdSubtractFocusOther)
                                    )
                            )
                    )
        );
    }

    private int cmdSetFocus(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        HypnoData.setFocus(player, focusValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Set " + player.getName().getString() + "'s focus to " + focusValue), true);
        return 1;
    }

    private int cmdSetFocusOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.setFocus(serverPlayer, focusValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Set " + serverPlayer.getName().getString() + "'s focus to " + focusValue), true);
            }
        });

        return targets.size();
    }

    private int cmdSubtractFocus(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        HypnoData.subFocus(player, focusValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Subtracted " + focusValue + " from " + player.getName().getString() + "'s focus."), true);

        return 1;
    }

    private int cmdSubtractFocusOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.subFocus(serverPlayer, focusValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Subtracted " + focusValue + " from " + serverPlayer.getName().getString() + "'s focus."), true);
            }
        });

        return targets.size();
    }

    private int cmdAddFocus(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        HypnoData.addFocus(player, focusValue);

        context.getSource().sendSuccess(() ->
                Component.literal("Added " + focusValue + " to " + player.getName().getString() + "'s focus."), true);
        return 1;
    }

    private int cmdAddFocusOther(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        int focusValue = IntegerArgumentType.getInteger(context, "focus");

        targets.forEach(entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                HypnoData.addFocus(serverPlayer, focusValue);
                context.getSource().sendSuccess(() ->
                        Component.literal("Added " + focusValue + " to " + serverPlayer.getName().getString() + "'s focus."), true);
            }
        });

        return targets.size();
    }


}
