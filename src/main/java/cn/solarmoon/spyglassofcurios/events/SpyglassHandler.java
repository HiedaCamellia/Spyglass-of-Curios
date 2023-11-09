package cn.solarmoon.spyglassofcurios.events;

import cn.solarmoon.spyglassofcurios.client.spyglassOfCuriosClient;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class SpyglassHandler {

    private final PacketRegister packetRegister = new PacketRegister();
    public boolean pressCheck = false;

    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;
        if (spyglassOfCuriosClient.useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {
            //发包
            packetRegister.sendPacket(player, "spyglassUse");
            //使用望远镜
            if (player.getOffhandItem().is(Items.SPYGLASS)) {
                client.gameMode.useItem(player, InteractionHand.OFF_HAND);
            }
            //按键检查
            pressCheck = true;
        }
        if (!spyglassOfCuriosClient.useSpyglass.isDown() && pressCheck) {
            //发包
            packetRegister.sendPacket(player,"spyglassStop");
            //重置按键检查
            pressCheck = false;
        }
    }

    @SubscribeEvent
    public void exchangeCheck(LivingSwapItemsEvent.Hands event) {
        Player player = Minecraft.getInstance().player;
        if (pressCheck && player != null) {
                packetRegister.sendPacket(player, "spyglassExchange");
        }
    }


}




