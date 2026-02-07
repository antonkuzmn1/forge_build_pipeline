package com.example.examplemod.content.item;

import com.example.examplemod.content.entity.LarvaEntity;
import com.example.examplemod.content.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class LarvaBucketItem extends Item {
    public LarvaBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).is(Blocks.BONE_BLOCK)) return InteractionResult.PASS;
        if (level instanceof ServerLevel sl) {
            LarvaEntity larva = ModEntities.LARVA.get().create(sl);
            if (larva != null) {
                larva.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                sl.addFreshEntity(larva);
                Player player = context.getPlayer();
                if (player != null && !player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                    player.getInventory().add(new ItemStack(Items.BUCKET));
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
