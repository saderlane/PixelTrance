package saderlane.pixeltrance;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.command.TranceCommand;
import saderlane.pixeltrance.item.ModItems;
import saderlane.pixeltrance.logic.FocusHandler;
import saderlane.pixeltrance.logic.MobInducerHandler;
import saderlane.pixeltrance.logic.TranceDecayHandler;
import saderlane.pixeltrance.logic.TranceHandler;
import saderlane.pixeltrance.registry.InducerRegistry;
import saderlane.pixeltrance.registry.MobInducerRegistry;
import saderlane.pixeltrance.server.item.PocketWatchServerEffects;
import saderlane.pixeltrance.sound.TranceSounds;


public class PixelTrance implements ModInitializer {
	public static final String MOD_ID = "pixeltrance";

	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		LOGGER.info("PixelTrance mod loaded!");


		// === Registration ===

		ModItems.init(); // Register items
		TranceSounds.register(); // Register sounds
		MobInducerRegistry.register(); // Register inducer mobs

		// === Server Tick Logic ===
		// Tick hook for trance/focus sources and item effects
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var world : server.getWorlds()) {

				// Clear inducer list
				InducerRegistry.clear();

				// Add held item inducers
				InducerRegistry.scanHeldItems(world.getPlayers());

				// Add mob-based inducers (pink sheep, etc.)
				MobInducerHandler.scan(world); // already a ServerWorld

				// Tick all subjects
				for (var subject : world.iterateEntities()) {
					if (subject instanceof LivingEntity living && subject instanceof TranceDataAccess) {
						TranceDecayHandler.tickForSubject(living);
						FocusHandler.tickForSubject(world, living);
						TranceHandler.tickForSubject(world, living);
					}
				}
			}

			PocketWatchServerEffects.tick(server);
		});


		// Register /trance debug command
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(TranceCommand.create());
		});

	}
}