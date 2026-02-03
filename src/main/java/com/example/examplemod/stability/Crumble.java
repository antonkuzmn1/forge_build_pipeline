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

    private Crumble() {
    }

    public static void collapseAbove(ServerLevel level, BlockPos brokenPos) {
        BlockPos.MutableBlockPos m = brokenPos.mutable();
        for (int i = 0; i < MAX_FALL; i++) {
            m.set(brokenPos.getX(), brokenPos.getY() + 1 + i, brokenPos.getZ());
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
            if (st.getDestroySpeed(level, m) < 0) {
                break;
            }
            FallingBlockEntity.fall(level, m.immutable(), st);
        }
    }
}
