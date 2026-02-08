package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.RootedSoilBlock;
import com.example.examplemod.content.block.ShellBlock;
import com.example.examplemod.content.block.SupportBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final RegistryObject<Block> GRAVE = ExampleMod.BLOCKS.register("grave", () -> new GraveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> MUSHROOM_SUPPORT = ExampleMod.BLOCKS.register("mushroom_support", () -> new SupportBlock(SupportBlock.SupportType.MUSHROOM, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1.0F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> WOODEN_SUPPORT = ExampleMod.BLOCKS.register("wooden_support", () -> new SupportBlock(SupportBlock.SupportType.WOODEN, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> ROOTED_SOIL = ExampleMod.BLOCKS.register("rooted_soil", () -> new RootedSoilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F).sound(SoundType.ROOTED_DIRT).randomTicks()));
    public static final RegistryObject<Block> SHELL_BLOCK = ExampleMod.BLOCKS.register("shell_block", () -> new ShellBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0F).sound(SoundType.WOOD)));

    private ModBlocks() {
    }

    public static void bootstrap() {
        GRAVE.getId();
        MUSHROOM_SUPPORT.getId();
        WOODEN_SUPPORT.getId();
        ROOTED_SOIL.getId();
    }
}
