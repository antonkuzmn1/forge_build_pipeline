package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.ModBlocks;
import com.example.examplemod.content.RootedSoilData;
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

        ChunkPos cp = chunk.getPos();
        for (long p : RootedSoilData.get(level).positionsInChunk(cp)) {
            BlockPos pos = BlockPos.of(p);
            if (level.getBlockState(pos).is(ModBlocks.ROOTED_SOIL.get())) {
                level.scheduleTick(pos, ModBlocks.ROOTED_SOIL.get(), 600);
            } else {
                RootedSoilData.get(level).remove(pos);
            }
        }
    }
}
