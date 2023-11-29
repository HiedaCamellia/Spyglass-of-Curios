package cn.solarmoon.spyglassofcurios;

import cn.solarmoon.spyglassofcurios.Config.RegisterConfig;
import cn.solarmoon.spyglassofcurios.client.SpyglassRenderer;
import cn.solarmoon.spyglassofcurios.events.SpyglassHandler;
import cn.solarmoon.spyglassofcurios.network.PacketRegister;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod("spyglassofcurios")
public class SpyglassOfCuriosMod {
    public static final String MOD_ID = "spyglassofcurios";

    public SpyglassOfCuriosMod() {
        //数据包
        PacketRegister packetRegister = new PacketRegister();
        packetRegister.register();

        RegisterConfig.register();

        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeyBinds);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpyglassHandler());

        CuriosRendererRegistry.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:spyglass")), SpyglassRenderer::new);

    }

    public static KeyMapping useSpyglass;

    @OnlyIn(Dist.CLIENT)
    public void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        useSpyglass = new KeyMapping(
                "key.spyglassofcurios.use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.spyglassofcurios");

        event.register(useSpyglass);
    }


}


