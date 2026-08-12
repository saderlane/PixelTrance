package net.saderlane.pixeltrance.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {

    // Custom Keybind to check hypnosis status
    private static final KeyMapping KEY_MAPPING_K =
            new KeyMapping("key.pixeltrance.k",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "key.categories.misc");
    public static final Lazy<KeyMapping> PRESS_K = Lazy.of(() -> KEY_MAPPING_K);

    // Custom keybind to check focus status
    private static final KeyMapping KEY_MAPPING_L =
            new KeyMapping("key.pixeltrance.l",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_L,
                    "key.categories.misc");

    public static final Lazy<KeyMapping> PRESS_L = Lazy.of(() -> KEY_MAPPING_L);


    public static void register() {

    }
}
