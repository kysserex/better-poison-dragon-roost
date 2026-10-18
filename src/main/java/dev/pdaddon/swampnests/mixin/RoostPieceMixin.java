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

/**
 * Se engancha al final de postProcess del nido de Poison Dragons y cambia sus bloques.
 * Se indican el nombre oficial y el SRG (m_213694_) para que funcione en desarrollo y en el juego normal.
 */
@Mixin(targets = "com.menoxd.poisondragons.world.structure.piece.PoisonDragonRoostPiece", remap = false)
public abstract class RoostPieceMixin {

    @Inject(method = {"postProcess", "m_213694_"}, at = @At("RETURN"), remap = false, require = 1)
    private void pdSwampNests$convertRoost(WorldGenLevel level, StructureManager structureManager,
                                           ChunkGenerator generator, RandomSource random, BoundingBox box,
                                           ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        StructurePiece self = (StructurePiece) (Object) this;
        NestConverter.convert(level, random, box, self.getBoundingBox(), AddonConfig.CONVERT_ROOSTS.get());
    }
}
