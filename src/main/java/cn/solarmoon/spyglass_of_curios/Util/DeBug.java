package cn.solarmoon.spyglass_of_curios.Util;

import cn.solarmoon.spyglass_of_curios.Init.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.mc;

public class DeBug {
    public static void deBug(String string) {
        if(mc.player == null || !Config.deBug.get()) return;
        mc.player.sendMessage(Component.nullToEmpty("[§6SOC§f] "  + string), Player.createPlayerUUID("2"));
    }
}
