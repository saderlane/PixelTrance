package net.saderlane.pixeltrance;

import net.minecraft.world.item.CreativeModeTabs;
import net.saderlane.pixeltrance.block.ModBlocks;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.item.ModCreativeModeTabs;
import net.saderlane.pixeltrance.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PixelTrance.MOD_ID)
public class PixelTrance {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "pixeltrance";

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public PixelTrance(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (pixeltrance) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register Creative Mode Tab
        PTLog.info("[PixelTrance] Registering Creative Mode Tab");
        ModCreativeModeTabs.register(modEventBus);

        // Register Items
        PTLog.info("[PixelTrance] Registering Mod Items");
        ModItems.register(modEventBus);

        // Register Blocks
        PTLog.info("[PixelTrance] Registering Mod Blocks");
        ModBlocks.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // Add items to the Creative tabs
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        // This is no longer needed as there is a custom creative mode tab
        /* Vanilla Creative Mode Tabs
        // Add items to Tools and Utilities tab
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.POCKET_WATCH);
        }

        // Add items to Ingredients tab
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.SPIRALITE);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_SPIRALITE);
        }

        // Add blocks to Functional Blocks
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.SPIRALITE_BLOCK);
        }

        // Add blocks to Natural Blocks
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.SPIRALITE_ORE);
        }
         */

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
