package saderlane.pixeltrance;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import saderlane.pixeltrance.command.TranceCommand;
import saderlane.pixeltrance.item.ModItems;
import saderlane.pixeltrance.server.item.PocketWatchServerEffects;
import saderlane.pixeltrance.sound.TranceSounds;


public class PixelTrance implements ModInitializer {
	public static final String MOD_ID = "pixeltrance";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("PixelTrance mod loaded!");

		ModItems.init();
		TranceSounds.register();
		ServerTickEvents.END_SERVER_TICK.register(PocketWatchServerEffects::tick);


		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(TranceCommand.create());
		});

	}
}