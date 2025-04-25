package saderlane.pixeltrance.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.data.TranceData;

public class FocusCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> create() {
        return net.minecraft.server.command.CommandManager.literal("focus")
                .requires(source -> source.hasPermissionLevel(0)) //allows anyone to run

                // /trance command
                // Will print the current trance level of the player running the command
                .executes(ctx -> {
                    PlayerEntity player = ctx.getSource().getPlayer();

                    // Get the TranceData attached to the player
                    TranceData focus = ((TranceDataAccess)player).getTranceData();

                    // Send the value to the user in chat
                    ctx.getSource().sendFeedback(() -> Text.literal("Focus: " + focus.getFocus()), false);
                    return 1;
                });
    }

}
