package me.eldodebug.soar.management.mods.impl;

import eu.shoroa.contrib.render.Blur;
import me.eldodebug.soar.Glide;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventPreRender2D;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.Mod;
import me.eldodebug.soar.management.mods.ModCategory;
import me.eldodebug.soar.management.mods.settings.impl.BooleanSetting;
import me.eldodebug.soar.management.mods.settings.impl.NumberSetting;
import me.eldodebug.soar.management.nanovg.NvRenderer;
import me.eldodebug.soar.management.nanovg.font.Fonts;
import me.eldodebug.soar.types.Color;
import me.eldodebug.soar.utils.render.EntityProjection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.Display;

public class ModESP extends Mod {
    private static ModESP instance;

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
    private void onRender2D(EventPreRender2D event) {
        if (mc.theWorld == null) return;

        NvRenderer renderer = Glide.getInstance().getNanoVGManager();

        renderer.setupAndDraw(() -> {
            renderer.scissor(1f, 1f, Display.getWidth() - 2f, Display.getHeight() - 2f);

            Color a = new Color(195, 145, 145, 255);
            Color b = new Color(95, 55, 55, 255);

            EntityProjection.getInstance().getPositionMap().forEach((entity, rect) -> {
                boolean isSelf = entity == mc.thePlayer;
                boolean inRange = entity.getDistanceToEntity(mc.thePlayer) > range.getValueFloat();
                boolean isPlayer = entity instanceof EntityPlayer && !showPlayers.isToggled();
                boolean isMob = entity instanceof EntityMob && !showMobs.isToggled();
                boolean isAnimal = entity instanceof EntityAnimal && !showAnimals.isToggled();
                boolean isOther = !(entity instanceof EntityPlayer) && !(entity instanceof EntityMob) && !(entity instanceof EntityAnimal) && !showOther.isToggled();

                float distance = entity.getDistanceToEntity(mc.thePlayer);
                float clamped = 1f - (distance / range.getValueFloat());

                if (!(isSelf || inRange || isPlayer || isMob || isAnimal || isOther)) {
                    renderer.stroke(rect, 0xFF000000, 3.5f);
                    renderer.linearStroke(rect, rect.x, rect.y, rect.x, rect.y + rect.height, a.toARGB(), b.toARGB(), 1f);

                    if (entity instanceof EntityLivingBase) {
                        float healthPercent = ((EntityLivingBase) entity).getHealth() / ((EntityLivingBase) entity).getMaxHealth();
                        renderer.drawRect(rect.x - 6, rect.y - 2, 3, rect.height + 4, 0xFF000000);
                        renderer.drawRect(rect.x - 5, rect.y - 1 + (1 - healthPercent) * (rect.height + 2), 1, healthPercent * (rect.height + 2), 0xFF00FF00);
                    }

                    renderer.drawBlurredText(entity.getName(), rect.x + rect.width / 2f, rect.y - 8, 0xFF000000, 1f, 10f, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Fonts.SEMIBOLD);
                    renderer.drawCenteredText(entity.getName(), rect.x + rect.width / 2f, rect.y - 8, 0xFFFFFFFF, 10f, Fonts.SEMIBOLD);
                }
            });
            renderer.resetScissor();
        }, false);
    }
}
