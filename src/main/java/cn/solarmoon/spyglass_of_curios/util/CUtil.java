package cn.solarmoon.spyglass_of_curios.util;

import cn.solarmoon.spyglass_of_curios.init.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CUtil {
    public static void deBug(String string) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || !Config.deBug.get()) return;
        mc.player.sendSystemMessage(Component.literal("[§6SOC§f] "  + string));
    }
}
