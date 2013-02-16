package powercraft.weasel;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_Color;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresColorPicker;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresTab;
import powercraft.management.gres.PC_GresTextEdit;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;

public class PCws_GuiWeaselOnlyNet implements PC_IGresClient {

	protected PCws_TileEntityWeasel te;
	protected PC_GresWidget ok, cancel;
	protected PC_GresTextEdit deviceName;
	protected PC_GresWidget deviceRename;
	protected PC_GresTextEdit networkName;
	protected PC_GresWidget network1, network2;
	protected PC_GresColorPicker networkColor;
	
	public PCws_GuiWeaselOnlyNet(EntityPlayer player, PC_TileEntity te, Object[] o){
		this.te = (PCws_TileEntityWeasel)te;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr(PCws_App.weasel.getBlockName() + "." + te.getPluginInfo().getKey()+".name"));
		
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
		
		w.add(lv);
		
		lh = new PC_GresLayoutH();
		lh.setAlignH(PC_GresAlign.JUSTIFIED);
		lh.add(ok = new PC_GresButton(Lang.tr("pc.gui.ok")));
		lh.add(cancel = new PC_GresButton(Lang.tr("pc.gui.cancel")));
		w.add(lh);
		
		gui.add(w);
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
			te.call("deviceRename", deviceName.getText());
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
			network1.getParent().calcChildPositions();
		}else if(widget==network1){
			if(network1.getId()==0){
				te.call("networkJoin", networkName.getText());
			}else{
				te.call("networkRename", networkName.getText());
			}
		}else if(widget==network2){
			te.call("networkNew", networkName.getText());
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

	@Override
	public void keyChange(String key, Object value) {}

}
