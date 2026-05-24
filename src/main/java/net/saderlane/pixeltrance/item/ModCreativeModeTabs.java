package net.saderlane.pixeltrance.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PixelTrance.MOD_ID);

    public static final Supplier<CreativeModeTab> PIXELTRANCE_TAB = CREATIVE_MODE_TAB.register("pixeltrance_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.POCKET_WATCH.get()))
                    .title(Component.translatable("creativetab.pixeltrance"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_SPIRALITE);
                        output.accept(ModItems.SPIRALITE);
                        output.accept(ModItems.POCKET_WATCH);
                        output.accept(ModBlocks.SPIRALITE_ORE);
                        output.accept(ModBlocks.SPIRALITE_BLOCK);


                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
