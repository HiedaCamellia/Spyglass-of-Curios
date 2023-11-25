package cn.solarmoon.spyglassofcurios.mixin;

import cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class KeyDownMixin {
    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2), cancellable = true)
    public void handleInput(CallbackInfo ci){
        if (SpyglassOfCuriosClient.useSpyglass.isDown()) {
            ci.cancel();
        }
    }
}

