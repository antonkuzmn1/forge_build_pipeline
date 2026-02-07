package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RootedSoilEvents {
    private static final int TICK_INTERVAL = 600;

    private RootedSoilEvents() {
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel level)) return;
        long t = level.getGameTime();
        if (t % TICK_INTERVAL != 0) return;
        for (ServerPlayer player : level.players()) {
            BlockPos p = player.blockPosition();
            int cx = p.getX() >> 4;
            int cz = p.getZ() >> 4;
            for (int dcx = -2; dcx <= 2; dcx++) {
                for (int dcz = -2; dcz <= 2; dcz++) {
                    if (!level.hasChunk(cx + dcx, cz + dcz)) continue;
                    tryGrowInChunk(level, cx + dcx, cz + dcz, level.getRandom());
                }
            }
        }
    }

    private static void tryGrowInChunk(ServerLevel level, int chunkX, int chunkZ, RandomSource random) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        for (int attempt = 0; attempt < 4; attempt++) {
            int x = baseX + random.nextInt(16);
            int z = baseZ + random.nextInt(16);
            int minY = level.getMinBuildHeight();
            int maxY = level.getMaxBuildHeight();
            for (int y = maxY - 1; y >= minY; y--) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!level.getBlockState(pos).is(Blocks.ROOTED_DIRT)) continue;
                BlockPos below = pos.below();
                if (!level.getBlockState(below).isAir()) continue;
                if (!Blocks.HANGING_ROOTS.defaultBlockState().canSurvive(level, below)) continue;
                level.setBlock(below, Blocks.HANGING_ROOTS.defaultBlockState(), 3);
                return;
            }
        }
    }
}
