package cn.solarmoon.spyglassofcurios.mixin;

import cn.solarmoon.spyglassofcurios.Client.Method.FindSpyglassInHand;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cn.solarmoon.spyglassofcurios.Client.Constants.*;
import static cn.solarmoon.spyglassofcurios.Client.RegisterClient.useSpyglass;

@Mixin(PlayerRenderer.class)
public abstract class SpyglassAnim extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public SpyglassAnim(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }

    //当按下按键，手中没有望远镜，就举起手来！ check：按键按下并手中没有望远镜
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {

        if (mc.player != null && hand.equals(InteractionHand.OFF_HAND)) {
            FindSpyglassInHand finder = new FindSpyglassInHand();
            finder.findSpyglassInHand();
            ItemStack spyglass = finder.getSpyglass();

            if(!spyglass.is(Items.SPYGLASS) && pressCheck && useSpyglass.isDown()) {
                cir.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                check = true;
            } else {
                check = false;
            }
        }
    }

}
