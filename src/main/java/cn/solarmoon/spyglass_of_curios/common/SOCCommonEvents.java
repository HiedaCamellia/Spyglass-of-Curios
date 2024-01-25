package cn.solarmoon.spyglass_of_curios.common;

import cn.solarmoon.spyglass_of_curios.common.event.PreventUse;
import cn.solarmoon.spyglass_of_curios.common.event.SpyglassUseAndTick;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SOCCommonEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpyglassUseAndTick());
        MinecraftForge.EVENT_BUS.register(new PreventUse());
    }
}
