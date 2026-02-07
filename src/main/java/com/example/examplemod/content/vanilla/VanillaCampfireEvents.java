// сып блоками
package com.example.examplemod.content.vanilla;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VanillaCampfireEvents {
    private VanillaCampfireEvents() {
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }

        if (!living.getMainHandItem().is(Items.CAMPFIRE) && !living.getOffhandItem().is(Items.CAMPFIRE)) {
            return;
        }

        BlockState state = event.getPlacedBlock();
        if (!state.is(Blocks.CAMPFIRE)) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState newState = state;
        if (newState.hasProperty(CampfireBlock.LIT)) {
            newState = newState.setValue(CampfireBlock.LIT, false);
        }

        if (newState != state) {
            level.setBlock(pos, newState, 3);
        }
    }
}
