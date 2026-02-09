package com.example.examplemod.event;

import com.example.examplemod.content.block.MushroomSupportBlock;
import com.example.examplemod.util.CrumblingUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

@Mod.EventBusSubscriber(modid = "crumblingmod")
public class BlockEvents
{
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        if (!(event.getLevel() instanceof Level))
            return;

        Level level = (Level) event.getLevel();
        if (level.isClientSide)
            return;
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();

        // Handle mushroom support block destruction
        if (block instanceof MushroomSupportBlock)
        {
            handleSupportBlockBreak(level, pos, event.getState());
            return;
        }

        // Check if block adjacent to support was broken - this requires crumbling nearby
        checkAdjacentSupportBlocks(level, pos);

        // Trigger crumbling at the break position if outside safe zone
        if (!CrumblingUtil.isInSafeZone(level, pos))
        {
            CrumblingUtil.triggerCrumbling(level, pos);
        }
    }

    @SubscribeEvent
    public static void onNeighborChange(BlockEvent.NeighborNotifyEvent event)
    {
        if (!(event.getLevel() instanceof Level))
            return;

        Level level = (Level) event.getLevel();
        if (level.isClientSide)
            return;
        BlockPos pos = event.getPos();

        // Check if a support block lost support
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof MushroomSupportBlock)
        {
            boolean isUpper = level.getBlockState(pos).getValue(MushroomSupportBlock.HALF) == DoubleBlockHalf.UPPER;

            // Upper block must have lower block below
            if (isUpper && level.isEmptyBlock(pos.below()))
            {
                level.destroyBlock(pos, false);
                // Trigger crumbling around the destroyed position
                CrumblingUtil.triggerCrumbling(level, pos);
                return;
            }

            // Lower block must have upper block above
            if (!isUpper && level.isEmptyBlock(pos.above()))
            {
                level.destroyBlock(pos, false);
                // Trigger crumbling around the destroyed position
                CrumblingUtil.triggerCrumbling(level, pos);
                return;
            }
        }
    }

    private static void handleSupportBlockBreak(Level level, BlockPos pos, net.minecraft.world.level.block.state.BlockState state)
    {
        // Trigger crumbling in the 3x3 area around support
        CrumblingUtil.triggerCrumbling(level, pos);

        // Destroy the counterpart block
        boolean isUpper = state.getValue(MushroomSupportBlock.HALF) == DoubleBlockHalf.UPPER;
        BlockPos counterpart = isUpper ? pos.below() : pos.above();

        if (level.getBlockState(counterpart).getBlock() instanceof MushroomSupportBlock)
        {
            level.destroyBlock(counterpart, false);
            // Also trigger crumbling around the counterpart
            CrumblingUtil.triggerCrumbling(level, counterpart);
        }
    }

    private static void checkAdjacentSupportBlocks(Level level, BlockPos pos)
    {
        // Check if any adjacent block is part of a support pillar
        BlockPos[] adjacent = {
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
        };

        for (BlockPos adjPos : adjacent)
        {
            Block adjBlock = level.getBlockState(adjPos).getBlock();
            if (adjBlock instanceof MushroomSupportBlock)
            {
                // Block adjacent to support was broken, trigger crumbling around support
                CrumblingUtil.triggerCrumbling(level, adjPos);
            }
        }
    }
}
