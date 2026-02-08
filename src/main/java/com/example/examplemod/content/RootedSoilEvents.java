package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RootedSoilEvents {
    private RootedSoilEvents() {
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }

        int baseX = cp.x << 4;
        int baseZ = cp.z << 4;
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = baseX + dx;
                int z = baseZ + dz;
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (level.getBlockState(pos).is(ModBlocks.ROOTED_SOIL.get())) {
                        // keep as-is; no scheduled growth from this mod
                    }
                }
            }
        }
    }
}
