package com.example.examplemod.content.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;

public class ShellEntity extends Entity {
    public ShellEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
}
