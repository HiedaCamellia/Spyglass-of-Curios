package cn.solarmoon.spyglassofcurios.events;

import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.Objects;

import static cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod.useSpyglass;
import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.*;


public class SpyglassHandler {

    //哼，想逃？
    @SubscribeEvent
    public void swapCheck(TickEvent.PlayerTickEvent event) {
        if (doubleSwap) {
            if (pressCheck && event.player.getMainHandItem().is(Items.SPYGLASS)) {
                PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "Hand3");//清除副手
            } else if (!pressCheck) {
                doubleSwap = false;
            }
        }
    }

    //按键绑定
    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;

        if (useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {

            if (player.getMainHandItem().is(Items.SPYGLASS)) {
                client.gameMode.useItem(player, InteractionHand.MAIN_HAND);
                return;
            }

            //发包
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.findCurio("spyglass", 0).ifPresent(e -> {
                if (e.stack().isEmpty()) return;
                if (player.getMainHandItem().is(Items.SPYGLASS)) return;
                if (Minecraft.getInstance().options.keySwapOffhand.isDown()) return;
                spyglass = e.stack().copy();
                PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "clear1");//清除饰品
                mainhandItem = player.getMainHandItem();
                offhandItem = player.getOffhandItem();
                PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "clear2");//清除副手
                PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setHand1");//副手上望远镜
            }));
            //使用望远镜
            client.gameMode.useItem(player, InteractionHand.OFF_HAND);
            //按键检查
            pressCheck = true;
        }
        if (!useSpyglass.isDown() && pressCheck) {
            //发包
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                if (!spyglass.isEmpty() && player.getOffhandItem().is(Items.SPYGLASS)) {
                    spyglass = player.getOffhandItem().copy();
                    PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setCurio1");
                    PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setHand2");
                } else if (spyglass.isEmpty() && player.getOffhandItem().is(Items.SPYGLASS)) {
                    spyglass = player.getOffhandItem().copy();
                    PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "clear2");
                    PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setCurio1");
                }
            });
            spyglass = ItemStack.EMPTY;
            offhandItem = ItemStack.EMPTY;
            //重置按键检查
            pressCheck = false;
        }
    }

    //防副手逻辑混乱
    @SubscribeEvent
    public void exchangeCheck(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        if (pressCheck && Minecraft.getInstance().options.keySwapOffhand.isDown()) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                if (player != null && !spyglass.isEmpty() && player.getOffhandItem().is(Items.SPYGLASS)) {
                    if (!offhandItem.isEmpty() && !mainhandItem.isEmpty()) {
                        PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setCurio1");
                        PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setHand2");//副手上副手
                        doubleSwap = true;
                    } else if (offhandItem.isEmpty() && !mainhandItem.isEmpty()) {
                        spyglass = ItemStack.EMPTY;
                    } else if (!offhandItem.isEmpty() && mainhandItem.isEmpty()) {
                        PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setHand2");
                        PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "setCurio1");
                        spyglass = ItemStack.EMPTY;
                    } else if (offhandItem.isEmpty() && mainhandItem.isEmpty()) {
                        spyglass = ItemStack.EMPTY;
                    }
                }
            });
        }
    }

    //TOOLTIP根据望远镜NBT显示倍率
    @SubscribeEvent
    public void spyglassTooltip(ItemTooltipEvent event) {
        ItemStack spyglass = event.getItemStack();
        if (spyglass.getItem() == Items.SPYGLASS && spyglass.hasTag()) {
            CompoundTag tag = spyglass.getTag();
            if (tag.contains("MULTIPLIER")) {
                int multiplier = tag.getInt("MULTIPLIER");
                Component tooltip = Component.translatable("tooltip.spyglassofcurios.multiplier", "§7§o" + multiplier);
                event.getToolTip().add(tooltip);
            } else {
                Component tooltip = Component.translatable("tooltip.spyglassofcurios.default_multiplier");
                event.getToolTip().add(tooltip);
            }
        } else if (spyglass.getItem() == Items.SPYGLASS) {
            Component tooltip = Component.translatable("tooltip.spyglassofcurios.default_multiplier");
            event.getToolTip().add(tooltip);
        }
    }

    //滚轮调焦
    //随时设置视距
    @SubscribeEvent
    public void onFovModifier(FovEvent event){
        event.setNewFov((float) SpyglassOfCuriosClient.MULTIPLIER);
    }
    //开启时给予NBT，固定焦距为NBT值
    @SubscribeEvent
    public void onUse(LivingEntityUseItemEvent.Start event) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        ItemStack spyglass = event.getItem();
        if (spyglass.is(Items.SPYGLASS) && spyglass.hasTag()) {
            if (spyglass.getTag().contains("MULTIPLIER")) {
                SpyglassOfCuriosClient.MULTIPLIER = (10-spyglass.getTag().getDouble("MULTIPLIER"))/10;
            } else {
                SpyglassOfCuriosClient.MULTIPLIER = 0.1;
            }
        } else if (spyglass.is(Items.SPYGLASS)) {
            SpyglassOfCuriosClient.MULTIPLIER = 0.1;
        }
        PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "spyglassPutNBT");
    }
    //滚轮调倍率并赋予NBT
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event){
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if(player != null && player.isScoping() && client.options.getCameraType().isFirstPerson()){
            if (player.getUseItem().hasTag()){
                //调整倍率
                SpyglassOfCuriosClient.MULTIPLIER = Mth.clamp((10 - Objects.requireNonNull(player.getUseItem().getTag()).getDouble("MULTIPLIER"))/10 - (event.getScrollDelta()/10), .1,1.0);
                player.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0f, (float)(1.0f+(1*(1- SpyglassOfCuriosClient.MULTIPLIER)*(1- SpyglassOfCuriosClient.MULTIPLIER))));
            }
            //发包(把倍率存入独立的望远镜NBT)
            PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "spyglassPutNBT");

            //防止滚轮触发别的操作
            event.setCanceled(true);
        }
    }

    //切换渲染模式
    @SubscribeEvent
    public void setRenderType(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = Minecraft.getInstance().player;
        if(RegisterConfig.disableRenderAll.get()) return;
        if (player != null && event.getItemStack().is(Items.SPYGLASS) && player.isCrouching()) {
            String[] renderTypes = {"back_waist", "head", "indescribable"};
            Boolean[] disableRenders = {RegisterConfig.disableRenderBackWaist.get(), RegisterConfig.disableRenderHead.get(), RegisterConfig.disableRenderIndescribable.get()};
            if (Arrays.stream(disableRenders).allMatch(Boolean::booleanValue)) return;
            int index = Arrays.asList(renderTypes).indexOf(renderType);
            do {
                index = (index + 1) % renderTypes.length;
            } while (disableRenders[index]);
            renderType = renderTypes[index];
            PacketRegister.sendPacket(spyglass, offhandItem, mainhandItem, doubleSwap, MULTIPLIER, renderType, "spyglassPutNBTRender");
            player.displayClientMessage(Component.translatable("switch.spyglassofcurios." + renderType), true);
        }
    }


}