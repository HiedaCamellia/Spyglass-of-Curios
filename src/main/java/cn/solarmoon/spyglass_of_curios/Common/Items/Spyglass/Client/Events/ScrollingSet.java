package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events;

import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FovAlgorithm;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.FindSpyglassInCurio;
import cn.solarmoon.spyglass_of_curios.Network.PacketRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.*;
import static cn.solarmoon.spyglass_of_curios.Util.Constants.renderType;

public class ScrollingSet {
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
}
