package com.example.examplemod.stability;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class StabilityData extends SavedData {
    private static final String NAME = "examplemod_stability";
    private final Long2IntOpenHashMap centers = new Long2IntOpenHashMap();

    public StabilityData() {
        centers.defaultReturnValue(0);
    }

    public static StabilityData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(StabilityData::load, StabilityData::new, NAME);
    }

    public void setCenter(BlockPos pos, int radius) {
        centers.put(pos.asLong(), radius);
        setDirty();
    }

    public void removeCenter(BlockPos pos) {
        if (centers.remove(pos.asLong()) != centers.defaultReturnValue()) {
            setDirty();
        }
    }

    public Long2IntMap centers() {
        return centers;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Long2IntMap.Entry e : centers.long2IntEntrySet()) {
            CompoundTag t = new CompoundTag();
            t.putLong("p", e.getLongKey());
            t.putInt("r", e.getIntValue());
            list.add(t);
        }
        tag.put("c", list);
        return tag;
    }

    public static StabilityData load(CompoundTag tag) {
        StabilityData d = new StabilityData();
        ListTag list = tag.getList("c", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            long p = t.getLong("p");
            int r = t.getInt("r");
            if (r > 0) {
                d.centers.put(p, r);
            }
        }
        return d;
    }
}
