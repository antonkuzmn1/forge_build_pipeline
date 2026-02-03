package com.example.examplemod.stability;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public final class StabilityManager {
    private static final int GRAVE_RADIUS = 1;

    private StabilityManager() {
    }

    public static void registerGrave(ServerLevel level, BlockPos pos) {
        StabilityData.get(level).setCenter(pos, GRAVE_RADIUS);
    }

    public static void registerSupport(ServerLevel level, BlockPos lowerPos, int radius) {
        StabilityData.get(level).setCenter(lowerPos, radius);
    }

    public static void unregisterCenter(ServerLevel level, BlockPos pos) {
        StabilityData.get(level).removeCenter(pos);
    }

    public static boolean isSafe(ServerLevel level, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        for (var e : StabilityData.get(level).centers().long2IntEntrySet()) {
            long key = e.getLongKey();
            int r = e.getIntValue();
            if (r <= 0) {
                continue;
            }
            BlockPos c = BlockPos.of(key);
            int dx = Math.abs(x - c.getX());
            int dz = Math.abs(z - c.getZ());
            if (Math.max(dx, dz) <= r) {
                return true;
            }
        }
        return false;
    }
}
