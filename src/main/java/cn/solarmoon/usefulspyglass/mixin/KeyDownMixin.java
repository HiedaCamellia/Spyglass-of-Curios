package cn.solarmoon.usefulspyglass.mixin;

import cn.solarmoon.usefulspyglass.client.usefulspyglassCilent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class KeyDownMixin {
    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2))
    public boolean handleInput(KeyMapping instance){
        return instance.isDown() || usefulspyglassCilent.useSpyglass.isDown();
    }
}
