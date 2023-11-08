package cn.solarmoon.usefulspyglass.events;

import cn.solarmoon.usefulspyglass.mixin.ClickDelayMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import cn.solarmoon.usefulspyglass.client.usefulspyglassCilent;
import top.theillusivec4.curios.api.CuriosApi;


@Mod.EventBusSubscriber
public class SpyglassHandler {
    public static boolean spyglassusing = false;
    public static ItemStack spyglass = ItemStack.EMPTY;
    public static ItemStack offhanditem = ItemStack.EMPTY;
    @SubscribeEvent
    public static void SpyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        ClickDelayMixin accessor = (ClickDelayMixin) Minecraft.getInstance();
        int delay = accessor.getrightClickDelay();


        if (player == null) return;
        if (usefulspyglassCilent.useSpyglass.isDown() && delay == 0 && !player.isUsingItem()) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.findCurio("spyglass", 0).ifPresent(e -> {
                    spyglass = e.stack().copyAndClear();
                    offhanditem = player.getOffhandItem().copyAndClear();
                    player.setItemInHand(InteractionHand.OFF_HAND, spyglass);
                    client.gameMode.useItem(player, InteractionHand.OFF_HAND);
                    spyglassusing = true;
                });
            });
        }
        if (!usefulspyglassCilent.useSpyglass.isDown() && spyglassusing) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.setEquippedCurio("spyglass", 0, spyglass);
                player.setItemInHand(InteractionHand.OFF_HAND, offhanditem);
                spyglass = ItemStack.EMPTY;
                offhanditem = ItemStack.EMPTY;
                spyglassusing = false;
            });
        }

    }
}



