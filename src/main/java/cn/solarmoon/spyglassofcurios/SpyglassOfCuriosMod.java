package cn.solarmoon.spyglassofcurios;

import cn.solarmoon.spyglassofcurios.Client.RegisterClient;
import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod("spyglassofcurios")
public class SpyglassOfCuriosMod {
    public static final String MOD_ID = "spyglassofcurios";

    public SpyglassOfCuriosMod() {

        RegisterConfig.register();

        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(RegisterClient::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::SetupRes);
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(
                CuriosApi.MODID,
                SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("spyglass").size(1).icon(new ResourceLocation("spyglassofcurios", "slot/spyglass")).build()
        );
    }

    @SubscribeEvent
    public void SetupRes(TextureStitchEvent.Pre evt) {
        evt.addSprite(new ResourceLocation("spyglassofcurios", "slot/spyglass"));
    }

}


