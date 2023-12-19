package cn.solarmoon.spyglass_of_curios.Util;

import net.minecraft.client.Minecraft;

import static cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client.FovAlgorithm.delta;


public class Constants {

    //静态焦距
    public static double MULTIPLIER = 1 - 9 * delta;

    public static Minecraft mc = Minecraft.getInstance();

    //静态渲染识别符
    public static String renderType = "back_waist";

    //使用标识
    public static boolean usingInHand = false;
    public static boolean usingInCurio = false;

}
