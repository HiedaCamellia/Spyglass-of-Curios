package cn.solarmoon.spyglass_of_curios.mixin;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.util.SpyglassUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin implements ISpyUser {

    @Inject(method = "isScoping",at=@At(value = "RETURN"),cancellable = true)
    public void onScoping(CallbackInfoReturnable<Boolean> cir){
        if (usingSpyglassInCurio()) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean usingSpyglassInCurio() {
        Player player = (Player)(Object) this;
        return SpyglassUtil.isUsing(player);
    }

    @Override
    public boolean usingSpyglassInHand() {
        Player player = (Player)(Object) this;
        return player.getUseItem().is(Items.SPYGLASS);
    }

    private double multiplier;

    @Override
    public double multiplier() {
        return this.multiplier;
    }

    @Override
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public ItemStack getSpyglassInCurio() {
        Player player = (Player)(Object) this;
        return new SpyglassUtil.Finder.Curio(player).getSpyglass();
    }

    @Override
    public ItemStack getSpyglassInHand() {
        Player player = (Player)(Object) this;
        return new SpyglassUtil.Finder.Hand(player).getSpyglass();
    }

}
