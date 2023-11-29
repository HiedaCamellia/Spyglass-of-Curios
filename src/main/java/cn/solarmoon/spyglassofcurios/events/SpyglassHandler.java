package cn.solarmoon.spyglassofcurios.events;

import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.Objects;

import static cn.solarmoon.spyglassofcurios.SpyglassOfCuriosMod.useSpyglass;
import static cn.solarmoon.spyglassofcurios.client.SpyglassOfCuriosClient.*;


public class SpyglassHandler {

    //按键绑定 && 设定焦距为nbt值 && 无nbt就自动赋予
    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;

        if (useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {

            ItemStack spyglass;
            InteractionHand hand = InteractionHand.MAIN_HAND;
            boolean flag;
            if (mc.player != null) {
                flag = !mc.player.getMainHandItem().isEmpty() || !mc.player.getOffhandItem().isEmpty();
                if(flag) {
                    spyglass = mc.player.getOffhandItem().is(Items.SPYGLASS) ? mc.player.getOffhandItem() : mc.player.getMainHandItem();
                    hand = mc.player.getOffhandItem().is(Items.SPYGLASS) ? InteractionHand.OFF_HAND : hand;
                } else {
                    spyglass = ItemStack.EMPTY;
                }
            } else {
                spyglass = ItemStack.EMPTY;
                flag = false;
            }

            if (flag && spyglass.is(Items.SPYGLASS)) {
                if (client.gameMode != null) {
                    client.gameMode.useItem(player, hand);
                    if (!spyglass.hasTag()) {
                        SpyglassOfCuriosClient.MULTIPLIER = .1;
                        PacketRegister.sendPacket(MULTIPLIER, renderType, "spyglassPutNBT");
                    } else SpyglassOfCuriosClient.MULTIPLIER = (10 - Objects.requireNonNull(spyglass.getTag()).getDouble("MULTIPLIER")) / 10;
                }
                return;
            }

            if (mc.player != null) {
                CuriosApi.getCuriosHelper().findCurio(mc.player, "spyglass", 0).ifPresent(slotResult -> {
                    if (flag && spyglass.is(Items.SPYGLASS)) return;
                    if (mc.player != null) {
                        mc.player.playSound(SoundEvents.SPYGLASS_USE);
                        if (!slotResult.stack().hasTag()) {
                            SpyglassOfCuriosClient.MULTIPLIER = .1;
                            PacketRegister.sendPacket(MULTIPLIER, renderType, "spyglassPutNBT");
                        } else SpyglassOfCuriosClient.MULTIPLIER = (10 - Objects.requireNonNull(slotResult.stack().getTag()).getDouble("MULTIPLIER")) / 10;
                    }
                });
            }

            //按键检查
            pressCheck = true;

        }

        if (!useSpyglass.isDown() && pressCheck) {
            //重置按键检查
            if (mc.player != null) {
                CuriosApi.getCuriosHelper().findCurio(mc.player, "spyglass", 0).ifPresent(slotResult -> mc.player.playSound(SoundEvents.SPYGLASS_STOP_USING));
            }
            pressCheck = false;
        }

    }

    //TOOLTIP根据望远镜NBT显示倍率
    @SubscribeEvent
    public void spyglassTooltip(ItemTooltipEvent event) {
        ItemStack spyglass = event.getItemStack();
        if (spyglass.is(Items.SPYGLASS) && spyglass.hasTag()) {
            CompoundTag tag = spyglass.getTag();
            if (tag != null && tag.contains("MULTIPLIER")) {
                int multiplier = tag.getInt("MULTIPLIER");
                Component tooltip = Component.translatable("tooltip.spyglassofcurios.multiplier", "§7§o" + multiplier);
                event.getToolTip().add(tooltip);
            }
        } else if (spyglass.is(Items.SPYGLASS)) {
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
    //滚轮调倍率并赋予NBT
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event){
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if(player != null && player.isScoping() && client.options.getCameraType().isFirstPerson()){
            ItemStack spyglass = ItemStack.EMPTY;
            if (player.isUsingItem()) spyglass = player.getUseItem();
            else if (mc.player != null && CuriosApi.getCuriosHelper().findCurio(mc.player, "spyglass", 0).isPresent() && !player.isUsingItem())
                spyglass = CuriosApi.getCuriosHelper().findCurio(mc.player, "spyglass", 0).get().stack();

            //调整倍率
            if (spyglass.getTag() != null) {
                SpyglassOfCuriosClient.MULTIPLIER = Mth.clamp((10 - spyglass.getTag().getDouble("MULTIPLIER")) / 10 - (event.getScrollDelta() / 10), .1, 1.0);
            }

            player.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0f, (float) (1.0f + (1 * (1 - SpyglassOfCuriosClient.MULTIPLIER) * (1 - SpyglassOfCuriosClient.MULTIPLIER))));
            //发包(把倍率存入独立的望远镜NBT)
            PacketRegister.sendPacket(MULTIPLIER, renderType, "spyglassPutNBT");

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
            PacketRegister.sendPacket(MULTIPLIER, renderType, "spyglassPutNBTRender");
            player.displayClientMessage(Component.translatable("switch.spyglassofcurios." + renderType), true);
        }
    }


}