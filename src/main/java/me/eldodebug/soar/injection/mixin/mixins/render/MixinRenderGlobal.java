package me.eldodebug.soar.injection.mixin.mixins.render;

import java.util.Set;

import me.eldodebug.soar.management.mods.impl.ModESP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.eldodebug.soar.injection.interfaces.IMixinRenderGlobal;
import me.eldodebug.soar.injection.interfaces.IMixinVisGraph;
import me.eldodebug.soar.utils.EnumFacings;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal implements IMixinRenderGlobal {

    @Shadow
    private WorldClient theWorld;

    @Shadow
    private Framebuffer entityOutlineFramebuffer;

    @Shadow
    private ShaderGroup entityOutlineShader;

    @Shadow
    @Final
    private Minecraft mc;

    @Redirect(method = "setupTerrain", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    private EnumFacing[] setupTerrain$getCachedArray() {
        return EnumFacings.FACINGS;
    }
    
    @Inject(method = "getVisibleFacings", at = @At(value = "NEW", target = "Lnet/minecraft/client/renderer/chunk/VisGraph;<init>()V", shift = At.Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setLimitScan(CallbackInfoReturnable<Set<EnumFacing>> cir, VisGraph visgraph) {
        ((IMixinVisGraph) visgraph).setLimitScan(true);
    }

//    @Overwrite
//    protected boolean isRenderEntityOutlines()
//    {
//        return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && (this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown()) || ModESP.getInstance().isToggled();
//    }
    
	@Override
	public WorldClient getWorldClient() {
		return theWorld;
	}
}
