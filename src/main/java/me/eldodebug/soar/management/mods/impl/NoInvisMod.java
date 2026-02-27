package me.eldodebug.soar.management.mods.impl;

import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.Mod;
import me.eldodebug.soar.management.mods.ModCategory;
import me.eldodebug.soar.management.mods.settings.impl.ColorSetting;

import java.awt.*;

public class NoInvisMod extends Mod {

    private static NoInvisMod instance;

    public ColorSetting color = new ColorSetting(TranslateText.NO_INVIS_COLOR, this, new Color(1f, 1f, 1f, 0.4f), true);

    public NoInvisMod() {
        super(TranslateText.NO_INVIS_NAME, TranslateText.NO_INVIS_DESCRIPTION, ModCategory.RENDER);
        instance = this;
    }

    public static NoInvisMod getInstance() {
        return instance;
    }
}
