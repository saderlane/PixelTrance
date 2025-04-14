package saderlane.pixeltrance.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.util.PTLog;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements TranceDataAccess {

    private final TranceData tranceData = new TranceData((LivingEntity) (Object) this);

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    // Accessor to retrieve this mob’s trance data
    @Override
    public TranceData getTranceData() {
        return tranceData;
    }

    // Save trance data to NBT when mob is saved (e.g. world save or chunk unload)
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveTrance(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("PixelTrance", tranceData.writeToNbt());
    }

    // Load trance data from NBT when mob is reloaded
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadTrance(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PixelTrance", NbtElement.COMPOUND_TYPE)) {
            tranceData.readFromNbt(nbt.getCompound("PixelTrance"));
            PTLog.debug("Loaded trance from NBT for mob: " + this.getName().getString());
        }
    }


}
