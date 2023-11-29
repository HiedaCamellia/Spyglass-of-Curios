package cn.solarmoon.spyglassofcurios.network.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;


public record SpyglassUsePacket(double multiplier, String renderType, String Handle) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(Handle);
        buf.writeDouble(multiplier);
        buf.writeUtf(renderType);
    }

    public static SpyglassUsePacket decode(FriendlyByteBuf buf) {
        String Handle = buf.readUtf(32767);
        double multiplier = buf.readDouble();
        String renderType = buf.readUtf();
        return new SpyglassUsePacket(multiplier, renderType, Handle);
    }

    public static void handle(SpyglassUsePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            switch (packet.Handle) {
                case "spyglassPutNBT" -> {
                    ItemStack spyglass = ItemStack.EMPTY;
                    if (player.isUsingItem()) spyglass = player.getUseItem();
                    else if (CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).isPresent() && !player.isUsingItem())
                        spyglass = CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).get().stack();
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