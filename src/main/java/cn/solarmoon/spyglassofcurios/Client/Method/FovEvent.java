package cn.solarmoon.spyglassofcurios.Client.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.Event;

public class FovEvent extends Event {
    private final double fov;
    private double newFov;

    public FovEvent(double fov)
    {
        this.fov = fov;
        this.setNewFov(Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0, fov));
    }

    public double getFov()
    {
        return fov;
    }

    public double getNewFov()
    {
        return newFov;
    }

    public void setNewFov(double newFov)
    {
        this.newFov = newFov;
    }
}
