package cn.solarmoon.spyglass_of_curios.Mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cn.solarmoon.spyglass_of_curios.Common.Items.RegisterItems.useSpyglass;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.usingInCurio;

@Mixin(Player.class)
public class Scoping{
    @Inject(method = "isScoping",at=@At(value = "RETURN"),cancellable = true)
    public void onScoping(CallbackInfoReturnable<Boolean> cir){
        if (useSpyglass.isDown() && usingInCurio){
            cir.setReturnValue(true);
        }
    }
}
