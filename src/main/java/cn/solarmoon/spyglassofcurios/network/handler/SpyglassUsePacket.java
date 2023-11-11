package cn.solarmoon.spyglassofcurios.network.handler;

import cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.*;


public class SpyglassUsePacket {

    public String spyglassHandle;

    public SpyglassUsePacket(String spyglassHandle) {
        this.spyglassHandle = spyglassHandle;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(spyglassHandle);
    }

    public static SpyglassUsePacket decode(FriendlyByteBuf buf) {
        String spyglassHandle = buf.readUtf(32767);
        return new SpyglassUsePacket(spyglassHandle);
    }


    public void handle(SpyglassUsePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            switch (packet.spyglassHandle) {
                case "spyglassUse" ->
                        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.findCurio("spyglass", 0).ifPresent(e -> {
                            spyglass = e.stack().copyAndClear();
                            offhandItem = player.getOffhandItem().copyAndClear();
                            player.setItemInHand(InteractionHand.OFF_HAND, spyglass);
                        }));
                case "spyglassStop" -> {
                    CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                        if (!spyglass.isEmpty()) {
                            handler.setEquippedCurio("spyglass", 0, spyglass);
                            player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                        } else if (spyglass.isEmpty() && player.getOffhandItem().is(Items.SPYGLASS)) {
                            spyglass = player.getOffhandItem().copyAndClear();
                            handler.setEquippedCurio("spyglass", 0, spyglass);
                        }
                    });
                    spyglass = ItemStack.EMPTY;
                    offhandItem = ItemStack.EMPTY;
                }
                case "spyglassExchange" -> {
                    CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                        if (!spyglass.isEmpty()) {
                            if (offhandItem == ItemStack.EMPTY) {
                                player.setItemInHand(InteractionHand.MAIN_HAND, spyglass);
                            } else {
                                handler.setEquippedCurio("spyglass", 0, spyglass);
                                player.setItemInHand(InteractionHand.MAIN_HAND, offhandItem);
                            }
                        }
                    });
                    spyglass = ItemStack.EMPTY;
                    offhandItem = ItemStack.EMPTY;
                }
                case "spyglassPutNBT" -> {
                    ItemStack spyglass = player.getUseItem();
                    if (spyglass.is(Items.SPYGLASS)) {
                        CompoundTag tag = spyglass.getOrCreateTag();
                        double newMultiplier = 10 - SpyglassOfCuriosClient.MULTIPLIER*10;
                        tag.putDouble("MULTIPLIER", newMultiplier);
                    }
                }
                case "spyglassPutNBTRender" -> {
                    ItemStack spyglass = player.getMainHandItem();
                    if (spyglass.is(Items.SPYGLASS)) {
                        CompoundTag tag = spyglass.getOrCreateTag();
                        tag.putString("renderType", renderType);
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}