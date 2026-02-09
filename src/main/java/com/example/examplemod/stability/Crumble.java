package com.example.examplemod.stability;

import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.SupportBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class Crumble {
    private static final int MAX_FALL = 96;
    private static final int RADIUS = 8;

    private Crumble() {
    }

    public static void trigger(ServerLevel level, BlockPos brokenPos) {
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                collapseColumn(level, brokenPos.getX() + dx, brokenPos.getZ() + dz);
            }
        }
    }

    public static void triggerChunk(ServerLevel level, int chunkX, int chunkZ) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                collapseColumn(level, baseX + dx, baseZ + dz);
            }
        }
    }

    private static void collapseColumn(ServerLevel level, int x, int z) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        BlockPos.MutableBlockPos m = new BlockPos(x, 0, z).mutable();
        for (int y = maxY - 2; y >= minY; y--) {
            m.set(x, y + 1, z);
            BlockState st = level.getBlockState(m);
            if (st.isAir()) continue;
            if (!st.getFluidState().isEmpty()) continue;
            if (st.getBlock() == Blocks.BEDROCK) continue;
            if (st.hasBlockEntity()) continue;
            m.set(x, y, z);
            if (!level.getBlockState(m).isAir()) continue;
            boolean isSupportOrGrave = st.getBlock() instanceof SupportBlock || st.getBlock() instanceof GraveBlock;
            if (!isSupportOrGrave && !isCrumbleBlock(st)) continue;
            BlockPos blockPos = m.immutable().above();
            if (!isSupportOrGrave && StabilityManager.isSafe(level, blockPos)) continue;
            m.set(x, y + 1, z);
            if (st.getBlock() instanceof SupportBlock && st.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER) {
                level.setBlock(blockPos.above(), Blocks.AIR.defaultBlockState(), 3);
                StabilityManager.unregisterCenter(level, blockPos);
            }
            if (st.getBlock() instanceof GraveBlock) {
                StabilityManager.unregisterCenter(level, blockPos);
            }
            FallingBlockEntity.fall(level, blockPos, st);
        }
    }

    private static boolean isCrumbleBlock(BlockState st) {
        // Dirt types
        if (st.is(Blocks.DIRT) || st.is(Blocks.COARSE_DIRT) || st.is(Blocks.GRASS_BLOCK) || st.is(Blocks.PODZOL)) {
            return true;
        }
        // Ice types
        if (st.is(Blocks.ICE) || st.is(Blocks.PACKED_ICE) || st.is(Blocks.BLUE_ICE)) {
            return true;
        }
        // Sandstone
        if (st.is(Blocks.SANDSTONE) || st.is(Blocks.RED_SANDSTONE)) {
            return true;
        }
        // Stone variants
        if (st.is(Blocks.STONE) || st.is(Blocks.GRANITE) || st.is(Blocks.ANDESITE) || st.is(Blocks.DIORITE) || st.is(Blocks.POLISHED_DIORITE)) {
            return true;
        }
        return false;
    }
}
