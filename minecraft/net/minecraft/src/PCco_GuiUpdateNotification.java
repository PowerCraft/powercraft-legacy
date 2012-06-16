package net.minecraft.src;

import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Gui notifying about an update.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCco_GuiUpdateNotification extends GuiScreen {

	private PC_GuiCheckBox checkDisable;

	private int halfX;
	private int halfY;

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		halfX = width / 2;
		halfY = height / 2;

		Keyboard.enableRepeatEvents(true);
		controlList.clear();



		HashMap<Integer, String> btns = new HashMap<Integer, String>();
		btns.put(0, "pc.gui.ok");
		PC_GuiButtonAligner.alignToRight(controlList, btns, 60, 4, halfY + 75 - 30, halfX + 120 - 10);



		String title = PC_Lang.tr("pc.gui.update.newVersionAvailable");

		String link = PC_Lang.tr("pc.gui.update.readMore");

		GuiButton but2 = (new PC_GuiClickableText(fontRenderer, 1, (halfX + ((fontRenderer.getStringWidth(title + " " + link)) / 2))
				- fontRenderer.getStringWidth(link), halfY - 75 + 32, link));
		controlList.add(but2);

		checkDisable = new PC_GuiCheckBox(this, fontRenderer, halfX - 110, halfY + 75 - 30 + 6, false,
				PC_Lang.tr("pc.gui.update.doNotShowAgain"));
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }
		if (guibutton.id == 0) {

			if (checkDisable.isChecked()) {
				PC_PropertyManager cfg = mod_PCcore.instance.cfg();

				cfg.setValue(mod_PCcore.pk_cfgUpdateIgnored, mod_PCcore.updateModVersion);
				System.out.println("Setting value " + mod_PCcore.updateModVersion);

				cfg.enableValidation(false);

				cfg.apply();

				cfg.enableValidation(true);

				mod_PCcore.update_last_ignored_version = mod_PCcore.instance.cfg().string(mod_PCcore.pk_cfgUpdateIgnored);
			}

			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
		if (guibutton.id == 1) {
			try {
				Desktop.getDesktop().browse(
						URI.create("http://www.minecraftforum.net/topic/842589-125-power-craft-factory-mod/#entry10831808"));
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		checkDisable.mouseClicked(i, j, k);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiBackgroundLayer(f);

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String title = PC_Lang.tr("pc.gui.update.newVersionAvailable");

		String link = PC_Lang.tr("pc.gui.update.readMore");

		fontRenderer.drawString(title, halfX - (fontRenderer.getStringWidth(title + " " + link) / 2), halfY - 75 + 34, 0x303060);

		String aa;

		aa = PC_Lang
				.tr("pc.gui.update.version",
						new String[] { mod_PCcore.instance.getVersion(), Minecraft.getVersion(), mod_PCcore.updateModVersion, mod_PCcore.updateMcVersion });

		fontRenderer.drawString(aa, halfX - (fontRenderer.getStringWidth(aa) / 2), halfY - 75 + 34 + 14, 0x303060);

		@SuppressWarnings("unchecked")
		List<String> lines = fontRenderer.listFormattedStringToWidth(mod_PCcore.updateText, 220);

		int cnt = 0;
		for (String s : lines) {
			if (s.length() > 1) {
				fontRenderer.drawString(s, halfX - 110, halfY - 75 + 34 + 32 + 12 * cnt, 0x000000);
				cnt++;
			}
		}

		checkDisable.drawCheckBox();


		GL11.glPopMatrix();

		super.drawScreen(i, j, f);

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-update.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width) / 2;
		int k = (height) / 2;
		drawTexturedModalRect(j - 120, k - 75, 0, 0, 240, 150);
	}
}
