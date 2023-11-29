package cn.solarmoon.spyglassofcurios.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

import static cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod.useSpyglass;
import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.*;

@Mixin(ItemInHandLayer.class)
public abstract class RenderItem<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {

    public RenderItem(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Shadow protected abstract void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemTransforms.TransformType p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_);

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T t, float p1, float p2, float p3, float p4, float p5, float p6, CallbackInfo ci) {
        if (useSpyglass.isDown() && pressCheck && !t.isUsingItem() && check) {
            if (mc.player != null) {
                CuriosApi.getCuriosHelper().findCurio(mc.player, "spyglass", 0).ifPresent(slotResult -> {
                    if(!t.getMainHandItem().isEmpty()) {
                        this.renderArmWithItem(t, t.getMainHandItem(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, multiBufferSource, i);
                    }
                    ci.cancel();
                });
            }
        }
    }

}

