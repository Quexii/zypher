package me.eldodebug.soar.injection.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface IMixinRenderEntity {
    ResourceLocation entityTexture(Entity entity);
}
