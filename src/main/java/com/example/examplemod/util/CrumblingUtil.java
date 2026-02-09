package com.example.examplemod.util;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

public class CrumblingUtil
{
    private static final Set<String> CRUMBABLE_BLOCKS = new HashSet<>();

    static
    {
        // Soil types
        CRUMBABLE_BLOCKS.add("minecraft:dirt");
        CRUMBABLE_BLOCKS.add("minecraft:coarse_dirt");
        CRUMBABLE_BLOCKS.add("minecraft:grass_block");
        CRUMBABLE_BLOCKS.add("minecraft:podzol");

        // Ice types
        CRUMBABLE_BLOCKS.add("minecraft:ice");
        CRUMBABLE_BLOCKS.add("minecraft:packed_ice");
        CRUMBABLE_BLOCKS.add("minecraft:blue_ice");
        CRUMBABLE_BLOCKS.add("minecraft:snow_block");
        CRUMBABLE_BLOCKS.add("minecraft:frosted_ice");

        // Stone types
        CRUMBABLE_BLOCKS.add("minecraft:sandstone");
        CRUMBABLE_BLOCKS.add("minecraft:red_sandstone");
        CRUMBABLE_BLOCKS.add("minecraft:granite");
        CRUMBABLE_BLOCKS.add("minecraft:stone");
        CRUMBABLE_BLOCKS.add("minecraft:andesite");
        CRUMBABLE_BLOCKS.add("minecraft:diorite");
        CRUMBABLE_BLOCKS.add("minecraft:polished_granite");
        CRUMBABLE_BLOCKS.add("minecraft:polished_andesite");
        CRUMBABLE_BLOCKS.add("minecraft:polished_diorite");
    }

    public static boolean isCrumbleable(BlockState state)
    {
        String blockName = state.getBlock().builtInRegistryHolder().key().location().toString();
        return CRUMBABLE_BLOCKS.contains(blockName);
    }

    /**
     * Check if this block is a support block (mushroom stem pillar)
     */
    public static boolean isSupportBlock(Level level, BlockPos pos)
    {
        String blockName = level.getBlockState(pos).getBlock().builtInRegistryHolder().key().location().toString();
        return blockName.equals("crumblingmod:mushroom_support");
    }

    /**
     * Handle crumbling in a 3x3 area centered at the given position
     */
    public static void triggerCrumbling(Level level, BlockPos center)
    {
        if (level.isClientSide)
            return;

        // Check 3x3 area horizontally around the center
        for (int x = center.getX() - 1; x <= center.getX() + 1; x++)
        {
            for (int z = center.getZ() - 1; z <= center.getZ() + 1; z++)
            {
                BlockPos pos = new BlockPos(x, center.getY(), z);
                BlockState state = level.getBlockState(pos);

                if (isCrumbleable(state) && !(state.getBlock() instanceof FallingBlock))
                {
                    makeFall(level, pos);
                }
            }
        }
    }

    /**
     * Make a block fall using FallingBlockEntity
     */
    public static void makeFall(Level level, BlockPos pos)
    {
        if (level.isClientSide)
            return;

        BlockState state = level.getBlockState(pos);
        if (!isCrumbleable(state))
            return;

        level.destroyBlock(pos, false);

        FallingBlockEntity entity = EntityType.FALLING_BLOCK.create(level);
        if (entity != null)
        {
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setBlockState(state);
            level.addFreshEntity(entity);
        }
    }

    /**
     * Check if a position is within safe zone of any support
     */
    public static boolean isInSafeZone(Level level, BlockPos checkPos)
    {
        // Get the block below to find the ground level
        int centerY = checkPos.getY();

        // Search for support structures in a limited horizontal range
        int horizontalSearchRadius = 3;
        for (int x = checkPos.getX() - horizontalSearchRadius; x <= checkPos.getX() + horizontalSearchRadius; x++)
        {
            for (int z = checkPos.getZ() - horizontalSearchRadius; z <= checkPos.getZ() + horizontalSearchRadius; z++)
            {
                // Check only nearby Y levels
                for (int y = centerY - 20; y <= centerY + 20; y++)
                {
                    BlockPos supportPos = new BlockPos(x, y, z);
                    if (isSupportBlock(level, supportPos))
                    {
                        if (isWithinSafeZone(checkPos, supportPos))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a position is within 3x3 safe zone of a support block
     */
    private static boolean isWithinSafeZone(BlockPos checkPos, BlockPos supportPos)
    {
        int dx = Math.abs(checkPos.getX() - supportPos.getX());
        int dz = Math.abs(checkPos.getZ() - supportPos.getZ());
        // 3x3 zone = 1 block radius
        return dx <= 1 && dz <= 1;
    }

    /**
     * Get safe zone radius (3x3 = 1 block radius from center)
     */
    public static int getSafeZoneRadius()
    {
        return 1;
    }
}
