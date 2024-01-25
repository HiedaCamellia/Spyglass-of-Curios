package cn.solarmoon.spyglass_of_curios.client.event;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.init.Config;
import cn.solarmoon.spyglass_of_curios.network.PacketRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;

import static cn.solarmoon.spyglass_of_curios.util.client.Constants.renderType;
import static cn.solarmoon.spyglass_of_curios.util.TextUtil.translation;

public class SelectRender {
    //切换渲染模式
    @SubscribeEvent
    public void setRenderType(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ISpyUser sp = (ISpyUser) player;
        if(Config.disableRenderAll.get()) return;
        if (player != null && event.getItemStack().is(Items.SPYGLASS) && player.isCrouching()) {
            String[] renderTypes = {"back_waist", "head", "indescribable"};
            Boolean[] disableRenders = {Config.disableRenderBackWaist.get(), Config.disableRenderHead.get(), Config.disableRenderIndescribable.get()};
            if (Arrays.stream(disableRenders).allMatch(Boolean::booleanValue)) return;
            int index = Arrays.asList(renderTypes).indexOf(renderType);
            do {
                index = (index + 1) % renderTypes.length;
            } while (disableRenders[index]);
            renderType = renderTypes[index];
            PacketRegister.sendPacket(sp.multiplier(), renderType, "spyglassPutNBTRender");
            player.displayClientMessage(translation("switch", renderType), true);
        }
    }
}
