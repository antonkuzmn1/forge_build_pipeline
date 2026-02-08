package com.example.examplemod.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ShellEntity extends Entity {
    public ShellEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        // no entity data to define
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        // no extra data
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        // no extra data
    }
}
