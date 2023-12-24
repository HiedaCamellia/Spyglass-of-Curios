package cn.solarmoon.spyglass_of_curios.Init;

import cn.solarmoon.spyglass_of_curios.SpyglassOfCurios;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;


@Mod.EventBusSubscriber(modid = SpyglassOfCurios.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterConfig {
    public static final String PATH =  SpyglassOfCurios.MOD_ID + ".toml";
    public static ForgeConfigSpec common;

    public static ForgeConfigSpec.ConfigValue<Boolean> deBug;

    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderAll;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderBackWaist;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderHead;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderIndescribable;

    public static ForgeConfigSpec.ConfigValue<Boolean> disableMultiplierInfo;

    public static ForgeConfigSpec.ConfigValue<Integer> defaultMultiplier;
    public static ForgeConfigSpec.ConfigValue<Integer> maxMultiplier;
    public static ForgeConfigSpec.ConfigValue<Integer> minMultiplier;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("渲染设置 Render Settings");
        disableRenderAll = builder.comment("\n禁用所有渲染 Disable Render for ALL").define("disableRenderAll", false);
        disableRenderBackWaist = builder.comment("\n禁用渲染 - 后腰 Disable Render - Back waist").define("disableRenderBackWaist", false);
        disableRenderHead = builder.comment("\n禁用渲染 - 头 Disable Render - Head").define("disableRenderHead", false);
        disableRenderIndescribable = builder.comment("\n禁用渲染 - 不可描述 Disable Render - Indescribable").define("disableRenderIndescribable", false);
        builder.pop();

        builder.push("信息设置 Info Settings");
        disableMultiplierInfo = builder.comment("\n禁用倍率显示 Disable Multiplier Info").define("disableMultiplierInfo", false);
        builder.pop();

        builder.push("倍率设置 Multiplier Settings");
        defaultMultiplier = builder.comment("\n望远镜默认倍率 The default multiplier of spyglass").define("defaultMultiplier", 7);
        maxMultiplier = builder.comment("\n望远镜最大倍率 The maximum multiplier of spyglass").defineInRange("maxMultiplier", 12, 0, 100);
        minMultiplier = builder.comment("\n望远镜最小倍率 The minimum multiplier of spyglass\n最小值小于-15将会导致镜像反转 A minimum value less than -15 will cause the image to flip.").defineInRange("minMultiplier", -12, -100, 100);
        builder.pop();

        deBug = builder.comment("用于调试 Used for test").define("deBug", false);

        common = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(COMMON, RegisterConfig.common,
                FMLPaths.CONFIGDIR.get().resolve(PATH).toString());
    }
}
