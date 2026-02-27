package me.eldodebug.soar.management.mods.impl;

import eu.shoroa.contrib.data.Rect;
import me.eldodebug.soar.Glide;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventRender2D;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.Mod;
import me.eldodebug.soar.management.mods.ModCategory;
import me.eldodebug.soar.management.mods.settings.impl.BooleanSetting;
import me.eldodebug.soar.management.mods.settings.impl.NumberSetting;
import me.eldodebug.soar.management.nanovg.NanoVGManager;
import me.eldodebug.soar.management.nanovg.font.Fonts;
import me.eldodebug.soar.utils.MathUtils;
import me.eldodebug.soar.utils.render.EntityProjection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModESP extends Mod {
    private static ModESP instance;

    private final Map<Entity, String> entityData = new HashMap<>();

    private NumberSetting range = new NumberSetting(TranslateText.RANGE, this, 100, 10, 500, true);
    private BooleanSetting showPlayers = new BooleanSetting(TranslateText.PLAYER_LIST, this, true);
    private BooleanSetting showAnimals = new BooleanSetting(TranslateText.MOBS, this, true);
    private BooleanSetting showMobs = new BooleanSetting(TranslateText.MOBS, this, true);
    private BooleanSetting showOther = new BooleanSetting(TranslateText.OTHER, this, true);

    public ModESP() {
        super(TranslateText.ESP_NAME, TranslateText.ESP_DESCRIPTION, ModCategory.RENDER);
        instance = this;
    }

    public static ModESP getInstance() {
        return instance;
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        if (mc.theWorld == null) return;

        EntityProjection projection = EntityProjection.getInstance();
        Map<Entity, Rect> positionMap = projection.getPositionMap();

        entityData.clear();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;
            if (entity.getDistanceToEntity(mc.thePlayer) > range.getValueFloat()) continue;

            if (entity instanceof EntityPlayer && !showPlayers.isToggled()) continue;
            if (entity instanceof EntityMob && !showMobs.isToggled()) continue;
            if (entity instanceof EntityAnimal && !showAnimals.isToggled()) continue;
            if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityMob) && !(entity instanceof EntityAnimal) && !showOther.isToggled())
                continue;

            int distance = (int) entity.getDistanceToEntity(mc.thePlayer);
            String name = (entity instanceof EntityPlayer) ? entity.getName() : entity.getClass().getSimpleName();
            String info = name + " (" + distance + "m)";
            entityData.put(entity, info);
        }

        NanoVGManager nvg = Glide.getInstance().getNanoVGManager();
        ScaledResolution sr = new ScaledResolution(mc);
        float scaleFactor = sr.getScaleFactor();

        nvg.setupAndDraw(() -> {
            positionMap.forEach((entity, rect) -> {
                if (!entityData.containsKey(entity)) return;

                float x = rect.x / scaleFactor;
                float y = rect.y / scaleFactor;
                float width = rect.width / scaleFactor;
                float height = rect.height / scaleFactor;

                Color boxColor = Color.WHITE;
                if (entity instanceof EntityPlayer) {
                    boxColor = new Color(255, 85, 85);
                } else if (entity instanceof EntityMob) {
                    boxColor = new Color(255, 100, 100);
                } else if (entity instanceof EntityAnimal) {
                    boxColor = new Color(85, 255, 85);
                }

                String info = entityData.get(entity);
                String[] parts = info.split(" ");
                int distance = Integer.parseInt(parts[1].replace("(", "").replace("m)", ""));

                float alpha = MathUtils.clamp(1f - (distance / range.getValueFloat()), 0.2f, 1f);

                float distanceRatio = distance / range.getValueFloat();
                float textScale = MathUtils.clamp(1.5f - (distanceRatio * 1.0f), 0.5f, 1.5f);
                float fontSize = 7f * textScale;
                float glowSize = 2f * textScale;

                float infoWidth = nvg.getTextWidth(info, fontSize, Fonts.REGULAR);
                float boxHeight = 12f * textScale;
                float boxPadding = 2f * textScale;
                float textOffsetY = 16f * textScale;

                nvg.save();
                nvg.setAlpha(alpha);
                nvg.drawOutlineRoundedRect(x,y,width,height,0f,1f,Color.BLACK);
                nvg.drawOutlineRoundedRect(x,y,width,height,0f,0.5f,boxColor);

                if (entity instanceof EntityLivingBase) {
                    float health = ((EntityLivingBase) entity).getHealth() / ((EntityLivingBase) entity).getMaxHealth();

                    nvg.drawRect(x - 3, y, 2, height, Color.BLACK);
                    nvg.drawRect(x - 2.5f, y + 0.5f + height - (height) * health, 1f, (height - 1) * health, Color.GREEN);

                    int armor = ((EntityLivingBase) entity).getTotalArmorValue();

                    if (armor > 0) {
                        float armorHeight = (height / 20f) * armor;
                        nvg.drawRect(x + width + 1, y + height - armorHeight, 2, armorHeight, Color.BLUE);
                        nvg.drawRect(x + width + 1.5f, y + height - armorHeight + 0.5f, 1f, armorHeight - 1f, Color.CYAN);
                    }
                }

                nvg.drawTextGlowing(info, x + (width / 2f) - (infoWidth / 2f), y - textOffsetY + (boxHeight - fontSize) / 2f, Color.BLACK, glowSize, fontSize, Fonts.REGULAR);
                nvg.drawText(info, x + (width / 2f) - (infoWidth / 2f), y - textOffsetY + (boxHeight - fontSize) / 2f, Color.WHITE, fontSize, Fonts.REGULAR);
                nvg.restore();
            });
        });
    }
}
