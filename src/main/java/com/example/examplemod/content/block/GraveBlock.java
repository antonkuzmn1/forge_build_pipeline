package com.example.examplemod.content.block;

import com.example.examplemod.stability.Crumble;
import com.example.examplemod.stability.StabilityManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public final class GraveBlock extends Block {
    public GraveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level instanceof ServerLevel sl) {
                StabilityManager.unregisterCenter(sl, pos);
                Crumble.trigger(sl, pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
