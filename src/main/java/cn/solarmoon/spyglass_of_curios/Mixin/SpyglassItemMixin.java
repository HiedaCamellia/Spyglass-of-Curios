package cn.solarmoon.spyglass_of_curios.mixin;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.init.Config;
import cn.solarmoon.spyglass_of_curios.init.Keys;
import cn.solarmoon.spyglass_of_curios.network.PacketRegister;
import cn.solarmoon.spyglass_of_curios.util.SpyglassUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

import static cn.solarmoon.spyglass_of_curios.util.TextUtil.translation;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item implements ICurioItem {

    public SpyglassItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        Level level = player.level;

        //使用键位了就只能是客户端侧了
        if (!level.isClientSide) return;

        player = Minecraft.getInstance().player != null ? Minecraft.getInstance().player : player;
        ISpyUser sp = (ISpyUser) player;

        if (Keys.useSpyglass.isDown() && !player.isUsingItem() && !player.isScoping() && !sp.usingSpyglassInCurio()) {
            SpyglassUtil.setFov(sp.getSpyglassInCurio(), level, sp);
            PacketRegister.sendPacket(true, "using");
            PacketRegister.sendPacket("soundUse");
        }

        if (!Keys.useSpyglass.isDown()) {
            //保证一定是使用后才发声，否则会导致持续发声
            //同时客户端单侧可能会因为一个sp的空按键导致执行，因此必须放入使用后的条件内
            if (sp.usingSpyglassInCurio()) {
                PacketRegister.sendPacket(false, "using");
                PacketRegister.sendPacket("soundStop");
            }
        }

    }

    /**
     * 能够用按键优先使用手中的望远镜
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int delta, boolean flag) {
        if (level.isClientSide) {
            Player player = Minecraft.getInstance().player;
            ISpyUser sp = (ISpyUser) player;
            if (player != null) {
                SpyglassUtil.Finder.Hand finder = new SpyglassUtil.Finder.Hand(player);
                ItemStack spyglass = finder.getSpyglass();
                InteractionHand hand = finder.getHand();
                if (Keys.useSpyglass.isDown() && finder.hasSpyglass() && !player.isScoping()) {
                    if (Minecraft.getInstance().gameMode != null) {
                        Minecraft.getInstance().gameMode.useItem(player, hand);
                    }
                    SpyglassUtil.setFov(spyglass, level, sp);
                }
            }
        }
        super.inventoryTick(stack, level, entity, delta, flag);
    }

    /**
     * 防止饰品槽望远镜正在使用时使用手中的望远镜
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void preventUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (player.isScoping()) {
            cir.setReturnValue(InteractionResultHolder.fail(player.getItemInHand(hand)));
        }
    }

    /**
     * 展示倍率信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(!Config.disableMultiplierInfo.get()) {
            if (stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("MULTIPLIER")) {
                    int multiplier = tag.getInt("MULTIPLIER");
                    Component tooltip = translation("tooltip", "multiplier", "§7§o" + multiplier);
                    components.add(tooltip);
                }
            } else {
                int defaultMul = Config.defaultMultiplier.get();
                Component tooltip = translation("tooltip", "default_multiplier", "§7§o" + defaultMul);
                components.add(tooltip);
            }
        }
        super.appendHoverText(stack, level, components, flag);
    }

}
