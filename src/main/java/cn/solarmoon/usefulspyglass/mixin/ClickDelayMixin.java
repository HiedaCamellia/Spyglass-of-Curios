package cn.solarmoon.usefulspyglass.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface ClickDelayMixin {
    @Accessor("rightClickDelay")
    int getrightClickDelay();
}
