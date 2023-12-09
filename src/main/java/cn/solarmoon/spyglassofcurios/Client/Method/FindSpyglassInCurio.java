package cn.solarmoon.spyglassofcurios.Client.Method;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class FindSpyglassInCurio {

    private ItemStack spyglass;
    private boolean hasSpyglass;

    public void findSpyglassInCurio(Player player) {
        if(CuriosApi.getCuriosHelper().findCurios(player, "spyglass").isEmpty()) return;
        hasSpyglass = CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).isPresent();
        spyglass = CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).get().stack();
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
