package saderlane.pixeltrance.client.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import saderlane.pixeltrance.client.data.ClientTranceState;

public class ScreenPullHandler {

    // Called every client tick
    // Applies rotation pull effect based on focus level and current hypnotic target
    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        float focus = ClientTranceState.getFocus();
        LivingEntity target = ClientTranceState.getHypnoticTarget();


        if (focus < 30f || target == null) return;

        var player = client.player;

        double dx = target.getX() - player.getX();
        double dy = target.getEyeY() - player.getEyeY();
        double dz = target.getZ() - player.getZ();

        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 0.001) return;

        // Pull strength scales from 0 at 30 focus to 1 at 100 focus
        double pullStrength = MathHelper.clamp((focus - 30f) / 70f, 0f, 1f);

        // Compute yaw and pitch angles toward the target
        double yawToTarget = Math.toDegrees(Math.atan2(-dx, dz));
        double pitchToTarget = Math.toDegrees(-Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

        // Compute difference between current and target angles
        double yawDiff = MathHelper.wrapDegrees(yawToTarget - player.getYaw());
        double pitchDiff = pitchToTarget - player.getPitch();

        // Apply rotation gradually, scaled by focus level
        player.setYaw((float)(player.getYaw() + yawDiff * 0.05 * pullStrength));
        player.setPitch((float)(player.getPitch() + pitchDiff * 0.05 * pullStrength));
    }
}