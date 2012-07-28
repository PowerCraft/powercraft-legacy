package net.minecraft.src;

import java.util.Iterator;
import java.util.Map;

import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;
import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import net.minecraft.src.PClo_NetManager.NetworkMember;
import net.minecraft.src.PClo_NetManager.WeaselNetwork;

public class PClo_GuiWeaselTerminalTerm implements PC_IGresBase {

	private PClo_WeaselPluginTerminal term;
	private PC_GresWindow w;
	private PC_GresWidget edInput;
	private PC_GresWidget btnOk;
	private PC_GresTextEditMultiline edMain;

	/**
	 * GUI for port.
	 * 
	 * @param display plugin instance
	 */
	public PClo_GuiWeaselTerminalTerm(PClo_WeaselPluginTerminal device) {
		this.term = device;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}
	

	@Override
	public void initGui(PC_IGresGui gui) {
		w = new PC_GresWindow(term.getName()+" @ "+term.getNetworkName());
		w.setAlignH(PC_GresAlign.CENTER);
		w.setMinWidth(260);

		
		w.add(edMain = (PC_GresTextEditMultiline) new PC_GresTextEditMultiline(term.text,240,130));
		
		edMain.enabled = false;
		edMain.setFgColor(0x00ee00);
		edMain.setBgColor(0x000000);
		edMain.scrollToBottom();
		
		PC_GresWidget hg;
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH).setWidgetMargin(0);
		hg.add(edInput = new PC_GresTextEdit("", 26, PC_GresInputType.TEXT).setWidgetMargin(1).setMinWidth(199));
		edInput.setColor(PC_GresWidget.textColorEnabled, 0x009900);
		edInput.setColor(PC_GresWidget.textColorDisabled, 0x009900);
		edInput.setColor(PC_GresWidget.textColorClicked, 0x00ff00);
		edInput.setColor(PC_GresWidget.textColorHover, 0x00ff00);
		hg.add(btnOk = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setMinWidth(40).setWidgetMargin(1));
		w.add(hg);
		
		w.add(new PC_GresGap(0, 0));
		
		gui.add(w);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget == btnOk) {
			String txt = edInput.text.trim();
			if(txt.length()>0) {				
				term.userInput.add(txt);
				term.addText("> "+txt+"\n");
			}
			edInput.text="";
			
			
			WeaselNetwork network = term.getNetwork();
			if(network!=null)
				((PClo_WeaselPlugin) network.getMember("CORE"))
						.callFunctionExternalDelegated("termIn", new WeaselString(term.getName()),txt);
			
			term.isChanged = true;
			return;
		}
		
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(btnOk, gui);
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {
		if(!edMain.getText().equals(term.text.trim())) {
			edMain.setText(term.text.trim());
			edMain.scrollToBottom();
		}
	}

}
