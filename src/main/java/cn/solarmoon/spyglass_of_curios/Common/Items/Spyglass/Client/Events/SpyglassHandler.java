package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events;

import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FindSpyglassInHand;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FovAlgorithm;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.FindSpyglassInCurio;
import cn.solarmoon.spyglass_of_curios.Init.Config;
import cn.solarmoon.spyglass_of_curios.Network.PacketRegister;
import cn.solarmoon.spyglass_of_curios.Util.DeBug;
import cn.solarmoon.spyglass_of_curios.Util.ICinemaMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

import static cn.solarmoon.spyglass_of_curios.Common.Items.RegisterItems.useSpyglass;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.*;


public class SpyglassHandler {

    private boolean cameraCheck;

    private double originSensitive;

    //按键绑定 && 设定焦距为nbt值 && 无nbt就自动赋予
    @SubscribeEvent
    public void spyglassUse(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (player == null) return;

        //电影视角 + 倍率越大灵敏度越低
        cinema(player);

        if (useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping()) {

            FindSpyglassInHand finder = new FindSpyglassInHand();
            boolean flag = finder.hasItem();
            boolean hasSpyglassInHand = finder.hasSpyglass();
            ItemStack spyglass = finder.getSpyglass();
            InteractionHand hand = finder.getHand();

            if (flag && hasSpyglassInHand) {
                if (client.gameMode != null) {
                    client.gameMode.useItem(player, hand);
                    setFov(spyglass);
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
                setFov(spyglassCurio);
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

    //防止使用望远镜时使用物品，以及使用时注入tag，
    @SubscribeEvent
    public void preventUse(LivingEntityUseItemEvent event) {
        if (usingInCurio) event.setCanceled(true);

        if(event.getEntity().isUsingItem()) return;
        ItemStack spyglass = event.getItem();
        setFov(spyglass);
    }

    //根据倍率tag的有无来决定是读取tag设置倍率还是设置默认倍率
    public void setFov(ItemStack spyglass) {
        if(!spyglass.is(Items.SPYGLASS)) return;
        boolean tagCheck = spyglass.getTag() != null && spyglass.getTag().contains("MULTIPLIER");
        FovAlgorithm.resetFov();
        if (tagCheck) {
            double tagDouble = spyglass.getTag().getDouble("MULTIPLIER");
            FovAlgorithm.setDefaultFov(tagDouble);
        } else PacketRegister.sendPacket(FovAlgorithm.putTag(), renderType, "spyglassPutNBT");
    }

    public void cinema(Player player) {
        if (!Config.disableCinemaCamera.get()) {
            Options options = mc.options;
            if (!player.isScoping() && !cameraCheck) {
                originSensitive = options.sensitivity().get();
            }
            if (player.isScoping()) {
                if (!cameraCheck) {
                    ((ICinemaMode) options).setSmoothCamera(true);
                    cameraCheck = true;
                }
                options.sensitivity().set(calculate());
            } else if (!player.isScoping() && cameraCheck) {
                options.sensitivity().set(originSensitive);
                ((ICinemaMode) options).setSmoothCamera(false);
                cameraCheck = false;
            }
        }
    }

    public double calculate() {
        double tag = new FovAlgorithm.Mt().argAlg(MULTIPLIER);
        double target = Math.abs(tag);
        if ( tag >= -4 && tag <= 0) return 0.5;
        return Mth.clamp(0.5 / Math.log(target), 0, 0.5 );
    }

}