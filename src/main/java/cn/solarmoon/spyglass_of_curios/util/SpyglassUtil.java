package cn.solarmoon.spyglass_of_curios.util;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.network.PacketRegister;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import static cn.solarmoon.spyglass_of_curios.util.client.Constants.renderType;

public class SpyglassUtil {

    /**
     * 根据倍率tag的有无来决定是读取tag设置倍率还是设置默认倍率
     * @param spyglass tag读取/写入源
     * @param level 判断是否为客户端侧（单侧生效）
     */
    public static void setFov(ItemStack spyglass, Level level, ISpyUser sp) {
        if(!spyglass.is(Items.SPYGLASS) || !level.isClientSide) return;
        boolean tagCheck = spyglass.getTag() != null && spyglass.getTag().contains("MULTIPLIER");
        FovAlgorithm fovC = new FovAlgorithm(sp);
        fovC.resetFov();
        if (tagCheck) {
            double tagDouble = spyglass.getTag().getDouble("MULTIPLIER");
            fovC.setDefaultFov(tagDouble);
        } else PacketRegister.sendPacket(fovC.putTag(), renderType, "spyglassPutNBT");
    }

    /**
     * 根据tag判断望远镜是否在饰品槽内被使用
     */
    public static boolean isUsing(LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack spyglass = new Finder.Curio(player).getSpyglass();
            return spyglass.getOrCreateTag().getBoolean("using");
        }
        return false;
    }

    /**
     * 寻找特定部位的望远镜
     */
    public static class Finder {

        public static class Curio {

            private ItemStack spyglass;
            private boolean hasSpyglass;
            private final Player player;

            /**
             * 寻找特定玩家望远镜饰品槽位内的望远镜
             * @param entity 要查找的玩家
             */
            public Curio(LivingEntity entity) {
                this.player = (Player) entity;
            }

            public void find() {
                if(CuriosApi.getCuriosHelper().findCurios(player, "spyglass").isEmpty()) return;
                hasSpyglass = CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).isPresent();
                spyglass = CuriosApi.getCuriosHelper().findCurio(player, "spyglass", 0).get().stack();
            }

            public boolean hasSpyglass() {
                find();
                return hasSpyglass;
            }

            public ItemStack getSpyglass() {
                find();
                if (hasSpyglass) return spyglass;
                return ItemStack.EMPTY;
            }

        }

        public static class Hand {

            private boolean flag;
            private ItemStack spyglass;
            private InteractionHand hand;
            private final Player player;

            public Hand(Player player) {
                this.player = player;
            }

            public void findSpyglassInHand() {
                if (player != null) {
                    flag = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
                    if(flag) {
                        spyglass = player.getOffhandItem().is(Items.SPYGLASS) ? player.getOffhandItem() : player.getMainHandItem();
                        hand = player.getOffhandItem().is(Items.SPYGLASS) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                    } else {
                        spyglass = ItemStack.EMPTY;
                    }
                } else {
                    spyglass = ItemStack.EMPTY;
                    flag = false;
                }
            }

            /**
             * 手中是否持有物品
             */
            public boolean hasItem() {
                findSpyglassInHand();
                return flag;
            }

            /**
             * 手中是否持有望远镜
             */
            public boolean hasSpyglass() {
                findSpyglassInHand();
                return spyglass.is(Items.SPYGLASS);
            }

            /**
             * 获取双手中的望远镜（优先副手）
             */
            public ItemStack getSpyglass() {
                findSpyglassInHand();
                return spyglass;
            }

            /**
             * 获取望远镜所在的手
             */
            public InteractionHand getHand() {
                findSpyglassInHand();
                return this.hand;
            }

        }
    }

}
