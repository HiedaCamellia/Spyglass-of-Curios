package cn.solarmoon.spyglassofcurios;

import cn.solarmoon.spyglassofcurios.Client.RegisterClient;
import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("spyglassofcurios")
public class SpyglassOfCuriosMod {
    public static final String MOD_ID = "spyglassofcurios";

    public SpyglassOfCuriosMod() {

        RegisterConfig.register();

        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(RegisterClient::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(RegisterClient::onRegisterKeyBinds);
        }

    }

}


