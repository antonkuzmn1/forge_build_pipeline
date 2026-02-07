package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.item.LarvaBucketItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final RegistryObject<Item> GRAVE = ExampleMod.ITEMS.register("grave", () -> new BlockItem(ModBlocks.GRAVE.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUSHROOM_SUPPORT = ExampleMod.ITEMS.register("mushroom_support", () -> new BlockItem(ModBlocks.MUSHROOM_SUPPORT.get(), new Item.Properties()));
    public static final RegistryObject<Item> WOODEN_SUPPORT = ExampleMod.ITEMS.register("wooden_support", () -> new BlockItem(ModBlocks.WOODEN_SUPPORT.get(), new Item.Properties()));

    public static final RegistryObject<Item> SHELL = ExampleMod.ITEMS.register("shell", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHELL_BLOCK = ExampleMod.ITEMS.register("shell_block", () -> new BlockItem(ModBlocks.SHELL_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> LARVA_BUCKET = ExampleMod.ITEMS.register("larva_bucket", () -> new LarvaBucketItem(new Item.Properties().stacksTo(1)));

    private ModItems() {
    }

    public static void bootstrap() {
        GRAVE.getId();
        MUSHROOM_SUPPORT.getId();
        WOODEN_SUPPORT.getId();
        SHELL.getId();
        SHELL_BLOCK.getId();
        LARVA_BUCKET.getId();
    }
}
