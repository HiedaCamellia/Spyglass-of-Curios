package cn.solarmoon.spyglassofcurios.network;

import cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod;
import cn.solarmoon.spyglassofcurios.network.handler.SpyglassUsePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;


public class PacketRegister {

    private static SimpleChannel instance;
    private int packetID = 0;

    private int id() {
        return packetID++;
    }

    public void register() {

        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SpyglassOfCuriosMod.MOD_ID, "main"))
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

    public static void sendPacket(ItemStack spyglass, ItemStack offhandItem, ItemStack mainhandItem, boolean doubleSwap, double multiplier, String renderType, String handle) {
        instance.sendToServer(new SpyglassUsePacket(spyglass, offhandItem, mainhandItem, doubleSwap, multiplier, renderType, handle));
    }

}