package cn.solarmoon.spyglass_of_curios.Util;

import cn.solarmoon.spyglass_of_curios.Config.RegisterConfig;
import net.minecraft.network.chat.Component;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.mc;

public class DeBug {
    public static void deBug(String string) {
        if(mc.player == null || !RegisterConfig.deBug.get()) return;
        mc.player.sendSystemMessage(Component.literal("[§6SOC§f] "  + string));
    }
}
