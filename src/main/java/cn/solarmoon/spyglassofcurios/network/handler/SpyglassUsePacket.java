package cn.solarmoon.spyglassofcurios.network.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;


public record SpyglassUsePacket(ItemStack spyglass, ItemStack offhandItem, ItemStack mainhandItem, boolean doubleSwap, double multiplier, String renderType, String Handle) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(Handle);
        buf.writeItemStack(spyglass, true);
        buf.writeItemStack(offhandItem, true);
        buf.writeItemStack(mainhandItem, true);
        buf.writeBoolean(doubleSwap);
        buf.writeDouble(multiplier);
        buf.writeUtf(renderType);
    }

    public static SpyglassUsePacket decode(FriendlyByteBuf buf) {
        String Handle = buf.readUtf(32767);
        ItemStack spyglass = buf.readItem();
        ItemStack offhandItem = buf.readItem();
        ItemStack mainhandItem = buf.readItem();
        boolean doubleSwap = buf.readBoolean();
        double multiplier = buf.readDouble();
        String renderType = buf.readUtf();
        return new SpyglassUsePacket(spyglass, offhandItem, mainhandItem, doubleSwap, multiplier, renderType, Handle);
    }

    public static void handle(SpyglassUsePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            switch (packet.Handle) {
                case "clear1" -> {
                    CuriosApi.getCuriosHelper().setEquippedCurio(player, "spyglass", 0, ItemStack.EMPTY);
                }
                case "clear2" -> {
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
                case "setHand1" -> {
                    ItemStack spyglass = packet.spyglass;
                    player.setItemInHand(InteractionHand.OFF_HAND, spyglass);
                }
                case "setCurio1" -> {
                    ItemStack spyglass = packet.spyglass;
                    CuriosApi.getCuriosHelper().setEquippedCurio(player, "spyglass", 0, spyglass);
                }
                case "setHand2" -> {
                    ItemStack offhandItem = packet.offhandItem;
                    player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                }
                case "Hand3" -> {
                    ItemStack offhandItem = packet.offhandItem;
                    ItemStack mainhandItem = packet.mainhandItem;
                    ItemStack spyglass = packet.spyglass;
                    player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                    player.setItemInHand(InteractionHand.MAIN_HAND, mainhandItem);
                    CuriosApi.getCuriosHelper().setEquippedCurio(player, "spyglass", 0, spyglass);
                }
                case "spyglassPutNBT" -> {
                    ItemStack spyglass = player.getUseItem();
                    if (spyglass.is(Items.SPYGLASS)) {
                        CompoundTag tag = spyglass.getOrCreateTag();
                        double newMultiplier = 10 - packet.multiplier*10;
                        tag.putDouble("MULTIPLIER", newMultiplier);
                    }
                }
                case "spyglassPutNBTRender" -> {
                    ItemStack spyglass = player.getMainHandItem();
                    if (spyglass.is(Items.SPYGLASS)) {
                        CompoundTag tag = spyglass.getOrCreateTag();
                        tag.putString("renderType", packet.renderType);
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}