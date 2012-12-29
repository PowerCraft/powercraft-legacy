package powercraft.weasel;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresTab;
import powercraft.management.PC_GresTextEditMultiline;
import powercraft.management.PC_GresTextEditMultiline.Keyword;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_IGresGui;
import weasel.WeaselEngine;

public class PCws_GuiWeaselCore extends PCws_GuiWeasel {

	private PC_GresTextEditMultiline program;
	private PC_GresLabel programError;
	private PC_GresButton launchProgram, restartProgram, stopProgram;
	private int tick;
	
	public PCws_GuiWeaselCore(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		
	}

	private void makeProgramTab(PC_GresTab tab){
		List<Keyword> kw = (List<Keyword>)te.getData("keywords");
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.add(program = new PC_GresTextEditMultiline((String)te.getData("program"), 300, 120, 
				PCws_WeaselHighlightHelper.colorDefault, PCws_WeaselHighlightHelper.colorBackground,
				kw, PCws_WeaselHighlightHelper.autoAdd));
		
		lv.add(programError = new PC_GresLabel(Lang.tr("pc.gui.weasel.core.noError")));
		programError.enable(false);
		
		PC_GresLayoutH lh = new PC_GresLayoutH();
		
		lh.add(launchProgram = new PC_GresButton(Lang.tr("pc.gui.weasel.core.launchProgram")));
		lh.add(restartProgram = new PC_GresButton(Lang.tr("pc.gui.weasel.core.restartProgram")));
		lh.add(stopProgram = new PC_GresButton(Lang.tr("pc.gui.weasel.core.stopProgram")));
		
		if(!(Boolean)te.getData("isRunning")){
			stopProgram.enable(false);
			programError.setColor(PC_GresWidget.textColorDisabled, 0x00aa00);
		}else{
			programError.setText(Lang.tr("pc.gui.weasel.core.running"));
		}
		
		lv.add(lh);
		
		tab.addTab(lv, new PC_GresLabel(Lang.tr("pc.gui.weasel.core.program")));
	}
	
	@Override
	protected void addTabs(PC_GresTab tab) {
		makeProgramTab(tab);
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==launchProgram){
			PC_PacketHandler.setTileEntity(te, "msg", "launch", program.getText());
		}else if(widget==restartProgram){
			PC_PacketHandler.setTileEntity(te, "msg", "restart", null);
		}else if(widget==stopProgram){
			PC_PacketHandler.setTileEntity(te, "msg", "stop", null);
		}else{
			super.actionPerformed(widget, gui);
		}
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		tick++;
		if(tick%20==0){
			if((Boolean)te.getData("isRunning")){
				stopProgram.enable(true);
				programError.setText(Lang.tr("pc.gui.weasel.core.running"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0x000000);
			}else{
				stopProgram.enable(false);
				programError.setText(Lang.tr("pc.gui.weasel.core.noError"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0x00aa00);
			}
			try{
				WeaselEngine.compileProgram(program.getText());
				launchProgram.enable(true);
			}catch(Exception e){
				programError.setText(e.getMessage());
				programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
				launchProgram.enable(false);
			}
		}
	}
	
}
