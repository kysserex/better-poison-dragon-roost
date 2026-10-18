package dev.pdaddon.swampnests;

import net.minecraftforge.common.ForgeConfigSpec;

public final class AddonConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CONVERT_ROOSTS;
    public static final ForgeConfigSpec.BooleanValue CONVERT_CAVES;
    public static final ForgeConfigSpec.DoubleValue SUSPICIOUS_GRAVEL_CHANCE;

    public static final ForgeConfigSpec.BooleanValue CLOUD_FROM_STREAM;
    public static final ForgeConfigSpec.BooleanValue CLOUD_FROM_BLAST;
    public static final ForgeConfigSpec.IntValue CLOUD_DURATION_TICKS;
    public static final ForgeConfigSpec.DoubleValue CLOUD_RADIUS;
    public static final ForgeConfigSpec.IntValue CLOUD_STREAM_INTERVAL_TICKS;
    public static final ForgeConfigSpec.IntValue POISON_SECONDS;
    public static final ForgeConfigSpec.IntValue POISON_AMPLIFIER;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("nests");
        CONVERT_ROOSTS = b
                .comment("Cambia los bloques del nido (piedra, roca, montones) por barro, raices, roble oscuro y grava.")
                .define("convertRoosts", true);
        CONVERT_CAVES = b
                .comment("Igual que convertRoosts, pero para las cuevas de dragon (incluye las estalactitas).")
                .define("convertCaves", true);
        SUSPICIOUS_GRAVEL_CHANCE = b
                .comment("Probabilidad (0 a 1) de que un monton del nido sea grava sospechosa. El resto es grava normal.")
                .defineInRange("suspiciousGravelChance", 0.5D, 0.0D, 1.0D);
        b.pop();

        b.push("poison_cloud");
        CLOUD_FROM_STREAM = b
                .comment("Dejar nube con el aliento continuo (chorro).")
                .define("fromStreamBreath", true);
        CLOUD_FROM_BLAST = b
                .comment("Dejar nube cuando impacta la bola de veneno (blast).")
                .define("fromBlastCharge", true);
        CLOUD_DURATION_TICKS = b
                .comment("Duracion de la nube en ticks (20 ticks = 1 segundo).")
                .defineInRange("durationTicks", 100, 20, 1200);
        CLOUD_RADIUS = b
                .comment("Radio inicial de la nube en bloques.")
                .defineInRange("radius", 2.5D, 0.5D, 8.0D);
        CLOUD_STREAM_INTERVAL_TICKS = b
                .comment("Cada cuantos ticks el chorro puede crear o renovar una nube.")
                .defineInRange("streamIntervalTicks", 8, 1, 40);
        POISON_SECONDS = b
                .comment("Segundos de veneno que aplica la nube.")
                .defineInRange("poisonSeconds", 5, 1, 60);
        POISON_AMPLIFIER = b
                .comment("Nivel del veneno (0 = Veneno I, 1 = Veneno II...).")
                .defineInRange("poisonAmplifier", 0, 0, 4);
        b.pop();

        SPEC = b.build();
    }

    private AddonConfig() {}
}
