package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final RegistryObject<Item> GRAVE = ExampleMod.ITEMS.register("grave", () -> new BlockItem(ModBlocks.GRAVE.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUSHROOM_SUPPORT = ExampleMod.ITEMS.register("mushroom_support", () -> new BlockItem(ModBlocks.MUSHROOM_SUPPORT.get(), new Item.Properties()));
    public static final RegistryObject<Item> WOODEN_SUPPORT = ExampleMod.ITEMS.register("wooden_support", () -> new BlockItem(ModBlocks.WOODEN_SUPPORT.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROOTED_SOIL = ExampleMod.ITEMS.register("rooted_soil", () -> new BlockItem(ModBlocks.ROOTED_SOIL.get(), new Item.Properties()));

    private ModItems() {
    }

    public static void bootstrap() {
        GRAVE.getId();
        MUSHROOM_SUPPORT.getId();
        WOODEN_SUPPORT.getId();
        ROOTED_SOIL.getId();
    }
}
