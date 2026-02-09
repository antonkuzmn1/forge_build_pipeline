package com.example.examplemod.content.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MushroomSupportBlock extends Block
{
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public MushroomSupportBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
        {
            // Top part has slight expansion
            return Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);
        }
        else
        {
            // Bottom part standard stem
            return Shapes.box(0.375, 0, 0.375, 0.625, 1, 0.625);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (!level.isClientSide)
        {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            {
                BlockState upperState = this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER);
                level.setBlock(pos.above(), upperState, 3);
            }
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
    {
        if (!level.isClientSide() && level instanceof Level)
        {
            // Remove counterpart block
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            {
                BlockPos below = pos.below();
                if (level.getBlockState(below).getBlock() instanceof MushroomSupportBlock)
                {
                    level.destroyBlock(below, false);
                }
            }
            else
            {
                BlockPos above = pos.above();
                if (level.getBlockState(above).getBlock() instanceof MushroomSupportBlock)
                {
                    level.destroyBlock(above, false);
                }
            }
        }
    }
}

