package com.example.examplemod.content;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.entity.ModEntities;
import com.example.examplemod.content.entity.NautilusEntity;
import com.example.examplemod.content.entity.ShellEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ShellEvents {
    private ShellEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        BlockHitResult hit = event.getHitVec();
        if (stack.is(ModItems.SHELL.get())) {
            if (level instanceof ServerLevel sl) {
                var blockPos = hit.getBlockPos().above();
                ShellEntity shell = ModEntities.SHELL.get().create(sl);
                if (shell != null) {
                    shell.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                    sl.addFreshEntity(shell);
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof ShellEntity shell && event.getHand() == InteractionHand.MAIN_HAND) {
            ItemStack stack = event.getItemStack();
            if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) {
                Level level = shell.level();
                if (level instanceof ServerLevel sl) {
                    NautilusEntity nautilus = ModEntities.NAUTILUS.get().create(sl);
                    if (nautilus != null) {
                        nautilus.setPos(shell.getX(), shell.getY(), shell.getZ());
                        sl.addFreshEntity(nautilus);
                        shell.discard();
                        if (!event.getEntity().getAbilities().instabuild) {
                            stack.shrink(1);
                            event.getEntity().getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                        }
                    }
                }
                event.setCanceled(true);
            }
        }
    }
}
