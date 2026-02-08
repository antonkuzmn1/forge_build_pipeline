package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.entity.LarvaEntity;
import com.example.examplemod.content.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LarvaEvents {
    private static final double LARVA_DROP_CHANCE = 0.02;

    private LarvaEvents() {}

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!event.getState().is(Blocks.MELON)) return;
        if (level.getRandom().nextDouble() < LARVA_DROP_CHANCE) {
            var pos = event.getPos();
            LarvaEntity larva = ModEntities.LARVA.get().create(level);
            if (larva != null) {
                larva.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                level.addFreshEntity(larva);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LarvaEntity larva && event.getItemStack().is(Items.BUCKET)) {
            if (event.getLevel() instanceof ServerLevel sl) {
                larva.discard();
                if (!event.getEntity().getAbilities().instabuild) event.getItemStack().shrink(1);
                event.getEntity().getInventory().add(new ItemStack(ModItems.LARVA_BUCKET.get()));
            }
            event.setCanceled(true);
        }
    }
}
