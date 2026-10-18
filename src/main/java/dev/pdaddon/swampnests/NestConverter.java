package dev.pdaddon.swampnests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Cambia los bloques propios de Poison Dragons que usa el nido por bloques de pantano.
 * Se ejecuta justo despues de que el nido genera cada chunk, y solo toca el area escribible del chunk.
 */
public final class NestConverter {
    private static final ResourceLocation LOOT = new ResourceLocation(PDSwampNests.MODID, "archaeology/roost");

    private static final int MARGIN_XZ = 2;
    private static final int MARGIN_Y = 3;

    private static volatile boolean resolved = false;
    private static Block poisonedStone;
    private static Block poisonedCobblestone;
    private static Block poisonedPile;

    private NestConverter() {}

    private static Block lookup(String path) {
        ResourceLocation id = new ResourceLocation(PDSwampNests.POISON_DRAGONS, path);
        return ForgeRegistries.BLOCKS.containsKey(id) ? ForgeRegistries.BLOCKS.getValue(id) : null;
    }

    private static void resolve() {
        if (resolved) return;
        poisonedStone = lookup("poisoned_stone");
        poisonedCobblestone = lookup("poisoned_cobblestone");
        poisonedPile = lookup("poisoned_pile");
        resolved = true;
    }

    public static void convert(WorldGenLevel level, RandomSource random, BoundingBox writable, BoundingBox piece, boolean enabled) {
        if (!enabled) return;
        resolve();

        int minX = Math.max(writable.minX(), piece.minX() - MARGIN_XZ);
        int maxX = Math.min(writable.maxX(), piece.maxX() + MARGIN_XZ);
        int minY = Math.max(writable.minY(), piece.minY() - MARGIN_Y);
        int maxY = Math.min(writable.maxY(), piece.maxY() + MARGIN_Y);
        int minZ = Math.max(writable.minZ(), piece.minZ() - MARGIN_XZ);
        int maxZ = Math.min(writable.maxZ(), piece.maxZ() + MARGIN_XZ);
        if (minX > maxX || minY > maxY || minZ > maxZ) return;

        double gravelChance = AddonConfig.SUSPICIOUS_GRAVEL_CHANCE.get();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
                    pos.set(x, y, z);
                    Block block = level.getBlockState(pos).getBlock();

                    if (block == poisonedStone) {
                        level.setBlock(pos, pickGround(random), 2);
                    } else if (block == poisonedCobblestone) {
                        level.setBlock(pos, pickWood(random), 2);
                    } else if (block == poisonedPile) {
                        placePile(level, pos, random, gravelChance);
                    }
                }
            }
        }
    }

    /** Suelo del nido: barro y raices. */
    private static BlockState pickGround(RandomSource r) {
        int v = r.nextInt(100);
        if (v < 50) return Blocks.MUD.defaultBlockState();
        if (v < 75) return Blocks.ROOTED_DIRT.defaultBlockState();
        if (v < 90) return pillar(Blocks.MUDDY_MANGROVE_ROOTS, r);
        return Blocks.MANGROVE_ROOTS.defaultBlockState();
    }

    /** Muros y troncos del nido: madera de roble oscuro mezclada con barro y raices. */
    private static BlockState pickWood(RandomSource r) {
        int v = r.nextInt(100);
        if (v < 30) return pillar(Blocks.DARK_OAK_LOG, r);
        if (v < 55) return pillar(Blocks.DARK_OAK_WOOD, r);
        if (v < 70) return Blocks.DARK_OAK_PLANKS.defaultBlockState();
        if (v < 85) return Blocks.MUD.defaultBlockState();
        return Blocks.ROOTED_DIRT.defaultBlockState();
    }

    private static BlockState pillar(Block block, RandomSource r) {
        Direction.Axis axis = Direction.Axis.values()[r.nextInt(3)];
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
    }

    /** Los montones de tesoro pasan a ser grava sospechosa (con loot) o grava normal. */
    private static void placePile(WorldGenLevel level, BlockPos pos, RandomSource random, double chance) {
        if (random.nextDouble() < chance) {
            level.setBlock(pos, Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), 2);
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BrushableBlockEntity brushable) {
                brushable.setLootTable(LOOT, random.nextLong());
            }
        } else {
            level.setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 2);
        }
    }
}
