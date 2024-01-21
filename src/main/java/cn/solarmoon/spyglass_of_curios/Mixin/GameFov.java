package cn.solarmoon.spyglass_of_curios.Mixin;


import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.MULTIPLIER;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.mc;

@Mixin(GameRenderer.class)
public class GameFov {
    @Shadow
    private float fov;
    @Shadow
    private float oldFov;
    @Inject(at = @At("HEAD"), method = "tickFov", cancellable = true)
    public void setFov(CallbackInfo ci) {
        if (null != mc.player && mc.player.isScoping() && mc.options.getCameraType().isFirstPerson()) {
            float f = (float) MULTIPLIER;
            this.oldFov = this.fov;
            this.fov += (f - this.fov) * 0.5F;
            ci.cancel();
        }
    }
}