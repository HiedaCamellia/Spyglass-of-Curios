package cn.solarmoon.spyglassofcurios.Client.Renderer;

import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import static cn.solarmoon.spyglassofcurios.Client.Constants.check;
import static cn.solarmoon.spyglassofcurios.Client.Constants.pressCheck;
import static cn.solarmoon.spyglassofcurios.Client.RegisterClient.useSpyglass;


public class SpyglassRenderer implements ICurioRenderer {

    public String nbt = "back_waist";

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float HeadYaw,
            float headPitch
    ) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LivingEntity living = slotContext.entity();
        if(useSpyglass.isDown() && pressCheck && !living.isUsingItem() && check){
            matrixStack.pushPose();
            if (living.isCrouching()) {
                matrixStack.translate(0.0F, 0.26F, 0F);
            }
            matrixStack.mulPose(Axis.YP.rotationDegrees(HeadYaw));
            matrixStack.mulPose(Axis.XP.rotationDegrees(headPitch));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.translate(-0.1, 0.21, -0.6);
            matrixStack.mulPose(Direction.SOUTH.getRotation());
            matrixStack.scale(1f, 1f, 1f);
            BakedModel spyglass = itemRenderer.getModel(Items.SPYGLASS.getDefaultInstance(), null, null, 1);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            itemRenderer.render(stack, ItemDisplayContext.NONE, true, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, spyglass);
            matrixStack.popPose();
        }
        if(RegisterConfig.disableRenderAll.get() || check) return;
        matrixStack.pushPose();
        if (stack.getTag() != null) {
            if (stack.getTag().contains("renderType")) {
                nbt = stack.getTag().getString("renderType");
            } else {
                nbt = "back_waist";
            }
        } else {
            nbt = "back_waist";
        }
        switch (nbt) {
            case "back_waist" -> {
                if (!RegisterConfig.disableRenderBackWaist.get()) {
                    if (living.isCrouching()) {
                        matrixStack.translate(0.0F, 0.14F, 0.3F);
                    }
                    matrixStack.translate(.15, 0.6, 0.2);
                    matrixStack.mulPose(Direction.DOWN.getRotation());
                    matrixStack.scale(0.7f, 0.7f, 0.7f);
                }
            }
            case "head" -> {
                if (!RegisterConfig.disableRenderHead.get()){
                    if (living.isCrouching()) {
                        matrixStack.translate(0.0F, 0.26F, 0F);
                    }
                    matrixStack.mulPose(Axis.YP.rotationDegrees(HeadYaw));
                    matrixStack.mulPose(Axis.XP.rotationDegrees(headPitch));
                    matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    matrixStack.translate(-0.1, 0.21, -0.6);
                    matrixStack.mulPose(Direction.SOUTH.getRotation());
                    matrixStack.scale(1f, 1f, 1f);
                }
            }
            case "indescribable" -> {
                if (!RegisterConfig.disableRenderIndescribable.get()) {
                    if (living.isCrouching()) {
                        matrixStack.translate(0.0F, 0.14F, 0.3F);
                    }
                    matrixStack.translate(0, -1, 0);
                    matrixStack.mulPose(Direction.DOWN.getRotation());
                    matrixStack.scale(7f, 7f, 7f);
                }
            }
        }
        BakedModel spyglass = itemRenderer.getModel(Items.SPYGLASS.getDefaultInstance(), null, null, 1);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        itemRenderer.render(stack, ItemDisplayContext.NONE, true, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, spyglass);
        matrixStack.popPose();
    }

}
