package cn.solarmoon.spyglassofcurios.Client.Method;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static cn.solarmoon.spyglassofcurios.Client.Constants.mc;

public class FindSpyglassInHand {

    private boolean flag;
    private ItemStack spyglass;
    private InteractionHand hand;

    //flag：手中是否有物品  spyglass：获取双手中的望远镜（优先副手） hand：获取望远镜所在的手
    public void findSpyglassInHand() {
        if (mc.player != null) {
            flag = !mc.player.getMainHandItem().isEmpty() || !mc.player.getOffhandItem().isEmpty();
            if(flag) {
                spyglass = mc.player.getOffhandItem().is(Items.SPYGLASS) ? mc.player.getOffhandItem() : mc.player.getMainHandItem();
                hand = mc.player.getOffhandItem().is(Items.SPYGLASS) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            } else {
                spyglass = ItemStack.EMPTY;
            }
        } else {
            spyglass = ItemStack.EMPTY;
            flag = false;
        }
    }

    public boolean hasItem() {
        findSpyglassInHand();
        return flag;
    }

    public ItemStack getSpyglass() {
        findSpyglassInHand();
        return spyglass;
    }

    public InteractionHand getHand() {
        findSpyglassInHand();
        return this.hand;
    }

}
