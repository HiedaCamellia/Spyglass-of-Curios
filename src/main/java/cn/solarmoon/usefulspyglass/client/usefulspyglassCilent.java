package cn.solarmoon.usefulspyglass.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = "usefulspyglass", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class usefulspyglassCilent {
    public static KeyMapping useSpyglass = new KeyMapping(
            "key.usefulspyglass.use",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.usefulspyglass"
    );

    @SubscribeEvent
    public static void registerKeymapping(RegisterKeyMappingsEvent event){
        event.register(usefulspyglassCilent.useSpyglass);
    }
}
