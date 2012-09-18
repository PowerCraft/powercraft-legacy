package net.minecraft.src;


import java.awt.Desktop;
import java.net.URI;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui notifying about an update.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_GuiUpdateNotification extends PC_GresBase {

	private PC_GresCheckBox checkDisable;
	private PC_GresWidget buttonOK;
	private GuiScreen gs;
	
	public PCco_GuiUpdateNotification(GuiScreen gs){
		this.gs = gs;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(240, 50, PC_Lang.tr("pc.gui.update.title"));
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresLayoutH hg;

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "graphics.png", 0, 0, 195, 24));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.update.newVersionAvailable")));
		hg.add(new PC_GresLink(PC_Lang.tr("pc.gui.update.readMore")).setId(1));



		w.add(hg);
		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		/**
		 * @todo
		 * hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.update.version", new String[] { mod_PCcore.instance.getVersion(), Minecraft.getVersion(),
				mod_PCcore.updateModVersion, mod_PCcore.updateMcVersion })));
		 */
		w.add(hg);

		w.add(new PC_GresSeparatorH(40, 5).setLineColor(0x999999));

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabelMultiline(mod_PCcore.updateText, 210).setAlignH(PC_GresAlign.LEFT).setColor(PC_GresWidget.textColorEnabled, 0x555599));
		w.add(hg);

		w.add(new PC_GresSeparatorH(40, 5).setLineColor(0x999999));

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(checkDisable = new PC_GresCheckBox(PC_Lang.tr("pc.gui.update.doNotShowAgain")));
		hg.add(new PC_GresGap(10, 0));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		gui.setPausesGame(true);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			if (checkDisable.isChecked()) {
				PC_PropertyManager cfg = mod_PCcore.instance.cfg();

				cfg.setValue(mod_PCcore.pk_cfgUpdateIgnoredSerVersion, mod_PCcore.updateModVersionSerial);
				PC_Logger.finest("Setting last shown update version to: " + mod_PCcore.updateModVersionSerial);

				cfg.enableValidation(false);

				cfg.apply();

				cfg.enableValidation(true);

				mod_PCcore.update_last_ignored_version_serial = mod_PCcore.updateModVersionSerial;
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			PC_Utils.mc().displayGuiScreen(gs);
			
			//gui.close();

		} else if (widget.getId() == 1) {
			try {
				Desktop.getDesktop().browse(URI.create("http://www.minecraftforum.net/topic/842589-125-power-craft-factory-mod/#entry10831808"));
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		GL11.glDisable(GL11.GL_LIGHTING);
		PC_Utils.mc().displayGuiScreen(gs);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}
	
	@Override
	public void updateTick(PC_IGresGui gui) {
		PC_CoordI c = gui.getSize();
		gs.width = c.x;
		gs.height = c.y;
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		gs.updateScreen();
	}
	
	@Override
	public boolean drawBackground(GuiScreen g, PC_IGresGui gui, int par1, int par2, float par3){
		gs.drawScreen(par1, par2, par3);
		g.drawString(g.fontRenderer, ", PowerCraft " + mod_PCcore.instance.getVersion(), 2 + g.fontRenderer.getStringWidth("Minecraft 1.3.2"), g.height - 10,
				0xffffffff);
		return true;
	}
	
}
