package com.example.examplemod.content;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

public final class RootedSoilData extends SavedData {
    private static final String NAME = "examplemod_rooted_soil";
    private final Long2ObjectOpenHashMap<LongOpenHashSet> byChunk = new Long2ObjectOpenHashMap<>();

    public static RootedSoilData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(RootedSoilData::load, RootedSoilData::new, NAME);
    }

    public void add(BlockPos pos) {
        long chunk = new ChunkPos(pos).toLong();
        LongOpenHashSet set = byChunk.get(chunk);
        if (set == null) {
            set = new LongOpenHashSet();
            byChunk.put(chunk, set);
        }
        if (set.add(pos.asLong())) {
            setDirty();
        }
    }

    public void remove(BlockPos pos) {
        long chunk = new ChunkPos(pos).toLong();
        LongOpenHashSet set = byChunk.get(chunk);
        if (set == null) {
            return;
        }
        if (set.remove(pos.asLong())) {
            if (set.isEmpty()) {
                byChunk.remove(chunk);
            }
            setDirty();
        }
    }

    public LongSet positionsInChunk(ChunkPos chunkPos) {
        LongOpenHashSet set = byChunk.get(chunkPos.toLong());
        return set == null ? LongSets.EMPTY_SET : set;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag chunks = new ListTag();
        for (Long2ObjectMap.Entry<LongOpenHashSet> e : byChunk.long2ObjectEntrySet()) {
            CompoundTag t = new CompoundTag();
            t.putLong("c", e.getLongKey());
            long[] arr = e.getValue().toLongArray();
            t.put("p", new LongArrayTag(arr));
            chunks.add(t);
        }
        tag.put("chunks", chunks);
        return tag;
    }

    public static RootedSoilData load(CompoundTag tag) {
        RootedSoilData d = new RootedSoilData();
        ListTag chunks = tag.getList("chunks", Tag.TAG_COMPOUND);
        for (int i = 0; i < chunks.size(); i++) {
            CompoundTag t = chunks.getCompound(i);
            long chunk = t.getLong("c");
            long[] arr = t.getLongArray("p");
            if (arr.length == 0) {
                continue;
            }
            LongOpenHashSet set = new LongOpenHashSet(arr);
            d.byChunk.put(chunk, set);
        }
        return d;
    }
}
