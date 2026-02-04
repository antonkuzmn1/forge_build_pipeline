package com.example.examplemod.content.block;

import com.example.examplemod.content.RootedSoilData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class RootedSoilBlock extends Block {
    private static final int TICK_RATE = 600;

    public RootedSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level instanceof ServerLevel sl) {
            RootedSoilData.get(sl).add(pos);
            sl.scheduleTick(pos, this, TICK_RATE);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level instanceof ServerLevel sl) {
                RootedSoilData.get(sl).remove(pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        tryGrow(level, pos);
        level.scheduleTick(pos, this, TICK_RATE);
    }

    private static void tryGrow(ServerLevel level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        if (!belowState.isAir()) {
            return;
        }
        BlockState roots = Blocks.HANGING_ROOTS.defaultBlockState();
        if (!roots.canSurvive(level, below)) {
            return;
        }
        level.setBlock(below, roots, 3);
    }
}
