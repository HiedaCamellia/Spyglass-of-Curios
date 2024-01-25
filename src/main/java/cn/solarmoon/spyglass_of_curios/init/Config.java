package cn.solarmoon.spyglass_of_curios.init;

import cn.solarmoon.spyglass_of_curios.SpyglassOfCurios;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;


@Mod.EventBusSubscriber(modid = SpyglassOfCurios.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
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

    public static ForgeConfigSpec.ConfigValue<Boolean> enableCinemaCamera;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        deBug = builder
                .comment("Used for test.")
                .comment("用于调试")
                .define("deBug", false);

        builder
                .comment("Render Settings")
                .push("渲染设置");
        disableRenderAll = builder
                .comment("Disable Render for ALL.")
                .comment("禁用所有渲染")
                .define("disableRenderAll", false);
        disableRenderBackWaist = builder
                .comment("Disable Render - Back waist.")
                .comment("禁用渲染 - 后腰")
                .define("disableRenderBackWaist", false);
        disableRenderHead = builder
                .comment("Disable Render - Head.")
                .comment("禁用渲染 - 头")
                .define("disableRenderHead", false);
        disableRenderIndescribable = builder
                .comment("Disable Render - Indescribable.")
                .comment("禁用渲染 - 不可描述")
                .define("disableRenderIndescribable", false);
        builder.pop();

        builder
                .comment("Info Settings")
                .push("信息设置");
        disableMultiplierInfo = builder
                .comment("Disable Multiplier Info.")
                .comment("禁用倍率显示")
                .define("disableMultiplierInfo", false);
        builder.pop();

        builder
                .comment("Multiplier Settings")
                .push("倍率设置");
        defaultMultiplier = builder
                .comment("The default multiplier of spyglass.")
                .comment("望远镜默认倍率")
                .define("defaultMultiplier", 7);
        maxMultiplier = builder
                .comment("The maximum multiplier of spyglass.")
                .comment("望远镜最大倍率")
                .defineInRange("maxMultiplier", 12, 0, 100);
        minMultiplier = builder
                .comment("The minimum multiplier of spyglass.")
                .comment("A minimum value less than -15 will cause the image to flip.")
                .comment("望远镜最小倍率")
                .comment("最小值小于-15将会导致镜像反转")
                .defineInRange("minMultiplier", -12, -100, 100);
        builder.pop();

        builder
                .comment("Display Settings")
                .push("显示设置");
        enableCinemaCamera = builder
                .comment("Enable cinematic camera when scoping (Just like Optifine).")
                .comment("启用使用望远镜时的电影视角（就像Optifine的放大一样）")
                .define("enableCinemaCamera", false);
        builder.pop();

        common = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(COMMON, Config.common,
                FMLPaths.CONFIGDIR.get().resolve(PATH).toString());
    }
}
