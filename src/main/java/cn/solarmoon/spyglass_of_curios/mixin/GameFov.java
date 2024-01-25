package cn.solarmoon.spyglass_of_curios.mixin;


import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameFov {
    @Shadow
    private float fov;
    @Shadow
    private float oldFov;
    @Inject(at = @At("HEAD"), method = "tickFov", cancellable = true)
    public void setFov(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (null != mc.player && mc.player.isScoping() && mc.options.getCameraType().isFirstPerson()) {
            ISpyUser sp = (ISpyUser) mc.player;
            float f = (float) sp.multiplier();
            this.oldFov = this.fov;
            this.fov += (f - this.fov) * 0.5F;
            ci.cancel();
        }
    }
}
