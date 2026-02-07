package com.example.examplemod.content.entity;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<NautilusEntity>> NAUTILUS = ENTITY_TYPES.register("nautilus",
            () -> EntityType.Builder.<NautilusEntity>of(NautilusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.7f, 0.5f)
                    .build("nautilus"));

    public static final RegistryObject<EntityType<ShellEntity>> SHELL = ENTITY_TYPES.register("shell",
            () -> EntityType.Builder.<ShellEntity>of(ShellEntity::new, MobCategory.MISC)
                    .sized(0.4f, 0.2f)
                    .build("shell"));

    public static final RegistryObject<EntityType<LarvaEntity>> LARVA = ENTITY_TYPES.register("larva",
            () -> EntityType.Builder.<LarvaEntity>of(LarvaEntity::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.2f)
                    .build("larva"));

    private ModEntities() {}
}
