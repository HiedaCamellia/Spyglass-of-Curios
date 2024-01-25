package cn.solarmoon.spyglass_of_curios.client.event;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.util.FovAlgorithm;
import cn.solarmoon.spyglass_of_curios.network.PacketRegister;
import cn.solarmoon.spyglass_of_curios.util.SpyglassUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.spyglass_of_curios.util.client.Constants.*;
import static cn.solarmoon.spyglass_of_curios.util.client.Constants.renderType;

public class ScrollingSet {
    //滚轮调倍率并赋予NBT
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event){
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        ISpyUser sp = (ISpyUser) player;
        if(player != null && player.isScoping() && client.options.getCameraType().isFirstPerson()){

            ItemStack spyglass = sp.getSpyglass();

            PacketRegister.sendPacket(sp.multiplier(), renderType, "soundRoll");

            //调整倍率
            FovAlgorithm fovC = new FovAlgorithm(sp);
            double tagDouble = spyglass.getOrCreateTag().getDouble("MULTIPLIER");
            fovC.setFov(tagDouble, event.getScrollDelta());

            //发包(把倍率存入独立的望远镜NBT)
            PacketRegister.sendPacket(fovC.putTag(), renderType, "spyglassPutNBT");

            //防止滚轮触发别的操作
            event.setCanceled(true);
        }
    }
}
