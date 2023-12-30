package cn.solarmoon.spyglass_of_curios;

import cn.solarmoon.spyglass_of_curios.Init.Config;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;

@Mod(MOD_ID)
public class SpyglassOfCurios {
    public static final String MOD_ID = "spyglass_of_curios";

    public SpyglassOfCurios() {

        Config.register();

    }

}


