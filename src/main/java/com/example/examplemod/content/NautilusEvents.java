package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.entity.ModEntities;
import com.example.examplemod.content.entity.NautilusEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NautilusEvents {
    private NautilusEvents() {}

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof NautilusEntity nautilus)) return;
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.HANGING_ROOTS)) return;
        Level level = nautilus.level();
        if (!(level instanceof ServerLevel sl)) return;
        var box = nautilus.getBoundingBox().inflate(8);
        NautilusEntity other = null;
        for (var e : level.getEntitiesOfClass(NautilusEntity.class, box)) {
            if (e != nautilus && e.isAlive()) {
                other = e;
                break;
            }
        }
        if (other != null) {
            NautilusEntity baby = ModEntities.NAUTILUS.get().create(sl);
            if (baby != null) {
                baby.setPos(nautilus.getX(), nautilus.getY(), nautilus.getZ());
                sl.addFreshEntity(baby);
                if (!event.getEntity().getAbilities().instabuild) stack.shrink(1);
            }
            event.setCanceled(true);
        }
    }
}
