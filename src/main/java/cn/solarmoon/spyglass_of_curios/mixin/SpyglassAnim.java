package cn.solarmoon.spyglass_of_curios.mixin;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class SpyglassAnim {

    /**
     * 当按下按键就举起手来！
     */
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if (player instanceof ISpyUser sp) {
            if (sp.usingSpyglassInCurio()) {
                if (hand.equals(InteractionHand.OFF_HAND)) cir.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                if (hand.equals(InteractionHand.MAIN_HAND)) cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
            }
        }
    }

}
