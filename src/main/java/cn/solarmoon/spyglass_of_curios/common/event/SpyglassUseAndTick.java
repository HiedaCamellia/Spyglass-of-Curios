package cn.solarmoon.spyglass_of_curios.common.event;

import cn.solarmoon.spyglass_of_curios.common.ic.ICinemaMode;
import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.init.Config;
import cn.solarmoon.spyglass_of_curios.util.FovAlgorithm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class SpyglassUseAndTick {

    /**
     * 电影视角
     * 按键设置
     * 按键使用手中望远镜
     */
    @SubscribeEvent
    public void spyglassUse(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide) {
            //当望远时生效
            //电影视角 + 倍率越大灵敏度越低
            cinema(player);
        }
    }

    private boolean cameraCheck;
    private double originSensitive;

    /**
     * 当正在望远时开启电影视场，已检测只在客户端侧生效
     * @param player 一般为本地玩家
     */
    @OnlyIn(Dist.CLIENT)
    public void cinema(Player player) {
        if (player.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && player.is(mc.player)) {
                ISpyUser sp = (ISpyUser) player;
                if (Config.enableCinemaCamera.get()) {
                    Options options = mc.options;
                    if (!player.isScoping() && !cameraCheck) {
                        originSensitive = options.sensitivity().get();
                    }
                    if (player.isScoping() && mc.options.getCameraType().isFirstPerson()) {
                        if (!cameraCheck && mc.options.getCameraType().isFirstPerson()) {
                            ((ICinemaMode) options).setSmoothCamera(true);
                            cameraCheck = true;
                        }
                        options.sensitivity().set(calculate(sp));
                    } else if ((!player.isScoping() || !mc.options.getCameraType().isFirstPerson()) && cameraCheck) {
                        options.sensitivity().set(originSensitive);
                        ((ICinemaMode) options).setSmoothCamera(false);
                        cameraCheck = false;
                    }
                }
            }
        }
    }

    /**
     * 鼠标灵敏度根据倍率的算法
     */
    public double calculate(ISpyUser sp) {
        double tag = FovAlgorithm.Mt.argAlg(sp.multiplier());
        double target = Math.abs(tag);
        if ( tag >= -4 && tag <= 0) return 0.5;
        return Mth.clamp(0.5 / Math.log(target), 0, 0.5 );
    }

}