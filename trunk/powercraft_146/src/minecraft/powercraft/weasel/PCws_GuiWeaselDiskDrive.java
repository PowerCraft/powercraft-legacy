package powercraft.weasel;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_Color;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresColorPicker;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryPlayer;
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
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;

public class PCws_GuiWeaselDiskDrive extends PCws_ContainerWeaselDiskDrive
		implements PC_IGresClient {

	protected PC_GresWidget ok, cancel;
	protected PC_GresTextEdit deviceName;
	protected PC_GresWidget deviceRename;
	protected PC_GresTextEdit networkName;
	protected PC_GresWidget network1, network2;
	protected PC_GresColorPicker networkColor;
	
	public PCws_GuiWeaselDiskDrive(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	protected void makeNetworkTab(PC_GresTab tab){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.device.name")));
		lh.add(deviceName = new PC_GresTextEdit((String)te.getData("deviceName"), 10));
		lv.add(lh);
		lv.add(deviceRename = new PC_GresButton(Lang.tr("pc.gui.weasel.device.rename")));
		lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.network.name")));
		lh.add(networkName = new PC_GresTextEdit((String)te.getData("networkName"), 10));
		lv.add(lh);
		lh = new PC_GresLayoutH();
		lh.add(network1 = new PC_GresButton(Lang.tr("pc.gui.weasel.network.join")));
		lh.add(network2 = new PC_GresButton(Lang.tr("pc.gui.weasel.network.new")));
		network1.setId(0);
		network2.enable(false);
		lv.add(lh);
		PC_Color color = (PC_Color)te.getData("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);
		lv.add(networkColor = new PC_GresColorPicker(color.getHex(), 100, 20));
		
		tab.addTab(lv, new PC_GresLabel(Lang.tr("pc.gui.weasel.network.tab")));
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr(PCws_App.weasel.getBlockName() + "." + te.getPluginInfo().getKey()+".name"));
		
		PC_GresTab tab = new PC_GresTab();
		makeNetworkTab(tab);
		addTabs(tab);
		
		w.add(tab);
		
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.setAlignH(PC_GresAlign.JUSTIFIED);
		lh.add(ok = new PC_GresButton(Lang.tr("pc.gui.ok")));
		lh.add(cancel = new PC_GresButton(Lang.tr("pc.gui.cancel")));
		w.add(lh);
		
		gui.add(w);
	}

	protected void addTabs(PC_GresTab tab){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		PC_GresInventory inv = new PC_GresInventory(4, 2);
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 4; i++) {
				inv.setSlot(lSlot.get(i+j*4), i, j);
			}
		}
		lv.add(inv);

		lv.add(new PC_GresInventoryPlayer(true));
		tab.addTab(lv, new PC_GresLabel(Lang.tr("pc.gui.weasel.diskDrive.tab")));
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==ok){
			onReturnPressed(gui);
		}else if(widget==cancel){
			onEscapePressed(gui);
		}else if(widget==deviceName){
			List<String> deviceNames = (List<String>)te.getData("deviceNames");
			deviceRename.enable(!deviceNames.contains(deviceName.getText()));
			if(deviceName.equals(""))
				deviceRename.enable(false);
		}else if(widget==deviceRename){
			PC_PacketHandler.setTileEntity(te, "msg", "deviceRename", deviceName.getText());
		}else if(widget==networkName){
			List<String> networkNames = (List<String>)te.getData("networkNames");
			if(networkNames.contains(networkName.getText())){
				network1.setText(Lang.tr("pc.gui.weasel.network.join"));
				network1.setId(0);
				network2.enable(false);
			}else{
				network1.setText(Lang.tr("pc.gui.weasel.network.rename"));
				network1.setId(1);
				network2.enable(true);
			}
			if(networkName.getText().equals("")){
				network2.enable(false);
				network1.setText(Lang.tr("pc.gui.weasel.network.join"));
				network1.setId(0);
			}
		}else if(widget==network1){
			if(network1.getId()==0){
				PC_PacketHandler.setTileEntity(te, "msg", "networkJoin", networkName.getText());
			}else{
				PC_PacketHandler.setTileEntity(te, "msg", "networkRename", networkName.getText());
			}
		}else if(widget==network2){
			PC_PacketHandler.setTileEntity(te, "msg", "networkNew", networkName.getText());
		}else if(widget==networkColor){
			te.setData("color", PC_Color.fromHex(networkColor.getColor()));
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
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
