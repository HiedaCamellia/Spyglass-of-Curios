package cn.solarmoon.spyglass_of_curios.Network;

import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.FindSpyglassInCurio;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
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
            Player player = context.getSender();
            Level level = Objects.requireNonNull(context.getSender()).level;
            double mul = packet.multiplier;
            if (player == null) return;
            switch (packet.Handle) {
                case "spyglassPutNBT" -> {
                    FindSpyglassInCurio curioFinder = new FindSpyglassInCurio();
                    boolean hasSpyglass = curioFinder.hasSpyglass(player);

                    ItemStack spyglass = ItemStack.EMPTY;
                    if (player.isUsingItem()) spyglass = player.getUseItem();
                    else if (!player.isUsingItem() && hasSpyglass) spyglass = curioFinder.getSpyglass(player);

                    if (spyglass.is(Items.SPYGLASS)) {
                        CompoundTag tag = spyglass.getOrCreateTag();
                        tag.putDouble("MULTIPLIER", mul);
                    }
                }
                case "spyglassPutNBTRender" -> {
                        ItemStack spyglass = player.getMainHandItem();
                        if (spyglass.is(Items.SPYGLASS)) {
                            CompoundTag tag = spyglass.getOrCreateTag();
                            tag.putString("renderType", packet.renderType);
                        }
                }
                case "playSound1" -> {
                    if(!level.isClientSide) level.playSound(null, player.getOnPos(), SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1f, 1f);
                }
                case "playSound2" -> {
                    if(!level.isClientSide) level.playSound(null, player.getOnPos(), SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, 1f, 1f);
                }
                case "playSound3" -> {
                    float volume = (float) (1.0f + (1 * (1 - mul) * (1 - mul)));
                    if(!level.isClientSide) level.playSound(null, player.getOnPos(), SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, volume, 1f);
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}