package cn.solarmoon.spyglassofcurios.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;


public class SpyglassOfCuriosClient {

    //静态焦距
    public static double MULTIPLIER = .1f;

    public static Minecraft mc = Minecraft.getInstance();

    //静态渲染识别符
    public static String renderType = "back_waist";

    //客户端静态物品栈
    public static ItemStack spyglass = ItemStack.EMPTY;
    public static ItemStack offhandItem = ItemStack.EMPTY;
    public static ItemStack mainhandItem = ItemStack.EMPTY;

    public static boolean doubleSwap = false;

    //客户端按键检查
    public static boolean pressCheck = false;

}
