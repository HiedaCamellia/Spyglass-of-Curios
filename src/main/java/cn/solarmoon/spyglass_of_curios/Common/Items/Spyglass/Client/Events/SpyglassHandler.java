package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events;

import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.FindSpyglassInCurio;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FindSpyglassInHand;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FovAlgorithm;
import cn.solarmoon.spyglass_of_curios.Config.RegisterConfig;
import cn.solarmoon.spyglass_of_curios.Network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

import java.util.Arrays;
import java.util.Objects;

import static cn.solarmoon.spyglass_of_curios.Common.Items.RegisterItems.useSpyglass;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.*;
import static cn.solarmoon.spyglass_of_curios.Util.Translation.translation;


public class SpyglassHandler {

    //按键绑定 && 设定焦距为nbt值 && 无nbt就自动赋予
    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;

        if (useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {

            FindSpyglassInHand finder = new FindSpyglassInHand();
            boolean flag = finder.hasItem();
            boolean hasSpyglassInHand = finder.hasSpyglass();
            ItemStack spyglass = finder.getSpyglass();
            InteractionHand hand = finder.getHand();

            if (flag && hasSpyglassInHand) {
                if (client.gameMode != null) {
                    client.gameMode.useItem(player, hand);
                    FovAlgorithm.resetFov();
                    if (spyglass.getTag() != null) {
                        double tagDouble = spyglass.getTag().getDouble("MULTIPLIER");
                        FovAlgorithm.setDefaultFov(tagDouble);
                    } else PacketRegister.sendPacket(FovAlgorithm.putTag(), renderType, "spyglassPutNBT");
                }
                usingInHand = true;
                return;
            }

            FindSpyglassInCurio finderCurio = new FindSpyglassInCurio();
            ItemStack spyglassCurio = finderCurio.getSpyglass(mc.player);
            boolean hasSpyglassInCurio = finderCurio.hasSpyglass(mc.player);
            if (!hasSpyglassInCurio || hasSpyglassInHand) return;
            if (mc.player != null) {
                PacketRegister.sendPacket(MULTIPLIER, renderType, "playSound1");
                FovAlgorithm.resetFov();
                if (spyglassCurio.hasTag()) {
                    FovAlgorithm.setDefaultFov(Objects.requireNonNull(spyglassCurio.getTag()).getDouble("MULTIPLIER"));
                } else PacketRegister.sendPacket(FovAlgorithm.putTag(), renderType, "spyglassPutNBT");
            }
            usingInCurio = true;

        }

        if (!useSpyglass.isDown()) {
            if(usingInCurio) {
                FindSpyglassInCurio curioFinder = new FindSpyglassInCurio();
                boolean hasSpyglass = curioFinder.hasSpyglass(mc.player);
                if (hasSpyglass) if (mc.player != null) {
                    PacketRegister.sendPacket(MULTIPLIER, renderType, "playSound2");
                }
            }
            usingInHand = false;
            usingInCurio = false;
        }

    }

    //防止使用望远镜时使用物品
    @SubscribeEvent
    public void preventUse(LivingEntityUseItemEvent event) {
        if(usingInCurio) event.setCanceled(true);
    }

    //TOOLTIP根据望远镜NBT显示倍率
    @SubscribeEvent
    public void spyglassTooltip(ItemTooltipEvent event) {
        if(RegisterConfig.disableMultiplierInfo.get()) return;
        ItemStack spyglass = event.getItemStack();
        if (spyglass.is(Items.SPYGLASS) && spyglass.hasTag()) {
            CompoundTag tag = spyglass.getTag();
            if (tag != null && tag.contains("MULTIPLIER")) {
                int multiplier = tag.getInt("MULTIPLIER");
                Component tooltip = translation("tooltip", "multiplier", "§7§o" + multiplier);
                event.getToolTip().add(tooltip);
            }
        } else if (spyglass.is(Items.SPYGLASS)) {
            Component tooltip = translation("tooltip", "default_multiplier");
            event.getToolTip().add(tooltip);
        }
    }

    //滚轮调倍率并赋予NBT
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event){
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if(player != null && player.isScoping() && client.options.getCameraType().isFirstPerson()){
            FindSpyglassInCurio curioFinder = new FindSpyglassInCurio();
            boolean hasSpyglass = curioFinder.hasSpyglass(mc.player);

            ItemStack spyglass = ItemStack.EMPTY;
            if (player.isUsingItem()) spyglass = player.getUseItem();
            else if (!player.isUsingItem() && hasSpyglass) spyglass = curioFinder.getSpyglass(mc.player);

            //调整倍率
            if (spyglass.getTag() != null) {
                double tagDouble = spyglass.getTag().getDouble("MULTIPLIER");
                FovAlgorithm.setFov(tagDouble, event.getScrollDelta());
            }

            PacketRegister.sendPacket(MULTIPLIER, renderType, "playSound3");
            //发包(把倍率存入独立的望远镜NBT)
            PacketRegister.sendPacket(FovAlgorithm.putTag(), renderType, "spyglassPutNBT");

            //防止滚轮触发别的操作
            event.setCanceled(true);
        }
    }

    //切换渲染模式
    @SubscribeEvent
    public void setRenderType(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = mc.player;
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
            player.displayClientMessage(translation("switch", renderType), true);
        }
    }

}