package cn.solarmoon.spyglassofcurios.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = "spyglassofcurios", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class spyglassOfCuriosClient {
    public static KeyMapping useSpyglass = new KeyMapping(
            "key.spyglassofcurios.use",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.spyglassofcurios"
    );

    @SubscribeEvent
    public static void registerKeymapping(RegisterKeyMappingsEvent event){
        event.register(spyglassOfCuriosClient.useSpyglass);
    }
}
