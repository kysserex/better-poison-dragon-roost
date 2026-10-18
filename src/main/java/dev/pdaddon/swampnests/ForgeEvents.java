package dev.pdaddon.swampnests;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = PDSwampNests.MODID)
public final class ForgeEvents {
    private static final ResourceLocation CHARGE_ID =
            new ResourceLocation(PDSwampNests.POISON_DRAGONS, "poison_dragon_charge");
    private static final String HANDLED = "pd_swamp_nests_handled";

    private ForgeEvents() {}

    /** Bola de veneno (blast): al impactar deja una nube. */
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide || !AddonConfig.CLOUD_FROM_BLAST.get()) return;

        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType());
        if (!CHARGE_ID.equals(id)) return;
        if (projectile.getPersistentData().getBoolean(HANDLED)) return;
        projectile.getPersistentData().putBoolean(HANDLED, true);

        Vec3 hit = event.getRayTraceResult().getLocation();
        PoisonCloudSpawner.spawn(projectile.level(), projectile.getOwner(), hit.x, hit.y, hit.z);
    }

    /** Quita el veneno de la nube a quien deberia estar a salvo (dragon, jinete, dueno). */
    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEffectSource() instanceof AreaEffectCloud cloud)) return;
        if (!cloud.getTags().contains(PoisonCloudSpawner.TAG)) return;

        LivingEntity victim = event.getEntity();
        if (!PoisonCloudSpawner.isProtected(cloud, victim)) return;

        MobEffectInstance added = event.getEffectInstance();
        MobEffectInstance previous = event.getOldEffectInstance();
        victim.removeEffect(added.getEffect());
        if (previous != null) {
            victim.addEffect(previous);
        }
    }
}
