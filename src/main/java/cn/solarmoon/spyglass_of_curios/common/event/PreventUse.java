package cn.solarmoon.spyglass_of_curios.common.event;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PreventUse {

    /**
     * 防止使用望远镜时使用物品
     */
    @SubscribeEvent
    public void preventUse(LivingEntityUseItemEvent event) {
        if (event.getEntity() instanceof ISpyUser sp) {
            if (sp.usingSpyglassInCurio() && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

}
