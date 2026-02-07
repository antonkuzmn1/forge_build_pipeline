package com.example.examplemod.content.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;

public class NautilusEntity extends WaterAnimal {
    public NautilusEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }
}
