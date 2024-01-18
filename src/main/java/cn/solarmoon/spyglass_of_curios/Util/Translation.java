package cn.solarmoon.spyglass_of_curios.Util;

import cn.solarmoon.spyglass_of_curios.SpyglassOfCurios;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class Translation {

    public static Component translation(String string1, Object... objects) {
        return new TranslatableComponent(string1 + "." + SpyglassOfCurios.MOD_ID, objects);
    }

    public static Component translation(String string1, String string2, Object... objects) {
        return new TranslatableComponent(string1 + "." + SpyglassOfCurios.MOD_ID + "." + string2, objects);
    }

}
