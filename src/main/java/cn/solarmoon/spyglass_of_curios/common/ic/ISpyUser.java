package cn.solarmoon.spyglass_of_curios.common.ic;

import net.minecraft.world.item.ItemStack;

/**
 * 用于判别玩家的各种望远镜使用状态
 */
public interface ISpyUser {

    boolean usingSpyglassInCurio();
    boolean usingSpyglassInHand();

    double multiplier();

    void setMultiplier(double multiplier);

    ItemStack getSpyglassInCurio();
    ItemStack getSpyglassInHand();

    /**
     * 直接根据顺序获取望远镜，优先获取手中的
     */
    default ItemStack getSpyglass() {
        ItemStack spyglass = ItemStack.EMPTY;
        if (usingSpyglassInHand()) spyglass = getSpyglassInHand();
        else if (usingSpyglassInCurio()) spyglass = getSpyglassInCurio();
        return spyglass;
    }

}
