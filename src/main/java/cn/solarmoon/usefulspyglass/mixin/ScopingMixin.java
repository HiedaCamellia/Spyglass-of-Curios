package cn.solarmoon.usefulspyglass.mixin;

import cn.solarmoon.usefulspyglass.events.SpyglassHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public class ScopingMixin {
    @Inject(method = "isScoping", at=@At("RETURN"),cancellable = true)
    public void spyglassUsing(CallbackInfoReturnable<Boolean> isScoping){
        isScoping.setReturnValue(isScoping.getReturnValue() || SpyglassHandler.spyglassInUse);
    }
}

