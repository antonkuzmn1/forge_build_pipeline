package com.example.examplemod.content.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class EntityAttributeRegistry {
    private EntityAttributeRegistry() {}

    public static void register(IEventBus bus) {
        bus.addListener(EntityAttributeRegistry::onEntityAttributeCreation);
    }

    private static void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder nautilus = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);

        AttributeSupplier.Builder larva = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D);

        if (ModEntities.NAUTILUS.isPresent()) event.put(ModEntities.NAUTILUS.get(), nautilus.build());
        if (ModEntities.LARVA.isPresent()) event.put(ModEntities.LARVA.get(), larva.build());
    }
}
