package com.example.examplemod.stability;

import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.SupportBlock;
import net.minecraft.core.BlockPos;
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
            if (st.getBlock() instanceof SupportBlock) continue;
            if (st.getBlock() instanceof GraveBlock) continue;
            if (!isCrumbleBlock(st)) continue;
            if (StabilityManager.isSafe(level, m.immutable())) continue;
            m.set(x, y, z);
            if (!level.getBlockState(m).isAir()) continue;
            m.set(x, y + 1, z);
            FallingBlockEntity.fall(level, m.immutable(), st);
        }
    }

    private static boolean isCrumbleBlock(BlockState st) {
        return st.is(Blocks.STONE) || st.is(Blocks.GRANITE) || st.is(Blocks.ANDESITE) || st.is(Blocks.DIORITE) || st.is(Blocks.POLISHED_DIORITE);
    }
}
