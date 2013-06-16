package powercraft.mobile;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import powercraft.api.gres.PC_GresButton;
import powercraft.api.gres.PC_GresColorPicker;
import powercraft.api.gres.PC_GresLabel;
import powercraft.api.gres.PC_GresLayoutH;
import powercraft.api.gres.PC_GresLayoutV;
import powercraft.api.gres.PC_GresTab;
import powercraft.api.gres.PC_GresTextEdit;
import powercraft.api.gres.PC_GresTextEditMultiline;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Color;

public class PCmo_GuiWeaselMiner extends PCmo_GuiMiner {

	private PC_GresTextEditMultiline cons;
	private PC_GresTextEdit inp;
	private PC_GresButton send;
	private PC_GresTextEdit deviceName;
	private PC_GresWidget deviceRename;
	private PC_GresTextEdit networkName;
	private PC_GresWidget network1, network2;
	private PC_GresColorPicker networkColor;
	
	public PCmo_GuiWeaselMiner(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	private void makeTerminalTab(PC_GresTab tab){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.add(cons = new PC_GresTextEditMultiline(((String)miner.getInfo("text")).trim(), 300, 120, 0x00EE00, 0x000000));
		cons.enable(false);
		cons.scrollToBottom();
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.add(inp = new PC_GresTextEdit("", 30));
		inp.setColor(PC_GresWidget.textColorEnabled, 0x009900);
		inp.setColor(PC_GresWidget.textColorDisabled, 0x009900);
		inp.setColor(PC_GresWidget.textColorClicked, 0x00ff00);
		inp.setColor(PC_GresWidget.textColorHover, 0x00ff00);
		lh.add(send = new PC_GresButton("pc.gui.ok"));
		lv.add(lh);
		tab.addTab(lv, new PC_GresLabel("pc.gui.weasel.terminal.terminal"));
	}
	
	private void makeNetworkTab(PC_GresTab tab){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel("pc.gui.weasel.device.name"));
		lh.add(deviceName = new PC_GresTextEdit((String)miner.getInfo("deviceName"), 10));
		lv.add(lh);
		lv.add(deviceRename = new PC_GresButton("pc.gui.weasel.device.rename"));
		lh = new PC_GresLayoutH();
		lh.add(new PC_GresLabel("pc.gui.weasel.network.name"));
		lh.add(networkName = new PC_GresTextEdit((String)miner.getInfo("networkName"), 10));
		lv.add(lh);
		lh = new PC_GresLayoutH();
		lh.add(network1 = new PC_GresButton("pc.gui.weasel.network.join"));
		lh.add(network2 = new PC_GresButton("pc.gui.weasel.network.new"));
		network1.setId(0);
		network2.enable(false);
		lv.add(lh);
		PC_Color color = (PC_Color)miner.getInfo("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);
		lv.add(networkColor = new PC_GresColorPicker(color.getHex(), 100, 20));
		
		tab.addTab(lv, new PC_GresLabel("pc.gui.weasel.network.tab"));
	}
	
	@Override
	public void makeOtherTabs(PC_GresTab tab) {
		makeTerminalTab(tab);
		makeNetworkTab(tab);
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==send){
			String txt = inp.getText().trim();
			inp.setText("");
			miner.doInfoSet("brainmsg", "input", txt);
		}else if(widget==deviceName){
			List<String> deviceNames = (List<String>)miner.getInfo("deviceNames");
			deviceRename.enable(!deviceNames.contains(deviceName.getText()));
			if(deviceName.equals(""))
				deviceRename.enable(false);
		}else if(widget==deviceRename){
			miner.doInfoSet("brainmsg", "deviceRename", deviceName.getText());
		}else if(widget==networkName){
			List<String> networkNames = (List<String>)miner.getInfo("networkNames");
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
				miner.doInfoSet("brainmsg", "networkJoin", networkName.getText());
			}else{
				miner.doInfoSet("brainmsg", "networkRename", networkName.getText());
			}
		}else if(widget==network2){
			miner.doInfoSet("brainmsg", "networkNew", networkName.getText());
		}else if(widget==networkColor){
			miner.doInfoSet("brainmsg", "networkColor", PC_Color.fromHex(networkColor.getColor()));
			miner.setInfo("color", PC_Color.fromHex(networkColor.getColor()));
		}else{
			super.actionPerformed(widget, gui);
		}
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		if(!cons.getText().equals(((String)miner.getInfo("text")).trim())){
			cons.setText(((String)miner.getInfo("text")).trim());
			cons.scrollToBottom();
		}
		super.updateScreen(gui);
	}
	
}
