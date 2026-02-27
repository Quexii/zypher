package me.eldodebug.soar.injection.interfaces;

import net.minecraft.entity.Entity;

import java.util.List;

public interface IMixinWorld {
	boolean isLoaded(int x, int z, boolean allowEmpty);
	List<Entity> getLoadedEntityList();
}
