package cn.solarmoon.spyglassofcurios.Client;

import cn.solarmoon.spyglassofcurios.Client.Events.SpyglassHandler;
import cn.solarmoon.spyglassofcurios.Client.Renderer.SpyglassRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@OnlyIn(Dist.CLIENT)
public class RegisterClient {

    public static void clientSetup(FMLClientSetupEvent event) {
        //事件
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());
        //饰品渲染
        CuriosRendererRegistry.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:spyglass")), SpyglassRenderer::new);
    }

    //按键
    public static KeyMapping useSpyglass;
    public static void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        useSpyglass = new KeyMapping(
                "key.spyglassofcurios.use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.spyglassofcurios");

        event.register(useSpyglass);
    }



}
