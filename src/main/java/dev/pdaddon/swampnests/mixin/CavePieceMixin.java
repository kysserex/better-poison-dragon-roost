package dev.pdaddon.swampnests.mixin;

import dev.pdaddon.swampnests.AddonConfig;
import dev.pdaddon.swampnests.NestConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Igual que RoostPieceMixin, pero para la cueva de dragon (poison_dragon_cave). */
@Mixin(targets = "com.menoxd.poisondragons.world.structure.piece.PoisonDragonCavePiece", remap = false)
public abstract class CavePieceMixin {

    @Inject(method = {"postProcess", "m_213694_"}, at = @At("RETURN"), remap = false, require = 1)
    private void pdSwampNests$convertCave(WorldGenLevel level, StructureManager structureManager,
                                          ChunkGenerator generator, RandomSource random, BoundingBox box,
                                          ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        StructurePiece self = (StructurePiece) (Object) this;
        NestConverter.convert(level, random, box, self.getBoundingBox(), AddonConfig.CONVERT_CAVES.get());
    }
}
