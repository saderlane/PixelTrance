package saderlane.pixeltrance.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import saderlane.pixeltrance.network.SuggestPromptS2CPacket;
import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.util.PTLog;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SuggestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("suggest")
                .then(CommandManager.argument("target", StringArgumentType.word())
                        .suggests(SUGGEST_PLAYER_NAMES)
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(SuggestCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        String targetName = StringArgumentType.getString(context, "target");
        String message = StringArgumentType.getString(context, "message");
        ServerPlayerEntity targetPlayer = context.getSource().getServer().getPlayerManager().getPlayer(targetName);

        if (targetPlayer == null) {
            context.getSource().sendError(Text.literal("No player found with name: " + targetName));
            return 0;
        }

        new SuggestPromptS2CPacket(message).send(targetPlayer);
        context.getSource().sendFeedback(() ->
                Text.literal("Sent suggestion to " + targetPlayer.getEntityName()), false);
        return 1;
    }

    private static final SuggestionProvider<ServerCommandSource> SUGGEST_PLAYER_NAMES = (context, builder) -> {
        for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
            builder.suggest(player.getEntityName());
        }
        return builder.buildFuture();
    };
}
