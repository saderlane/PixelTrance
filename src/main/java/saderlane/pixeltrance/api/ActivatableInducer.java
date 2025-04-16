package saderlane.pixeltrance.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ActivatableInducer {

    // Is the item currently active
    boolean isActivated(ItemStack stack);

    // Toggles activate state server-side and returns new state
    boolean toggleActivation(ItemStack stack, LivingEntity user);

}
