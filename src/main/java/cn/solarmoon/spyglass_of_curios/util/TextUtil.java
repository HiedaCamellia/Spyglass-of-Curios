package cn.solarmoon.spyglass_of_curios.util;

import cn.solarmoon.spyglass_of_curios.SpyglassOfCurios;
import net.minecraft.network.chat.Component;

public class TextUtil {

    public static Component translation(String string1, Object... objects) {
        return Component.translatable(string1 + "." + SpyglassOfCurios.MOD_ID, objects);
    }

    public static Component translation(String string1, String string2, Object... objects) {
        return Component.translatable(string1 + "." + SpyglassOfCurios.MOD_ID + "." + string2, objects);
    }

}
