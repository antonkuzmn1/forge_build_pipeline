package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final RegistryObject<Item> MUSHROOM_SUPPORT_ITEM = ITEMS.register("mushroom_support",
            () -> new BlockItem(ModBlocks.MUSHROOM_SUPPORT.get(), new Item.Properties()));

    public static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
