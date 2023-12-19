package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class FindSpyglassInCurio {

    private ItemStack spyglass;
    private boolean hasSpyglass;

    public void findSpyglassInCurio(Player player) {
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if(iCuriosItemHandler.findCurio("spyglass", 0).isEmpty()) {
                hasSpyglass = false;
                return;
            }
            hasSpyglass = iCuriosItemHandler.findCurio("spyglass", 0).isPresent();
            spyglass = iCuriosItemHandler.findCurio("spyglass", 0).get().stack();
        });
    }

    public boolean hasSpyglass(Player player) {
        findSpyglassInCurio(player);
        return hasSpyglass;
    }

    public ItemStack getSpyglass(Player player) {
        findSpyglassInCurio(player);
        return spyglass;
    }

}
