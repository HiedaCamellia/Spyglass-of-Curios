package cn.solarmoon.spyglassofcurios.mixin;

import cn.solarmoon.spyglassofcurios.Client.Method.FovEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class Fov {
    @Inject(method = "getFieldOfViewModifier",at=@At(value = "RETURN"),cancellable = true)
    public void onScopingFov(CallbackInfoReturnable<Float> cir){
        Minecraft client = Minecraft.getInstance();
        if (null != client.player && client.player.isScoping() && client.options.getCameraType().isFirstPerson()){
            //需在此处将FovEvent调用到事件总线上以能被处理
            FovEvent FovEvent = new FovEvent(cir.getReturnValue());
            MinecraftForge.EVENT_BUS.post(FovEvent);
            cir.setReturnValue((float)FovEvent.getNewFov());
        }
    }
}
