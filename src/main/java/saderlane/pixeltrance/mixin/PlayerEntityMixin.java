package saderlane.pixeltrance.mixin;
import saderlane.pixeltrance.util.PTLog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.api.TranceDataAccess;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements TranceDataAccess {


    // Adds tranceData field to every unique player
    @Unique
    private final TranceData tranceData = new TranceData((PlayerEntity)(Object)this);

    // Method to let other classes access trance data attached to player
    @Override
    public TranceData getTranceData() {
        return tranceData;
    }

    // Inject into save method
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveTrance(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("PixelTrance", tranceData.writeToNbt());
    }

    // Inject into load method
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadTrance(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PixelTrance")) {
            PTLog.info("[PixelTrance] Loaded trance from NBT");
            tranceData.readFromNbt(nbt.getCompound("PixelTrance"));
        }
    }

}
