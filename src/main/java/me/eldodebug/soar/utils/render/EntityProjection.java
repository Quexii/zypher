package me.eldodebug.soar.utils.render;

import me.eldodebug.soar.Glide;
import me.eldodebug.soar.injection.interfaces.IMixinRenderManager;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventRender2D;
import me.eldodebug.soar.management.event.impl.EventRender3D;
import me.eldodebug.soar.types.Rect;
import me.eldodebug.soar.utils.proj.Projection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EntityProjection {
    private static EntityProjection instance;

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final float SMOOTHING_FACTOR = 0.85f;

    private final Map<Entity, Rect> positionMap = new HashMap<>();
    private final Map<Entity, Rect> prevScreenPositions = new HashMap<>();
    private final Map<Entity, Vec3> prevPositions = new HashMap<>();
    private final Map<Entity, Vec3> currPositions = new HashMap<>();

    public EntityProjection() {
        instance = this;
        Glide.getInstance().getEventManager().register(this);
    }

    public static EntityProjection getInstance() {
        return instance;
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (mc.theWorld == null || mc.getRenderManager() == null) return;

        positionMap.clear();
        prevScreenPositions.clear();
        prevPositions.clear();
        currPositions.clear();

        float partialTicks = event.getPartialTicks();
        IMixinRenderManager renderManager = ((IMixinRenderManager) mc.getRenderManager());

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;

            AxisAlignedBB bb = entity.getEntityBoundingBox();

            Vec3 prev = prevPositions.getOrDefault(entity, new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ));
            Vec3 curr = currPositions.getOrDefault(entity, new Vec3(entity.posX, entity.posY, entity.posZ));
            double interpX = prev.xCoord + (curr.xCoord - prev.xCoord) * partialTicks;
            double interpY = prev.yCoord + (curr.yCoord - prev.yCoord) * partialTicks;
            double interpZ = prev.zCoord + (curr.zCoord - prev.zCoord) * partialTicks;

            Vec3[] corners = new Vec3[]{
                    new Vec3(bb.minX - entity.posX + interpX, bb.minY - entity.posY + interpY, bb.minZ - entity.posZ + interpZ),
                    new Vec3(bb.minX - entity.posX + interpX, bb.maxY - entity.posY + interpY, bb.minZ - entity.posZ + interpZ),
                    new Vec3(bb.maxX - entity.posX + interpX, bb.maxY - entity.posY + interpY, bb.minZ - entity.posZ + interpZ),
                    new Vec3(bb.maxX - entity.posX + interpX, bb.minY - entity.posY + interpY, bb.minZ - entity.posZ + interpZ),
                    new Vec3(bb.minX - entity.posX + interpX, bb.minY - entity.posY + interpY, bb.maxZ - entity.posZ + interpZ),
                    new Vec3(bb.minX - entity.posX + interpX, bb.maxY - entity.posY + interpY, bb.maxZ - entity.posZ + interpZ),
                    new Vec3(bb.maxX - entity.posX + interpX, bb.maxY - entity.posY + interpY, bb.maxZ - entity.posZ + interpZ),
                    new Vec3(bb.maxX - entity.posX + interpX, bb.minY - entity.posY + interpY, bb.maxZ - entity.posZ + interpZ)
            };

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;

            boolean isInFront = false;
            int validProjections = 0;

            for (Vec3 corner : corners) {
                float x = (float) (corner.xCoord - renderManager.getRenderPosX());
                float y = (float) (corner.yCoord - renderManager.getRenderPosY());
                float z = (float) (corner.zCoord - renderManager.getRenderPosZ());

                Vec3 screenPos = Projection.w2s(x, y, z);

                if (screenPos.xCoord != 0f || screenPos.yCoord != 0f) {
                    if (screenPos.zCoord < 1.0f) {
                        isInFront = true;
                        validProjections++;

                        minX = (float) Math.min(minX, screenPos.xCoord);
                        minY = (float) Math.min(minY, screenPos.yCoord);
                        maxX = (float) Math.max(maxX, screenPos.xCoord);
                        maxY = (float) Math.max(maxY, screenPos.yCoord);
                    }
                }
            }

            if (isInFront && validProjections >= 2 && minX < Float.MAX_VALUE && maxX > Float.MIN_VALUE) {
                if (maxX >= 0 && minX <= mc.displayWidth &&
                        maxY >= 0 && minY <= mc.displayHeight) {

                    float width = maxX - minX;
                    float height = maxY - minY;

                    if (width > 1 && height > 1 && width < mc.displayWidth && height < mc.displayHeight) {
                        Rect newRect = new Rect(minX, minY, width, height);
                        Rect prevRect = prevScreenPositions.get(entity);

                        if (prevRect != null) {
                            float smoothX = prevRect.x + (newRect.x - prevRect.x) * (1f - SMOOTHING_FACTOR);
                            float smoothY = prevRect.y + (newRect.y - prevRect.y) * (1f - SMOOTHING_FACTOR);
                            float smoothW = prevRect.width + (newRect.width - prevRect.width) * (1f - SMOOTHING_FACTOR);
                            float smoothH = prevRect.height + (newRect.height - prevRect.height) * (1f - SMOOTHING_FACTOR);
                            positionMap.put(entity, new Rect(smoothX, smoothY, smoothW, smoothH));
                            prevScreenPositions.put(entity, new Rect(smoothX, smoothY, smoothW, smoothH));
                        } else {
                            positionMap.put(entity, newRect);
                            prevScreenPositions.put(entity, newRect);
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        if (mc.theWorld == null) return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;
            prevPositions.put(entity, currPositions.getOrDefault(entity, new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ)));
            currPositions.put(entity, new Vec3(entity.posX, entity.posY, entity.posZ));
        }

        Iterator<Map.Entry<Entity, Rect>> it = prevScreenPositions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Entity, Rect> entry = it.next();
            if (!positionMap.containsKey(entry.getKey())) {
                it.remove();
            }
        }
    }

    public Map<Entity, Rect> getPositionMap() {
        return positionMap;
    }

    public Rect getScreenPosition(Entity entity) {
        return positionMap.get(entity);
    }
}
