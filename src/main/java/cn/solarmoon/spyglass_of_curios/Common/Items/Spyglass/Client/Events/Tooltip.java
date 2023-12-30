package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events;

import cn.solarmoon.spyglass_of_curios.Init.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.spyglass_of_curios.Util.Translation.translation;

public class Tooltip {
    //TOOLTIP根据望远镜NBT显示倍率
    @SubscribeEvent
    public void spyglassTooltip(ItemTooltipEvent event) {
        if(Config.disableMultiplierInfo.get()) return;
        ItemStack spyglass = event.getItemStack();
        if (spyglass.is(Items.SPYGLASS) && spyglass.hasTag()) {
            CompoundTag tag = spyglass.getTag();
            if (tag != null && tag.contains("MULTIPLIER")) {
                int multiplier = tag.getInt("MULTIPLIER");
                Component tooltip = translation("tooltip", "multiplier", "§7§o" + multiplier);
                event.getToolTip().add(tooltip);
            }
        } else if (spyglass.is(Items.SPYGLASS)) {
            int defaultMul = Config.defaultMultiplier.get();
            Component tooltip = translation("tooltip", "default_multiplier", "§7§o" + defaultMul);
            event.getToolTip().add(tooltip);
        }
    }
}
