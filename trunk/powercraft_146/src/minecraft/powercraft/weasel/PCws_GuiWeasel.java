package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_Color;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresColorPicker;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresTab;
import powercraft.management.PC_GresTextEdit;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;

public abstract class PCws_GuiWeasel implements PC_IGresClient {

	protected PCws_TileEntityWeasel te;
	protected PC_GresWidget ok, cancel;
	protected PC_GresTextEdit diviceName;
	protected PC_GresWidget diviceRename;
	protected PC_GresTextEdit networkName;
	protected PC_GresWidget network1, network2;
	protected PC_GresColorPicker networkColor;
	
	public PCws_GuiWeasel(EntityPlayer player, Object[] o){
		te = GameInfo.getTE(player.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr(PCws_App.weasel.getItemBlock().getItemName() + "." + te.getPluginInfo().getKey()));
		
		PC_GresTab tab = new PC_GresTab();
		makeNetworkTab(tab);
		addTabs(tab);
		
		w.add(tab);
		
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.setAlignH(PC_GresAlign.JUSTIFIED);
		lh.add(ok = new PC_GresButton(Lang.tr("pc.gui.ok")));
		lh.add(cancel = new PC_GresButton(Lang.tr("pc.gui.cnacel")));
		w.add(lh);
		
		gui.add(w);
	}
	
	protected void makeNetworkTab(PC_GresTab tab){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel("pc.gui.weasel.divice.name"));
		lh.add(diviceName = new PC_GresTextEdit((String)te.getData("diviceName"), 10));
		lv.add(lh);
		lv.add(diviceRename = new PC_GresButton(Lang.tr("pc.gui.weasel.divice.rename")));
		lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel("pc.gui.weasel.network.name"));
		lh.add(networkName = new PC_GresTextEdit((String)te.getData("networkName"), 10));
		lv.add(lh);
		lh = new PC_GresLayoutH();
		lh.add(network1 = new PC_GresButton(Lang.tr("pc.gui.weasel.network.join")));
		lh.add(network2 = new PC_GresButton(Lang.tr("pc.gui.weasel.network.new")));
		network2.enable(false);
		lv.add(lh);
		PC_Color color = (PC_Color)te.getData("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);
		lv.add(networkColor = new PC_GresColorPicker(color.getHex(), 100, 20));
		
		tab.addTab(lv, new PC_GresLabel("pc.gui.weasel.network"));
	}
	
	protected abstract void addTabs(PC_GresTab tab);
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==ok){
			onReturnPressed(gui);
		}else if(widget==cancel){
			onEscapePressed(gui);
		}else if(widget==diviceRename){
			te.setData("diviceName", diviceName.getText());
		}else if(widget==networkName){
			
		}
	}
	
	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

}
