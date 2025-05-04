package saderlane.pixeltrance.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;
import net.minecraft.entity.player.PlayerEntity;

import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.mixin.PlayerEntityMixin;
import saderlane.pixeltrance.api.TranceDataAccess;

public class TranceCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> create() {
        return net.minecraft.server.command.CommandManager.literal("trance")
                .requires(source -> source.hasPermissionLevel(0)) //allows anyone to run

                // /trance command
                // Will print the current trance level of the player running the command
                .executes(ctx -> {
                    PlayerEntity player = ctx.getSource().getPlayer();

                    // Get the TranceData attached to the player
                    TranceData trance = ((TranceDataAccess)player).getTranceData();

                    // Send the value to the user in chat
                    player.sendMessage(Text.literal("Trance: " + trance.getTrance()), true);
                    return 1;
                })


                // /trance clear
                // Directly set the trance value
                .then(CommandManager.literal("clear")
                    .executes(ctx -> {
                        PlayerEntity player = ctx.getSource().getPlayer();

                        // Set trance to 0
                        TranceData trance = ((TranceDataAccess)player).getTranceData();
                        trance.setTrance(0);

                        // Confirm to the user
                        player.sendMessage(Text.literal("Trance cleared"), true);
                        return 1;
                    })
                )


                // /trance set <value>
                // Directly set the trance value
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("value", FloatArgumentType.floatArg(0, 100))
                                .executes(ctx -> {
                                    PlayerEntity player = ctx.getSource().getPlayer();

                                    // Get the float argument from the command
                                    float value = FloatArgumentType.getFloat(ctx, "value");

                                    // Get the trance data and update it
                                    TranceData trance = ((TranceDataAccess)player).getTranceData();
                                    trance.setTrance(value);

                                    // Confirm to the user
                                    player.sendMessage(Text.literal("Set trance to " + value), true);
                                    return 1;
                                })
                        )
                )

                // /trance add <value>
                // Add trance to the meter
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("value", FloatArgumentType.floatArg())
                                .executes(ctx -> {
                                    PlayerEntity player = ctx.getSource().getPlayer();

                                    // Get the float argument from the command
                                    float amount = FloatArgumentType.getFloat(ctx, "value");

                                    // Get the TranceData attached to the player and add to it
                                    TranceData trance = ((TranceDataAccess)player).getTranceData();
                                    trance.addTrance(amount);

                                    player.sendMessage(Text.literal("Increased trance by " + amount), true);
                                    return 1;
                                })
                        )
                )

                // /trance remove <value>
                // Remove trance from the meter
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("value", FloatArgumentType.floatArg())
                                .executes(ctx -> {
                                    PlayerEntity player = ctx.getSource().getPlayer();

                                    // Get the float argument from the command
                                    float amount = FloatArgumentType.getFloat(ctx, "value");

                                    // Get the TranceData attached to the player and subtract from it
                                    TranceData trance = ((TranceDataAccess)player).getTranceData();
                                    trance.tranceDecay(amount);

                                    player.sendMessage(Text.literal("Reduced trance by " + amount), true);
                                    return 1;
                                })
                        )
                );
    }

}
