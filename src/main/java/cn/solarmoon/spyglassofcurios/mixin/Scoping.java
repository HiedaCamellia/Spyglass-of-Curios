package cn.solarmoon.spyglassofcurios.mixin;

import cn.solarmoon.spyglassofcurios.Client.Method.FindSpyglassInCurio;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cn.solarmoon.spyglassofcurios.Client.Constants.mc;
import static cn.solarmoon.spyglassofcurios.Client.Constants.pressCheck;
import static cn.solarmoon.spyglassofcurios.Client.RegisterClient.useSpyglass;

@Mixin(Player.class)
public class Scoping{
    @Inject(method = "isScoping",at=@At(value = "RETURN"),cancellable = true)
    public void onScoping(CallbackInfoReturnable<Boolean> cir){
        FindSpyglassInCurio curioFinder = new FindSpyglassInCurio();
        boolean hasSpyglass = curioFinder.hasSpyglass(mc.player);
        if (useSpyglass.isDown() && pressCheck && hasSpyglass){
            cir.setReturnValue(true);
        }
    }
}
