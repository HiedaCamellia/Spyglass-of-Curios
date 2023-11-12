package cn.solarmoon.spyglassofcurios.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;


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
        matrixStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LivingEntity living = slotContext.entity();
        if (stack.getTag() != null) {
            if (stack.getTag().contains("renderType")) {
                nbt = stack.getTag().getString("renderType");
            } else {
                nbt = "back_waist";
            }
        } else {
            nbt = "back_waist";
        }
        if ("back_waist".equals(nbt)) {
            if(living.isCrouching()) {
                matrixStack.translate(0.0F, 0.14F, 0.3F);
            }
            matrixStack.translate(.15, 0.6, 0.2);
            matrixStack.mulPose(Direction.DOWN.getRotation());
            matrixStack.scale(0.7f, 0.7f, 0.7f);
        }
        if ("head".equals(nbt)) {
            if(living.isCrouching()) {
                matrixStack.translate(0.0F, 0.26F, 0F);
            }
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(HeadYaw));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(headPitch));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(-0.1, 0.21, -0.6);
            matrixStack.mulPose(Direction.SOUTH.getRotation());
            matrixStack.scale(1f, 1f, 1f);
        }
        if ("indescribable".equals(nbt)) {
            if(living.isCrouching()) {
                matrixStack.translate(0.0F, 0.14F, 0.3F);
            }
            matrixStack.translate(0, -1, 0);
            matrixStack.mulPose(Direction.DOWN.getRotation());
            matrixStack.scale(7f, 7f, 7f);
        }

        BakedModel spyglass = itemRenderer.getModel(Items.SPYGLASS.getDefaultInstance(), null, null, 1);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        itemRenderer.render(stack, ItemTransforms.TransformType.NONE, true, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, spyglass);
        matrixStack.popPose();
    }

}
