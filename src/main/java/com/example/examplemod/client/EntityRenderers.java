package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderers {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        if (ModEntities.NAUTILUS.isPresent()) {
            event.registerEntityRenderer(ModEntities.NAUTILUS.get(), EmptyRenderer::new);
        }
        if (ModEntities.SHELL.isPresent()) {
            event.registerEntityRenderer(ModEntities.SHELL.get(), EmptyRenderer::new);
        }
        if (ModEntities.LARVA.isPresent()) {
            event.registerEntityRenderer(ModEntities.LARVA.get(), EmptyRenderer::new);
        }
    }

    private static class EmptyRenderer extends net.minecraft.client.renderer.entity.EntityRenderer<Entity> {
        protected EmptyRenderer(EntityRendererProvider.Context ctx) {
            super(ctx);
        }
        @Override
        public void render(Entity entity, float f1, float f2, com.mojang.blaze3d.vertex.PoseStack stack, net.minecraft.client.renderer.MultiBufferSource buf, int light) {}
        @Override
        public ResourceLocation getTextureLocation(Entity entity) {
            return new ResourceLocation("minecraft", "textures/entity/steve.png");
        }
    }
}
