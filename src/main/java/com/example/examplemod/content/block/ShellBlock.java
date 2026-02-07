package com.example.examplemod.content.block;

import com.example.examplemod.content.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ShellBlock extends Block {
    public static final IntegerProperty SHELL_TYPE = IntegerProperty.create("shell_type", 0, 2);
    public static final IntegerProperty HALF = IntegerProperty.create("half", 0, 4);

    private static final VoxelShape FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape HALF_EAST = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape HALF_WEST = Block.box(8, 0, 0, 16, 16, 16);
    private static final VoxelShape HALF_NORTH = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape HALF_SOUTH = Block.box(0, 0, 0, 16, 16, 8);

    public ShellBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SHELL_TYPE, 0).setValue(HALF, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHELL_TYPE, HALF);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int h = state.getValue(HALF);
        if (h == 0) return FULL;
        int meta = (h - 1) & 3;
        return switch (meta) {
            case 0 -> HALF_EAST;
            case 1 -> HALF_WEST;
            case 2 -> HALF_NORTH;
            default -> HALF_SOUTH;
        };
    }
}
