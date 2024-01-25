package cn.solarmoon.spyglass_of_curios.client.curios_renderer;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.init.Config;
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
import net.minecraft.world.level.Level;
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
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        LivingEntity living = slotContext.entity();
        ISpyUser sp = (ISpyUser)living;
        Level level = living.level;
        BakedModel spyglass = itemRenderer.getModel(Items.SPYGLASS.getDefaultInstance(), level, living, 1);
        if (sp.usingSpyglassInCurio()) {
            if (living.isCrouching()) {
                matrixStack.translate(0.0F, 0.26F, 0F);
            }
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(HeadYaw));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(Math.max(headPitch, -30)));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(-0.1, 0.21, -0.6);
            matrixStack.mulPose(Direction.SOUTH.getRotation());
            matrixStack.scale(1f, 1f, 1f);
        }
        else if (!Config.disableRenderAll.get()) {
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
                    if (!Config.disableRenderBackWaist.get()) {
                        if (living.isCrouching()) {
                            matrixStack.translate(0.0F, 0.14F, 0.3F);
                        }
                        matrixStack.translate(.15, 0.6, 0.2);
                        matrixStack.mulPose(Direction.DOWN.getRotation());
                        matrixStack.scale(0.7f, 0.7f, 0.7f);
                    }
                }
                case "head" -> {
                    if (!Config.disableRenderHead.get()) {
                        if (living.isCrouching()) {
                            matrixStack.translate(0.0F, 0.26F, 0F);
                        }
                        matrixStack.mulPose(Vector3f.YP.rotationDegrees(HeadYaw));
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(headPitch));
                        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                        matrixStack.translate(-0.1, 0.21, -0.6);
                        matrixStack.mulPose(Direction.SOUTH.getRotation());
                        matrixStack.scale(1f, 1f, 1f);
                    }
                }
                case "indescribable" -> {
                    if (!Config.disableRenderIndescribable.get()) {
                        if (living.isCrouching()) {
                            matrixStack.translate(0.0F, 0.14F, 0.3F);
                        }
                        matrixStack.translate(0, -1, 0);
                        matrixStack.mulPose(Direction.DOWN.getRotation());
                        matrixStack.scale(7f, 7f, 7f);
                    }
                }
            }
        }
        itemRenderer.render(stack, ItemTransforms.TransformType.NONE, true, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, spyglass);
        matrixStack.popPose();
    }

}
