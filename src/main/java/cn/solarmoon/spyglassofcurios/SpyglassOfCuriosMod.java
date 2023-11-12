package cn.solarmoon.spyglassofcurios;

import cn.solarmoon.spyglassofcurios.events.SpyglassHandler;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod("spyglassofcurios")
public class SpyglassOfCuriosMod {
    public static final String MOD_ID = "spyglassofcurios";

    public SpyglassOfCuriosMod() {
        //数据包
        PacketRegister packetRegister = new PacketRegister();
        packetRegister.register();

        //非静态事件处理器
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);


    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(
                CuriosApi.MODID,
                SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("spyglass").size(1).icon(new ResourceLocation("spyglassofcurios", "slot/spyglass")).build()
        );
    }

}


