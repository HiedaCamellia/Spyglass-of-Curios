package cn.solarmoon.spyglass_of_curios.mixin;

import cn.solarmoon.spyglass_of_curios.common.ic.ICinemaMode;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Options.class)
public abstract class OptionsMixin implements ICinemaMode {

    @Shadow
    public boolean smoothCamera;

    public void setSmoothCamera(boolean value) {
        this.smoothCamera = value;
    }

}

