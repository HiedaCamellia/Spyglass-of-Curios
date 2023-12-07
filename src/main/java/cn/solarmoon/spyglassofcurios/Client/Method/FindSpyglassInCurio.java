package cn.solarmoon.spyglassofcurios.Client.Method;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class FindSpyglassInCurio {

    private ItemStack spyglass;
    private boolean hasSpyglass;

    public void findSpyglassInCurio(Player player) {
        if(player == null) return;
        hasSpyglass = !CuriosApi.getCuriosHelper().findCurios(player, "spyglass").isEmpty();
        if(CuriosApi.getCuriosHelper().findCurios(player, "spyglass").isEmpty()) return;
        spyglass = CuriosApi.getCuriosHelper().findCurios(player, "spyglass").get(0).stack();
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
