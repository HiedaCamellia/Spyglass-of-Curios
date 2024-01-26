package cn.solarmoon.spyglass_of_curios.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketRegister {

    private static SimpleChannel instance;
    private int packetID = 0;

    private int id() {
        return packetID++;
    }

    public void register() {

        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();

        instance = network;

        network.messageBuilder(SpyglassUsePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpyglassUsePacket::decode)
                .encoder(SpyglassUsePacket::encode)
                .consumerMainThread(SpyglassUsePacket::handle)
                .add();
    }

    public static void sendPacket(double multiplier, String renderType, String handle) {
        instance.sendToServer(new SpyglassUsePacket(multiplier, renderType, false, handle));
    }

    public static void sendPacket(boolean flag ,String handle) {
        instance.sendToServer(new SpyglassUsePacket(0, "", flag, handle));
    }

    public static void sendPacket(String handle) {
        instance.sendToServer(new SpyglassUsePacket(0, "", false, handle));
    }

    //注册数据包
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        PacketRegister dataPackRegister = new PacketRegister();
        dataPackRegister.register();
    }

}