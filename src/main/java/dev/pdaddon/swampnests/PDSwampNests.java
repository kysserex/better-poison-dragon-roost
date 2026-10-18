package dev.pdaddon.swampnests;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(PDSwampNests.MODID)
public class PDSwampNests {
    public static final String MODID = "pd_swamp_nests";
    public static final String POISON_DRAGONS = "poison_dragons";

    public PDSwampNests() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AddonConfig.SPEC);
    }
}
