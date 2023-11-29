package cn.solarmoon.spyglassofcurios.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import static cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod.useSpyglass;
import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.mc;
import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.pressCheck;

@Mixin(Player.class)
public class Scoping{
    @Inject(method = "isScoping",at=@At(value = "RETURN"),cancellable = true)
    public void onScoping(CallbackInfoReturnable<Boolean> cir){
        CuriosApi.getCuriosInventory(mc.player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findCurio("spyglass", 0).ifPresent(slotResult -> {
            if (useSpyglass.isDown() && pressCheck){
                cir.setReturnValue(true);
            }
        }));
    }
}
