package me.eldodebug.soar.gui.mainmenu.impl;

import java.awt.Color;

import eu.shoroa.contrib.render.Blur;
import me.eldodebug.soar.Glide;
import me.eldodebug.soar.gui.mainmenu.GuiGlideMainMenu;
import me.eldodebug.soar.gui.mainmenu.MainMenuScene;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.nanovg.NanoVGManager;
import me.eldodebug.soar.management.nanovg.font.Fonts;
import me.eldodebug.soar.management.nanovg.font.Icons;
import me.eldodebug.soar.utils.mouse.MouseUtils;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.nanovg.NanoVG;

public class MainScene extends MainMenuScene {

	public MainScene(GuiGlideMainMenu parent) {
		super(parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Glide instance = Glide.getInstance();
		if(instance.getSoar8Released()){
			instance.setSoar8Released(false);
			this.setCurrentScene(this.getSceneByClass(DiscontinuedSoar8.class));
		} else if(instance.getUpdateNeeded()){
			instance.setUpdateNeeded(false);
			this.setCurrentScene(this.getSceneByClass(UpdateScene.class));
		}
		NanoVGManager nvg = instance.getNanoVGManager();

		nvg.setupAndDraw(() -> drawNanoVG(nvg));
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		float yPos = sr.getScaledHeight() / 2f - 22;

		nvg.drawBlurredText(Icons.GLIDE, sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f - (nvg.getTextHeight(Icons.GLIDE, 54, Fonts.GLICONIC) / 2) - 60, new Color(0,0,0,180), 8f, 54, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Fonts.GLICONIC);
		nvg.drawCenteredText(Icons.GLIDE, sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f - (nvg.getTextHeight(Icons.GLIDE, 54, Fonts.GLICONIC) / 2) - 60, Color.WHITE, 54, Fonts.GLICONIC);

		Blur.drawBlur(sr.getScaledWidth() / 2f - (180 / 2f), yPos, 180, 20, 4.5F);
		nvg.drawRoundedRect(sr.getScaledWidth() / 2f - (180 / 2f), yPos, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawBlurredText(TranslateText.SINGLEPLAYER.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F, Color.BLACK, 12f, 9.5F, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Fonts.SEMIBOLD);
		nvg.drawCenteredText(TranslateText.SINGLEPLAYER.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F, Color.WHITE, 9.5F, Fonts.SEMIBOLD);

		Blur.drawBlur(sr.getScaledWidth() / 2f - (180 / 2f), yPos + 26, 180, 20, 4.5F);
		nvg.drawRoundedRect(sr.getScaledWidth() / 2f - (180 / 2f), yPos + 26, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawBlurredText(TranslateText.MULTIPLAYER.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F + 26, Color.BLACK, 12f, 9.5F, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Fonts.SEMIBOLD);
		nvg.drawCenteredText(TranslateText.MULTIPLAYER.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F + 26, Color.WHITE, 9.5F, Fonts.SEMIBOLD);

		Blur.drawBlur(sr.getScaledWidth() / 2f - (180 / 2f), yPos + (26 * 2), 180, 20, 4.5F);
		nvg.drawRoundedRect(sr.getScaledWidth() / 2f - (180 / 2f), yPos + (26 * 2), 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawBlurredText(TranslateText.SETTINGS.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F + (26 * 2), Color.BLACK, 12f, 9.5F, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE, Fonts.SEMIBOLD);
		nvg.drawCenteredText(TranslateText.SETTINGS.getText(), sr.getScaledWidth() / 2f, yPos + 6.5F + (26 * 2), Color.WHITE, 9.5F, Fonts.SEMIBOLD);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		float yPos = sr.getScaledHeight() / 2 - 22;
		
		if(mouseButton == 0) {
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (160 / 2), yPos, 160, 20)) {
				mc.displayGuiScreen(new GuiSelectWorld(this.getParent()));
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20)) {
				mc.displayGuiScreen(new GuiMultiplayer(this.getParent()));
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20)) {
				mc.displayGuiScreen(new GuiOptions(this.getParent(), mc.gameSettings));
			}
		}
	}
}
