package cn.solarmoon.spyglassofcurios;

import cn.solarmoon.spyglassofcurios.events.FovEvent;
import cn.solarmoon.spyglassofcurios.events.SpyglassHandler;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("spyglassofcurios")
public class SpyglassOfCuriosMod {
    public static final String MOD_ID = "spyglassofcurios";

    //模型材质
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public static ResourceLocation id(String path, String... args) {
        return new ResourceLocation(MOD_ID, String.format(path, (Object[]) args));
    }

    public SpyglassOfCuriosMod() {
        //数据包
        PacketRegister packetRegister = new PacketRegister();
        packetRegister.register();

        //非静态事件处理器
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());

    }
}


