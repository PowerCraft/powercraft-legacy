package powercraft.weasel;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import powercraft.api.gres.PC_GresButton;
import powercraft.api.gres.PC_GresColorPicker;
import powercraft.api.gres.PC_GresInventory;
import powercraft.api.gres.PC_GresInventoryPlayer;
import powercraft.api.gres.PC_GresLabel;
import powercraft.api.gres.PC_GresTextEdit;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWidgetTab;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Color;

public class PCws_GuiWeaselDiskDrive extends PCws_ContainerWeaselDiskDrive
		implements PC_IGresClient {

	protected PC_GresTextEdit deviceName;
	protected PC_GresWidget deviceRename;
	protected PC_GresTextEdit networkName;
	protected PC_GresWidget network1, network2;
	protected PC_GresColorPicker networkColor;
	
	public PCws_GuiWeaselDiskDrive(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	protected void makeNetworkTab(PC_GresWindow win){
		
		PC_GresWidgetTab wt = new PC_GresWidgetTab(0xBBBBBB, "/gui/items.png", Item.paper.getIconFromDamage(0));
		wt.add(new PC_GresLabel("pc.gui.weasel.device.name"));
		wt.add(deviceName = new PC_GresTextEdit((String)tileEntity.getData("deviceName"), 10));
		wt.add(deviceRename = new PC_GresButton("pc.gui.weasel.device.rename"));
		win.add(wt);
		
		wt = new PC_GresWidgetTab(0x70360F, "/gui/items.png", Item.sign.getIconFromDamage(0));
		wt.add(new PC_GresLabel("pc.gui.weasel.network.name"));
		wt.add(networkName = new PC_GresTextEdit((String)tileEntity.getData("networkName"), 10));
		wt.add(network1 = new PC_GresButton("pc.gui.weasel.network.join"));
		wt.add(network2 = new PC_GresButton("pc.gui.weasel.network.new"));
		network1.setId(0);
		network2.enable(false);
		
		PC_Color color = (PC_Color)tileEntity.getData("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);
		wt.add(networkColor = new PC_GresColorPicker(color.getHex(), 100, 20));
		
		win.add(wt);
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PCws_App.weasel.getUnlocalizedName() + "." + tileEntity.getPluginInfo().getKey()+".name");
		
		makeNetworkTab(w);
		PC_GresInventory inv = new PC_GresInventory(4, 2);
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 4; i++) {
				inv.setSlot(i, j, invSlots[i+j*4]);
			}
		}
		w.add(inv);

		w.add(new PC_GresInventoryPlayer(true));
		
		gui.add(w);
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==deviceName){
			List<String> deviceNames = (List<String>)tileEntity.getData("deviceNames");
			deviceRename.enable(!deviceNames.contains(deviceName.getText()));
			if(deviceName.equals(""))
				deviceRename.enable(false);
		}else if(widget==deviceRename){
			tileEntity.call("deviceRename", deviceName.getText());
		}else if(widget==networkName){
			List<String> networkNames = (List<String>)tileEntity.getData("networkNames");
			if(networkNames.contains(networkName.getText())){
				network1.setText(PC_LangRegistry.tr("pc.gui.weasel.network.join"));
				network1.setId(0);
				network2.enable(false);
			}else{
				network1.setText(PC_LangRegistry.tr("pc.gui.weasel.network.rename"));
				network1.setId(1);
				network2.enable(true);
			}
			if(networkName.getText().equals("")){
				network2.enable(false);
				network1.setText(PC_LangRegistry.tr("pc.gui.weasel.network.join"));
				network1.setId(0);
			}
			network1.getParent().calcChildPositions();
		}else if(widget==network1){
			if(network1.getId()==0){
				tileEntity.call("networkJoin", networkName.getText());
			}else{
				tileEntity.call("networkRename", networkName.getText());
			}
		}else if(widget==network2){
			tileEntity.call("networkNew", networkName.getText());
		}else if(widget==networkColor){
			tileEntity.setData("color", PC_Color.fromHex(networkColor.getColor()));
		}
	}

	@Override
	public void onKeyPressed(PC_IGresGui gui, char c, int i) {
		if(i==Keyboard.KEY_ESCAPE || i==Keyboard.KEY_RETURN || i==Keyboard.KEY_E)
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

	@Override
	public void keyChange(String key, Object value) {}

}
