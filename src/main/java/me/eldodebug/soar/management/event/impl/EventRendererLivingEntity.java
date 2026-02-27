package me.eldodebug.soar.management.event.impl;

import me.eldodebug.soar.management.event.Event;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRendererLivingEntity extends Event {
	
	private RendererLivingEntity<EntityLivingBase> renderer;
	private Entity entity;
	private double x, y, z;
	private float limbSwing, limbSwingAmount, partialTicks, renderYawOffset, scaleFactor;

	public EventRendererLivingEntity(RendererLivingEntity<EntityLivingBase> renderer, Entity entity, double x, double y, double z, float limbSwing, float limbSwingAmount, float partialTicks, float renderYawOffset, float scaleFactor) {
		this.renderer = renderer;
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.partialTicks = partialTicks;
		this.renderYawOffset = renderYawOffset;
		this.scaleFactor = scaleFactor;
	}

	public RendererLivingEntity<EntityLivingBase> getRenderer() {
		return renderer;
	}

	public Entity getEntity() {
		return entity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getLimbSwing() {
		return limbSwing;
	}

	public float getLimbSwingAmount() {
		return limbSwingAmount;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public float getRenderYawOffset() {
		return renderYawOffset;
	}

	public float getScaleFactor() {
		return scaleFactor;
	}
}