package powercraft.core;

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraftforge.common.Configuration;

import org.lwjgl.opengl.GL11;

import powercraft.core.PC_GresWidget.PC_GresAlign;

public class PCco_GuiUpdateNotification implements PC_IGresClient {

	private PC_GresCheckBox checkDisable;
	private PC_GresWidget buttonOK;
	private GuiScreen gs;
	
	public PCco_GuiUpdateNotification(EntityPlayer player, Object[] o){
		gs = (GuiScreen)o[0];
	}

	public PC_GresWidget getTabPage(PC_Module module){
		PC_GresScrollArea sa = new PC_GresScrollArea(PC_GresScrollArea.HSCROLL|PC_GresScrollArea.VSCROLL);
		sa.setSize(300, 100);
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabelMultiline(module.getUpdateText(), 210).setAlignH(PC_GresAlign.LEFT).setColor(PC_GresWidget.textColorEnabled, 0x555599).setColor(PC_GresWidget.textColorHover, 0x555599));
		sa.setChild(hg);
		return sa;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(240, 50, PC_Utils.tr("pc.gui.update.title"));
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresLayoutH hg;

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresImage(mod_PowerCraftCore.getInstance().getTextureDirectory() + "graphics.png", 0, 0, 195, 24));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabel(PC_Utils.tr("pc.gui.update.newVersionAvailable")));
		hg.add(new PC_GresLink(PC_Utils.tr("pc.gui.update.readMore")).setId(1));



		w.add(hg);
		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		/**
		 * @todo
		 * hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.update.version", new String[] { mod_PCcore.instance.getVersion(), Minecraft.getVersion(),
				mod_PCcore.updateModVersion, mod_PCcore.updateMcVersion })));
		 */
		w.add(hg);

		PC_GresTab t = new PC_GresTab();
		
		PC_GresWidget core = null;
		
		for(PC_Module module: PC_Module.getAllModules()){
			if(module.isUpdateAvailable()){
				String name = module.getNameWithoutPowerCraft();
				if(name.equalsIgnoreCase("Core")){
					t.addTab(core = getTabPage(module), new PC_GresLabel(name));
				}else{
					t.addTab(getTabPage(module), new PC_GresLabel(name));
				}
			}
		}
		
		if(core!=null)
			t.makeTabVisible(core);

		w.add(t);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(checkDisable = new PC_GresCheckBox(PC_Utils.tr("pc.gui.update.doNotShowAgain")));
		hg.add(new PC_GresGap(10, 0));
		hg.add(buttonOK = new PC_GresButton(PC_Utils.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		gui.setPausesGame(true);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			if (checkDisable.isChecked()) {
				PC_Module.ignoreALLUpdateVersion();
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			PC_ClientUtils.mc().currentScreen = gs;

		} else if (widget.getId() == 1) {
			try {
				Desktop.getDesktop().browse(URI.create("http://powercrafting.net/viewtopic.php?f=3&t=100"));
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		GL11.glDisable(GL11.GL_LIGHTING);
		PC_ClientUtils.mc().currentScreen = gs;
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
	public void onGuiClosed(PC_IGresGui gui) {
	}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		gs.drawScreen(-1, -1, par3);
		return true;
	}
	
}
