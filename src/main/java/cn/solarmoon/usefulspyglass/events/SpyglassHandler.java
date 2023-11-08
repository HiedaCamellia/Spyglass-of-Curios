package cn.solarmoon.usefulspyglass.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import cn.solarmoon.usefulspyglass.client.usefulspyglassCilent;
import top.theillusivec4.curios.api.CuriosApi;


@Mod.EventBusSubscriber
public class SpyglassHandler {
    public static ItemStack spyglass = ItemStack.EMPTY;
    public static ItemStack offhanditem = ItemStack.EMPTY;
    public static boolean spyglassInUse = false;
    @SubscribeEvent
    public static void spyglassUse(InputEvent.Key event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;
        if (usefulspyglassCilent.useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping() && client.rightClickDelay == 0) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.findCurio("spyglass", 0).ifPresent(e -> {
                    spyglass = e.stack().copyAndClear();
                    offhanditem = player.getOffhandItem().copyAndClear();
                    player.setItemInHand(InteractionHand.OFF_HAND, spyglass);
                });
            });
            //按下右键
            player.startUsingItem(InteractionHand.OFF_HAND);
        }
        if (!usefulspyglassCilent.useSpyglass.isDown() && player.isScoping()) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.setEquippedCurio("spyglass", 0, spyglass);
                player.setItemInHand(InteractionHand.OFF_HAND, offhanditem);
            });
            spyglass = ItemStack.EMPTY;
            offhanditem = ItemStack.EMPTY;
        }

    }
}



