package dev.pdaddon.swampnests;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public final class PoisonCloudSpawner {
    /** Etiqueta para reconocer las nubes creadas por este addon. */
    public static final String TAG = "pd_swamp_nests_cloud";

    private PoisonCloudSpawner() {}

    /** Crea una nube de veneno en el punto dado, o renueva una cercana si ya existe. */
    public static void spawn(Level level, Entity owner, double x, double y, double z) {
        if (level.isClientSide) return;

        // Si el punto cae dentro de un bloque solido, subir la nube sobre el.
        BlockPos bp = BlockPos.containing(x, y, z);
        if (!level.getBlockState(bp).getCollisionShape(level, bp).isEmpty()) {
            y = bp.getY() + 1.0D;
        }

        float radius = AddonConfig.CLOUD_RADIUS.get().floatValue();
        int duration = AddonConfig.CLOUD_DURATION_TICKS.get();

        double r = radius * 0.75D;
        AABB area = new AABB(x - r, y - 1.0D, z - r, x + r, y + 1.0D, z + r);
        List<AreaEffectCloud> nearby = level.getEntitiesOfClass(AreaEffectCloud.class, area,
                c -> c.getTags().contains(TAG));
        if (!nearby.isEmpty()) {
            for (AreaEffectCloud existing : nearby) {
                existing.setDuration(duration);
                existing.setRadius(radius);
            }
            return;
        }

        AreaEffectCloud cloud = new AreaEffectCloud(level, x, y, z);
        if (owner instanceof LivingEntity living) {
            cloud.setOwner(living);
        }
        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick(-(radius * 0.6F) / (float) duration);
        cloud.setFixedColor(0x6FA82E);
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON,
                AddonConfig.POISON_SECONDS.get() * 20,
                AddonConfig.POISON_AMPLIFIER.get()));
        cloud.addTag(TAG);
        level.addFreshEntity(cloud);
    }

    /** El dragon, su jinete, su dueno y otros dragones de veneno no sufren la nube. */
    public static boolean isProtected(AreaEffectCloud cloud, LivingEntity victim) {
        Entity owner = cloud.getOwner();
        if (owner == null) return false;
        if (victim == owner || owner.hasPassenger(victim)) return true;

        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(victim.getType());
        if (id != null && id.getNamespace().equals(PDSwampNests.POISON_DRAGONS)) return true;

        if (owner instanceof OwnableEntity ownable) {
            UUID ownerId = ownable.getOwnerUUID();
            if (ownerId != null) {
                if (ownerId.equals(victim.getUUID())) return true;
                if (victim instanceof OwnableEntity pet && ownerId.equals(pet.getOwnerUUID())) return true;
            }
        }
        return false;
    }
}
