package saderlane.pixeltrance.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import saderlane.pixeltrance.PixelTrance;

public class TranceSounds {
    public static final Identifier TRANCE_TRIGGER_ID = new Identifier(PixelTrance.MOD_ID, "trance_trigger");
    public static SoundEvent TRANCE_TRIGGER = SoundEvent.of(TRANCE_TRIGGER_ID);

    public static final Identifier WATCH_TICK_ID = new Identifier(PixelTrance.MOD_ID, "watch_tick_loop");
    public static SoundEvent WATCH_TICK = SoundEvent.of(WATCH_TICK_ID);


    public static void register() {
        Registry.register(Registries.SOUND_EVENT, TRANCE_TRIGGER_ID, TRANCE_TRIGGER);
        Registry.register(Registries.SOUND_EVENT, WATCH_TICK_ID, WATCH_TICK);
    }
}
