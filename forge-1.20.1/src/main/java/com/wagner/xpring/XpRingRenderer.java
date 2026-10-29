package com.wagner.xpring;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class XpRingRenderer implements BlockEntityRenderer<XpRingBlockEntity> {
    private final ItemRenderer itemRenderer;

    public XpRingRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            XpRingBlockEntity ring,
            float partialTick,
            PoseStack pose,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        if (ring.getLevel() == null) {
            return;
        }

        float time = ring.getLevel().getGameTime() + partialTick;
        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        ItemStack stack = new ItemStack(XpRingMod.XP_RING.get());
        if (ring.getStoredLevels() > 0) {
            XpRingItem.setStoredLevels(stack, ring.getStoredLevels());
        }

        pose.pushPose();
        pose.translate(0.5, 0.62 + Mth.sin(time / 10.0F) * 0.06, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        pose.mulPose(Axis.ZP.rotationDegrees(Mth.sin(time / 24.0F) * 8.0F));
        pose.scale(0.55F, 0.55F, 0.55F);
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GUI,
                packedLight,
                packedOverlay,
                pose,
                buffer,
                ring.getLevel(),
                0
        );
        pose.popPose();
    }
}
