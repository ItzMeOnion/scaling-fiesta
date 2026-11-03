package com.example.butterflyknife.mixin;

import com.example.butterflyknife.ButterflyKnifeClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class ButterflyKnifeHeldItemMixin {

    /**
     * Before the held item is drawn, push a matrix and, if a spin is active,
     * rotate it around the hand. The matrix is popped after the item is drawn.
     */
    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD")
    )
    private void butterflyknife$applySpin(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode,
                                          boolean leftHanded, MatrixStack matrices,
                                          VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        matrices.push();

        if (leftHanded || renderMode != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) {
            return;
        }
        if (stack.isEmpty() || !ButterflyKnifeClient.isSpinning()) {
            return;
        }

        float t = ButterflyKnifeClient.spinProgress();
        float eased = 1f - (1f - t) * (1f - t) * (1f - t); // ease-out cubic

        float spin = eased * 1080f;                          // three full turns
        float flip = (float) Math.sin(t * Math.PI) * 30f;    // tilt up and back down

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spin));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(flip));
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN")
    )
    private void butterflyknife$restoreMatrix(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode,
                                              boolean leftHanded, MatrixStack matrices,
                                              VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        matrices.pop();
    }
}
