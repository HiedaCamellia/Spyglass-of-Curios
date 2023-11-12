package cn.solarmoon.spyglassofcurios.mixin;

import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.pressCheck;

@Mixin(Player.class)
public class SwapMixin {
    @Inject(method = "setItemSlot", at=@At(value = "HEAD"), cancellable = true)
    public void onSwap(CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (pressCheck && player != null && player.isScoping() && player.getOffhandItem().is(Items.SPYGLASS)) {
            PacketRegister.sendPacket(player, "spyglassExchange");
        }
    }
}
