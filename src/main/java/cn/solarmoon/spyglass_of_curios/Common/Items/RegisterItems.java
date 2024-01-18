package cn.solarmoon.spyglass_of_curios.Common.Items;

import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events.ScrollingSet;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events.SelectRender;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events.SpyglassHandler;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Events.Tooltip;
import cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Client.Renderer.SpyglassRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static cn.solarmoon.spyglass_of_curios.SpyglassOfCurios.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterItems {

    //客户端事件订阅
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        //事件
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());
        MinecraftForge.EVENT_BUS.register(new SelectRender());
        MinecraftForge.EVENT_BUS.register(new ScrollingSet());
        MinecraftForge.EVENT_BUS.register(new Tooltip());
        //饰品渲染
        CuriosRendererRegistry.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:spyglass")), SpyglassRenderer::new);
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(useSpyglass));
    }

    //按键
    //按键
    public static KeyMapping useSpyglass = new KeyMapping(
            "key.spyglass_of_curios.use",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.spyglass_of_curios");

}
