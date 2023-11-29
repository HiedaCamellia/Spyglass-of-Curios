package cn.solarmoon.spyglassofcurios.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.solarmoon.spyglassofcurios.Client.RegisterClient.useSpyglass;

//让望远镜能够持续使用
@Mixin(Minecraft.class)
public class KeyDown {
    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2), cancellable = true)
    public void handleInput(CallbackInfo ci){
        if (useSpyglass.isDown()) {
            ci.cancel();
        }
    }
}
