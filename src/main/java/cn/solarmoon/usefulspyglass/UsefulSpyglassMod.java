package cn.solarmoon.usefulspyglass;

import cn.solarmoon.usefulspyglass.events.SpyglassHandler;
import cn.solarmoon.usefulspyglass.network.PacketRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("usefulspyglass")
public class UsefulSpyglassMod {
    public static final String MOD_ID = "usefulspyglass";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation id(String path, String... args) {
        return new ResourceLocation(MOD_ID, String.format(path, (Object[]) args));
    }

    public UsefulSpyglassMod() {
        //数据包
        PacketRegister packetRegister = new PacketRegister();
        packetRegister.register();

        //非静态事件处理器
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());
    }
}


