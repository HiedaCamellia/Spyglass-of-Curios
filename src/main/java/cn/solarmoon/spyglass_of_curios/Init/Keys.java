package cn.solarmoon.spyglass_of_curios.init;

import cn.solarmoon.spyglass_of_curios.SpyglassOfCurios;
import cn.solarmoon.spyglass_of_curios.util.TextUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SpyglassOfCurios.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Keys {

    //按键
    public static KeyMapping useSpyglass;
    @SubscribeEvent
    public static void onRegisterKeyBinds(RegisterKeyMappingsEvent event) {
        useSpyglass = new KeyMapping(
                TextUtil.translation("key", "use").getString(),
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                TextUtil.translation("category").getString()
        );

        event.register(useSpyglass);
    }

}
