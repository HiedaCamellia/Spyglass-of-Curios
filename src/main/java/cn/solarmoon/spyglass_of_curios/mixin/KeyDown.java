package cn.solarmoon.spyglass_of_curios.mixin;

import cn.solarmoon.spyglass_of_curios.init.Keys;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//让望远镜能够持续使用
@Mixin(Minecraft.class)
public class KeyDown {
    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2), cancellable = true)
    public void handleInput(CallbackInfo ci){
        if (Keys.useSpyglass.isDown()) {
            ci.cancel();
        }
    }
}
