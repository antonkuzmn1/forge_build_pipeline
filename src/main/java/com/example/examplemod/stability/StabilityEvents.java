package com.example.examplemod.stability;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.SupportBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StabilityEvents {
    private StabilityEvents() {
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getPlacedBlock();
        BlockPos pos = event.getPos();

        if (state.getBlock() instanceof GraveBlock) {
            StabilityManager.registerGrave(level, pos);
            return;
        }

        if (state.getBlock() instanceof SupportBlock support) {
            if (state.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER) {
                StabilityManager.registerSupport(level, pos, support.type().radius());
            }
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (!StabilityManager.isSafe(level, pos)) {
            Crumble.collapseAbove(level, pos);
        }

        if (state.getBlock() instanceof GraveBlock) {
            StabilityManager.unregisterCenter(level, pos);
            return;
        }

        if (state.getBlock() instanceof SupportBlock) {
            BlockPos lower = state.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            StabilityManager.unregisterCenter(level, lower);
        }
    }
}
