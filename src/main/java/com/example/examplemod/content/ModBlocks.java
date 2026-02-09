package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.MushroomSupportBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);

    public static final RegistryObject<Block> MUSHROOM_SUPPORT = BLOCKS.register("mushroom_support",
            () -> new MushroomSupportBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2f, 0.2f)));

    public static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
    }
}
