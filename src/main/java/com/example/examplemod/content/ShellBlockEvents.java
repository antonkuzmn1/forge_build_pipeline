package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.ShellBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ShellBlockEvents {
    private ShellBlockEvents() {}

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!event.getState().is(ModBlocks.SHELL_BLOCK.get())) return;
        BlockPos pos = event.getPos();
        var state = event.getState();
        int half = state.getValue(ShellBlock.HALF);
        event.setCanceled(true);
        if (half == 0) {
            var newState = state.setValue(ShellBlock.HALF, 1 + level.getRandom().nextInt(4))
                    .setValue(ShellBlock.SHELL_TYPE, level.getRandom().nextInt(3));
            level.setBlock(pos, newState, 3);
        } else {
            Block.popResource(level, pos, new ItemStack(ModItems.SHELL.get()));
            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
