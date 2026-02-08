package com.example.examplemod.content.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class LarvaEntity extends Animal {
    public LarvaEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public net.minecraft.world.entity.AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, net.minecraft.world.entity.AgeableMob other) {
        return null;
    }
}
