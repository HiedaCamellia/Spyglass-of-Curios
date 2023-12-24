package cn.solarmoon.spyglass_of_curios;

import cn.solarmoon.spyglass_of_curios.init.RegisterConfig;
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

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;

@Mod(MOD_ID)
public class SpyglassOfCurios {
    public static final String MOD_ID = "spyglass_of_curios";

    public SpyglassOfCurios() {

        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::SetupRes);
        }

        RegisterConfig.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(
                CuriosApi.MODID,
                SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("spyglass").size(1).icon(new ResourceLocation(MOD_ID, "slot/spyglass")).build()
        );
    }

    @SubscribeEvent
    public void SetupRes(TextureStitchEvent.Pre evt) {
        evt.addSprite(new ResourceLocation(MOD_ID, "slot/spyglass"));
    }

}


