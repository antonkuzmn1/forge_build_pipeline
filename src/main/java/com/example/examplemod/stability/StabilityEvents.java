package com.example.examplemod.stability;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.SupportBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
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

        if (state.getBlock() instanceof GraveBlock) {
            StabilityManager.unregisterCenter(level, pos);
        } else if (state.getBlock() instanceof SupportBlock) {
            BlockPos lower = state.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            StabilityManager.unregisterCenter(level, lower);
        }

        if (!StabilityManager.isSafe(level, pos)) {
            Crumble.trigger(level, pos);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        ChunkPos cp = event.getChunk().getPos();
        int centerX = (cp.x << 4) + 8;
        int centerZ = (cp.z << 4) + 8;
        for (Player p : level.players()) {
            double dx = Math.abs(p.getX() - centerX);
            double dz = Math.abs(p.getZ() - centerZ);
            if (dx <= 64 && dz <= 64) {
                Crumble.triggerChunk(level, cp.x, cp.z);
                break;
            }
        }
    }
}
