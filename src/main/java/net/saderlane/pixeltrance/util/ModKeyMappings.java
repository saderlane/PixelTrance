package net.saderlane.pixeltrance.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.saderlane.pixeltrance.PixelTrance;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = PixelTrance.MOD_ID, value = Dist.CLIENT)
public class ModKeyMappings {

    // Custom Keybind to check hypnosis status
    private static final KeyMapping PRESS_K =
            new KeyMapping("key.pixeltrance.k",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "key.categories.misc");

    // Custom keybind to check focus status
    private static final KeyMapping PRESS_L =
            new KeyMapping("key.pixeltrance.l",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_L,
                    "key.categories.misc");

    //public static final Lazy<KeyMapping> PRESS_L = Lazy.of(() -> KEY_MAPPING_L);

    @SubscribeEvent
    static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(PRESS_K);
        event.register(PRESS_L);
    }
}
