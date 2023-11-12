package cn.solarmoon.spyglassofcurios.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;


@Mod.EventBusSubscriber(modid = "spyglassofcurios", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpyglassOfCuriosClient {
    //按键注册
    public static KeyMapping useSpyglass = new KeyMapping(
            "key.spyglassofcurios.use",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.spyglassofcurios"
    );
    @SubscribeEvent
    public static void registerKeymapping(RegisterKeyMappingsEvent event){
        event.register(SpyglassOfCuriosClient.useSpyglass);
    }

    //静态焦距
    public static double MULTIPLIER = .1f;

    //静态渲染识别符
    public static String renderType = "back_waist";

    public static boolean pressCheck = false;

    //客户端静态物品栈
    public static ItemStack spyglass = ItemStack.EMPTY;
    public static ItemStack offhandItem = ItemStack.EMPTY;
    public static ItemStack mainhandItem = ItemStack.EMPTY;

    //注册望远镜渲染
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent e) {
        CuriosRendererRegistry.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:spyglass")), SpyglassRenderer::new);
    }

    @SubscribeEvent
    public static void SetupRes(TextureStitchEvent.Pre evt) {
        evt.addSprite(new ResourceLocation("spyglassofcurios", "slot/spyglass"));
    }

}
