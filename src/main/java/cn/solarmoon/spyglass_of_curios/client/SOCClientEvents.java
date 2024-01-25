package cn.solarmoon.spyglass_of_curios.client;

import cn.solarmoon.spyglass_of_curios.client.curios_renderer.SpyglassRenderer;
import cn.solarmoon.spyglass_of_curios.client.event.ScrollingSet;
import cn.solarmoon.spyglass_of_curios.client.event.SelectRender;
import cn.solarmoon.spyglass_of_curios.client.event.SpyglassUseAndTick;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SOCClientEvents {

    //客户端事件订阅
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        //事件
        MinecraftForge.EVENT_BUS.register(new SelectRender());
        MinecraftForge.EVENT_BUS.register(new ScrollingSet());
        MinecraftForge.EVENT_BUS.register(new SpyglassUseAndTick());
        //饰品渲染
        CuriosRendererRegistry.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:spyglass")), SpyglassRenderer::new);
    }

}
