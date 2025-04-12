package saderlane.pixeltrance;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import saderlane.pixeltrance.command.TranceCommand;
import saderlane.pixeltrance.item.ModItems;
import saderlane.pixeltrance.logic.FocusSourceHandler;
import saderlane.pixeltrance.logic.HypnoticSourceProcessor;
import saderlane.pixeltrance.logic.TranceSourceHandler;
import saderlane.pixeltrance.server.item.PocketWatchServerEffects;
import saderlane.pixeltrance.sound.TranceSounds;


public class PixelTrance implements ModInitializer {
	public static final String MOD_ID = "pixeltrance";

	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static final TranceSourceHandler TRANCE_SOURCE_HANDLER = new TranceSourceHandler();
	public static final FocusSourceHandler FOCUS_SOURCE_HANDLER = new FocusSourceHandler();



	@Override
	public void onInitialize() {
		LOGGER.info("PixelTrance mod loaded!");

		ModItems.init();
		TranceSounds.register();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var world : server.getWorlds()) {
				for (var entity : world.getPlayers()) {
					TRANCE_SOURCE_HANDLER.tickForEntity(entity);
					FOCUS_SOURCE_HANDLER.tickForEntity(entity);
				}
			}

			// Centralized trance/focus processing for all hypnotic sources
			HypnoticSourceProcessor.tick(server);
		});



		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(TranceCommand.create());
		});

	}
}