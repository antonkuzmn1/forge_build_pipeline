package com.example.examplemod.content.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.example.examplemod.ExampleMod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EntityAttributeSubscriber {
    private EntityAttributeSubscriber() {}

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        var nautilus = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .build();

        var larva = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .build();

        if (ModEntities.NAUTILUS.isPresent()) event.put(ModEntities.NAUTILUS.get(), nautilus);
        if (ModEntities.LARVA.isPresent()) event.put(ModEntities.LARVA.get(), larva);
    }
}
