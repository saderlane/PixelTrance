package saderlane.pixeltrance.logic;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import saderlane.pixeltrance.api.MobInducerWrapper;
import saderlane.pixeltrance.registry.InducerRegistry;

// Scans all mobs in a world for qualifying inducers
public class MobInducerHandler {

    public static void scan(ServerWorld world) {
        for (LivingEntity mob : world.getEntitiesByType(EntityType.SHEEP, entity -> true)) {
            if (mob instanceof SheepEntity sheep && sheep.getColor() == DyeColor.PINK) {
                InducerRegistry.addMobInducer(new MobInducerWrapper(sheep));
            }
        }

    }

}
