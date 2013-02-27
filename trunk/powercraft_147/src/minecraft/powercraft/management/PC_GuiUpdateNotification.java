package powercraft.management;

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresCheckBox;
import powercraft.management.gres.PC_GresGap;
import powercraft.management.gres.PC_GresImage;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLabelMultiline;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLink;
import powercraft.management.gres.PC_GresScrollArea;
import powercraft.management.gres.PC_GresTab;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.registry.PC_TextureRegistry;
import powercraft.management.tileentity.PC_TileEntity;

public class PC_GuiUpdateNotification implements PC_IGresClient {

	private PC_GresCheckBox checkDisable;
	private PC_GresWidget buttonOK;
	private GuiScreen gs;
	
	public PC_GuiUpdateNotification(EntityPlayer player, PC_TileEntity te, Object[] o){
		gs = (GuiScreen)o[0];
	}

	public PC_GresWidget getTabPage(String updateText){
		PC_GresScrollArea sa = new PC_GresScrollArea(PC_GresScrollArea.HSCROLL|PC_GresScrollArea.VSCROLL);
		sa.setSize(300, 100);
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabelMultiline(updateText, 210).setAlignH(PC_GresAlign.LEFT).setColor(PC_GresWidget.textColorEnabled, 0x555599).setColor(PC_GresWidget.textColorHover, 0x555599));
		sa.setChild(hg);
		return sa;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(240, 50, "pc.gui.update.title");
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresLayoutH hg;

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresImage(PC_TextureRegistry.getGresImgDir() + "graphics.png", 0, 0, 195, 24));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabel("pc.gui.update.newVersionAvailable"));
		hg.add(new PC_GresLink("pc.gui.update.readMore").setId(1));



		w.add(hg);
		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		/**
		 * @todo
		 * hg.add(new PC_GresLabel(PC_PC_LangRegistry.tr("pc.gui.update.version", new String[] { mod_PCcore.instance.getVersion(), Minecraft.getVersion(),
				mod_PCcore.updateModVersion, mod_PCcore.updateMcVersion })));
		 */
		w.add(hg);

		PC_GresTab t = new PC_GresTab();
		
		PC_GresWidget core = null;
		
		for(PC_Struct2<PC_IModule, String> module: PC_UpdateManager.getUpdateModuels()){
			String name = module.a.getName();
			if(name.equalsIgnoreCase("Core")){
				t.addTab(core = getTabPage(module.b), new PC_GresLabel(name));
			}else{
				t.addTab(getTabPage(module.b), new PC_GresLabel(name));
			}
		}
		
		for(PC_Struct3<String, String, String> module: PC_UpdateManager.getNewModuels()){
			String name = "NEW "+module.a;
			if(module.a.equalsIgnoreCase("Core")){
				t.addTab(core = getTabPage(module.c), new PC_GresLabel(name));
			}else{
				t.addTab(getTabPage(module.c), new PC_GresLabel(name));
			}
		}
		
		if(core!=null)
			t.makeTabVisible(core);

		w.add(t);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(checkDisable = new PC_GresCheckBox("pc.gui.update.doNotShowAgain"));
		hg.add(new PC_GresGap(10, 0));
		hg.add(buttonOK = new PC_GresButton("pc.gui.ok").setId(0));
		w.add(hg);

		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		gui.setPausesGame(true);
		
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			if (checkDisable.isChecked()) {
				PC_UpdateManager.ignoreALLUpdateVersion();
			}

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
		PC_ClientUtils.mc().currentScreen = gs;
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}
	
	@Override
	public void updateTick(PC_IGresGui gui) {
		PC_VecI c = gui.getSize();
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

	@Override
	public void keyChange(String key, Object value) {}
	
}
