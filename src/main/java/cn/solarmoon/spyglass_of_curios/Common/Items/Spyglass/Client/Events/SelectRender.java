package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events;

import cn.solarmoon.spyglass_of_curios.Init.RegisterConfig;
import cn.solarmoon.spyglass_of_curios.Network.PacketRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.*;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.renderType;
import static cn.solarmoon.spyglass_of_curios.Util.Translation.translation;

public class SelectRender {
    //切换渲染模式
    @SubscribeEvent
    public void setRenderType(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = mc.player;
        if(RegisterConfig.disableRenderAll.get()) return;
        if (player != null && event.getItemStack().is(Items.SPYGLASS) && player.isCrouching()) {
            String[] renderTypes = {"back_waist", "head", "indescribable"};
            Boolean[] disableRenders = {RegisterConfig.disableRenderBackWaist.get(), RegisterConfig.disableRenderHead.get(), RegisterConfig.disableRenderIndescribable.get()};
            if (Arrays.stream(disableRenders).allMatch(Boolean::booleanValue)) return;
            int index = Arrays.asList(renderTypes).indexOf(renderType);
            do {
                index = (index + 1) % renderTypes.length;
            } while (disableRenders[index]);
            renderType = renderTypes[index];
            PacketRegister.sendPacket(MULTIPLIER, renderType, "spyglassPutNBTRender");
            player.displayClientMessage(translation("switch", renderType), true);
        }
    }
}
