package cn.solarmoon.spyglassofcurios.events;

import cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.TranslatableFormatException;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class SpyglassHandler {

    private final PacketRegister packetRegister = new PacketRegister();
    public boolean pressCheck = false;

    //按键绑定
    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;
        if (SpyglassOfCuriosClient.useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {
            //发包
            packetRegister.sendPacket(player, "spyglassUse");
            //使用望远镜
            if (player.getOffhandItem().is(Items.SPYGLASS)) {
                client.gameMode.useItem(player, InteractionHand.OFF_HAND);
            }
            //按键检查
            pressCheck = true;
        }
        if (!SpyglassOfCuriosClient.useSpyglass.isDown() && pressCheck) {
            //发包
            packetRegister.sendPacket(player,"spyglassStop");
            //重置按键检查
            pressCheck = false;
        }
    }

    //防副手逻辑混乱
    @SubscribeEvent
    public void exchangeCheck(LivingSwapItemsEvent.Hands event) {
        Player player = Minecraft.getInstance().player;
        if (pressCheck && player != null) {
            packetRegister.sendPacket(player, "spyglassExchange");
        }
    }

    //根据望远镜NBT显示倍率
    @SubscribeEvent
    public void spyglassTooltip(ItemTooltipEvent event) {
        ItemStack spyglass = event.getItemStack();
        if (spyglass.getItem() == Items.SPYGLASS && spyglass.hasTag()) {
            CompoundTag tag = spyglass.getTag();
            if (tag.contains("MULTIPLIER")) {
                double multiplier = tag.getDouble("MULTIPLIER");
                Component tooltip = Component.translatable("tooltip.spyglassofcurios.multiplier", "\u00A77\u00A7o" + multiplier);
                event.getToolTip().add(tooltip);
            }
        } else if (spyglass.getItem() == Items.SPYGLASS) {
            Component tooltip = Component.translatable("tooltip.spyglassofcurios.default_multiplier");
            event.getToolTip().add(tooltip);
        }
    }

    //滚轮调焦
    @SubscribeEvent
    public void onFovModifier(FovEvent event){
        event.setNewFov((float) SpyglassOfCuriosClient.MULTIPLIER);
    }
    @SubscribeEvent
    public void onUse(LivingEntityUseItemEvent event) {
        Minecraft client = Minecraft.getInstance();
        if (event.getItem().is(Items.SPYGLASS) && event.getEntity() != null && event.getEntity().getUseItem().hasTag() && client.options.getCameraType().isFirstPerson()) {
            SpyglassOfCuriosClient.MULTIPLIER = 2 - event.getEntity().getUseItem().getTag().getDouble("MULTIPLIER");
        }
        if (event.getItem().is(Items.SPYGLASS) && event.getEntity() != null && !event.getEntity().getUseItem().hasTag() && client.options.getCameraType().isFirstPerson()) {
            SpyglassOfCuriosClient.MULTIPLIER = 0.1;
        }
    }
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event){
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if(player != null && player.isScoping() && player.getUseItem().hasTag() && client.options.getCameraType().isFirstPerson()){
            //调整倍率
            double newMultiplier = Mth.clamp(2 - player.getUseItem().getTag().getDouble("MULTIPLIER")-(event.getScrollDelta()/10), .1,.8);
            SpyglassOfCuriosClient.MULTIPLIER = newMultiplier;
            player.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0f, (float)(1.0f+(1*(1- SpyglassOfCuriosClient.MULTIPLIER)*(1- SpyglassOfCuriosClient.MULTIPLIER))));

            //发包(把倍率存入独立的望远镜NBT)
            packetRegister.sendPacket(player, "spyglassPutNBT");

            //防止滚轮触发别的操作
            event.setCanceled(true);
        }
    }

}




