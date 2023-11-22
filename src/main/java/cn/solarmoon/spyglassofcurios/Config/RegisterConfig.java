package cn.solarmoon.spyglassofcurios.Config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;


@Mod.EventBusSubscriber(modid = "spyglassofcurios", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterConfig {
    public static final String PATH = "spyglassofcurios.toml";
    public static ForgeConfigSpec common;

    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderAll;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderBackWaist;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderHead;
    public static ForgeConfigSpec.ConfigValue<Boolean> disableRenderIndescribable;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Render Settings");
        disableRenderAll = builder.comment("Disable Render for ALL").define("disableRenderAll", false);
        disableRenderBackWaist = builder.comment("Disable Render-Back waist").define("disableRenderBackWaist", false);
        disableRenderHead = builder.comment("Disable Render-Head").define("disableRenderHead", false);
        disableRenderIndescribable = builder.comment("Disable Render-Indescribable").define("disableRenderIndescribable", false);
        builder.pop();
        common = builder.build();
    }
    public static void register() {
        ModLoadingContext.get().registerConfig(COMMON, RegisterConfig.common,
                FMLPaths.CONFIGDIR.get().resolve(PATH).toString());
    }
}