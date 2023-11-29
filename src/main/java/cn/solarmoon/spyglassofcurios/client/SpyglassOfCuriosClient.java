package cn.solarmoon.spyglassofcurios.client;

import net.minecraft.client.Minecraft;


public class SpyglassOfCuriosClient {

    //静态焦距
    public static double MULTIPLIER = .1f;

    public static Minecraft mc = Minecraft.getInstance();

    //静态渲染识别符
    public static String renderType = "back_waist";

    //客户端按键检查
    public static boolean pressCheck = false;

    public static boolean check = false;

}
