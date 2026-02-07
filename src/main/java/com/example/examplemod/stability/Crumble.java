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
                collapseColumn(level, brokenPos.offset(dx, 0, dz));
            }
        }
    }

    private static void collapseColumn(ServerLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos m = origin.mutable();
        for (int i = 0; i < MAX_FALL; i++) {
            m.set(origin.getX(), origin.getY() + 1 + i, origin.getZ());
            BlockState st = level.getBlockState(m);
            if (st.isAir()) {
                break;
            }
            if (!st.getFluidState().isEmpty()) {
                break;
            }
            if (st.getBlock() == Blocks.BEDROCK) {
                break;
            }
            if (st.hasBlockEntity()) {
                break;
            }
            if (st.getBlock() instanceof SupportBlock) {
                break;
            }
            if (st.getBlock() instanceof GraveBlock) {
                break;
            }
            if (!isCrumbleBlock(st)) {
                break;
            }
            if (StabilityManager.isSafe(level, m.immutable())) {
                break;
            }
            BlockPos below = m.below();
            if (!level.getBlockState(below).isAir()) {
                break;
            }
            FallingBlockEntity.fall(level, m.immutable(), st);
        }
    }

    private static boolean isCrumbleBlock(BlockState st) {
        return st.is(Blocks.STONE) || st.is(Blocks.GRANITE) || st.is(Blocks.ANDESITE) || st.is(Blocks.DIORITE) || st.is(Blocks.POLISHED_DIORITE);
    }
}
