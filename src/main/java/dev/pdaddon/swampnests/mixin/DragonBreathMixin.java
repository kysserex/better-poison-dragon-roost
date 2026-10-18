package dev.pdaddon.swampnests.mixin;

import dev.pdaddon.swampnests.AddonConfig;
import dev.pdaddon.swampnests.PoisonCloudSpawner;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * El chorro de aliento llama a applyDragonBreathAt(x, y, z, stage) en cada tick con el punto de impacto.
 * Aqui se deja (o se renueva) una nube de veneno en ese punto.
 */
@Mixin(targets = "com.menoxd.poisondragons.entity.PoisonDragonEntity", remap = false)
public abstract class DragonBreathMixin {

    @Inject(method = "applyDragonBreathAt", at = @At("HEAD"), remap = false, require = 1)
    private void pdSwampNests$leaveCloud(double x, double y, double z, int stage, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self.level().isClientSide || !AddonConfig.CLOUD_FROM_STREAM.get()) return;
        if (self.tickCount % AddonConfig.CLOUD_STREAM_INTERVAL_TICKS.get() != 0) return;
        PoisonCloudSpawner.spawn(self.level(), self, x, y, z);
    }
}
