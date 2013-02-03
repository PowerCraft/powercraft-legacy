package powercraft.weasel;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresImage;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresSeparatorV;
import powercraft.management.PC_GresTab;
import powercraft.management.PC_GresTextEditMultiline;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_GresTextEditMultiline.Keyword;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import weasel.WeaselEngine;

public class PCws_GuiWeaselCore extends PCws_GuiWeasel {

	private PC_GresTextEditMultiline program;
	private PC_GresLabel programError;
	private PC_GresButton launchProgram, restartProgram, stopProgram;
	private PC_GresWidget txRunning, txStack, txMemory, txPeripherals, txLength;
	private int tick;
	
	public PCws_GuiWeaselCore(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
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
		
		if(te.getData("error")!=null){
			stopProgram.enable(false);
			programError.setText((String)te.getData("error"));
			programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
		}else if(!(Boolean)te.getData("isRunning")){
			stopProgram.enable(false);
			programError.setColor(PC_GresWidget.textColorDisabled, 0x00aa00);
		}else{
			programError.setText(Lang.tr("pc.gui.weasel.core.running"));
		}
		
		lv.add(lh);
		
		tab.addTab(lv, new PC_GresLabel(Lang.tr("pc.gui.weasel.core.program")));
	}
	
	private void makeStatusTab(PC_GresTab tab){
		PC_GresLayoutH mhl = new PC_GresLayoutH();
		PC_GresLayoutV vl = new PC_GresLayoutV();
		vl.setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.CENTER).setMinWidth(100);
		vl.add(new PC_GresImage(ModuleInfo.getGresImgDir() + "graphics.png", 0, 24, 80, 80));
		vl.add(new PC_GresLabel("WEASEL VM"));
		vl.add(new PC_GresLabel("© MightyPork"));
		mhl.add(vl);
		mhl.add(new PC_GresSeparatorV(3, 150).setLineColor(0x666666));
		
		int colorLabel = 0x000000;
		int colorValue = 0x000099;
		
		vl = new PC_GresLayoutV();
		vl.setAlignH(PC_GresAlign.LEFT).setAlignV(PC_GresAlign.CENTER).setMinWidth(100);
		PC_GresLayoutH hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.LEFT);
		hl.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.core.runningStateLabel")).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorDisabled, colorLabel).enable(false));
		hl.add(txRunning = new PC_GresLabel("").setColor(PC_GresWidget.textColorDisabled, colorValue).enable(false));
		vl.add(hl);

		hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.LEFT);
		hl.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.core.programLength")).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorDisabled, colorLabel).enable(false));
		hl.add(txLength = new PC_GresLabel("").setColor(PC_GresWidget.textColorDisabled, colorValue).enable(false));
		vl.add(hl);

		hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.LEFT);
		hl.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.core.stackLabel")).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorDisabled, colorLabel).enable(false));
		hl.add(txStack = new PC_GresLabel("").setColor(PC_GresWidget.textColorDisabled, colorValue).enable(false));
		vl.add(hl);

		hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.LEFT);
		hl.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.core.memoryLabel")).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorDisabled, colorLabel).enable(false));
		hl.add(txMemory = new PC_GresLabel("").setColor(PC_GresWidget.textColorDisabled, colorValue).enable(false));
		vl.add(hl);

		hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.LEFT);
		hl.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.core.peripheralsLabel")).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorDisabled, colorLabel).enable(false));
		hl.add(txPeripherals = new PC_GresLabel("").setColor(PC_GresWidget.textColorDisabled, colorValue).enable(false));
		vl.add(hl);
		mhl.add(vl);
		tab.addTab(mhl, new PC_GresLabel(Lang.tr("pc.gui.weasel.core.status")));
		
		
		if (te.getData("error")!=null) {
			txRunning.setText(Lang.tr("pc.gui.weasel.core.crashed"));
		} else if ((Boolean)te.getData("isRunning")) {
			txRunning.setText(Lang.tr("pc.gui.weasel.core.running"));
		} else {
			txRunning.setText(Lang.tr("pc.gui.weasel.core.stoped"));
		}
		txStack.setText((Integer)te.getData("stackSize") + " " + Lang.tr("pc.gui.weasel.core.unitObjects"));
		txMemory.setText((Integer)te.getData("variableCount") + " " + Lang.tr("pc.gui.weasel.core.unitObjects"));
		txPeripherals.setText(""+(Integer)te.getData("networkMemberCount"));
		txLength.setText((Integer)te.getData("instructionCount")+" " + Lang.tr("pc.gui.weasel.core.unitInstructions"));
		
	}
	
	@Override
	protected void addTabs(PC_GresTab tab) {
		makeStatusTab(tab);
		makeProgramTab(tab);
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==launchProgram){
			te.call("launch", program.getText());
		}else if(widget==restartProgram){
			te.call("restart", null);
		}else if(widget==stopProgram){
			te.call("stop", null);
		}else{
			super.actionPerformed(widget, gui);
		}
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		tick++;
		if(tick%20==0){
			if(te.getData("error")!=null){
				stopProgram.enable(false);
				programError.setText((String)te.getData("error"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
			}else if((Boolean)te.getData("isRunning")){
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
				String msg = e.getMessage();
				if(msg.length()>40){
					msg = msg.substring(0, 38)+"...";
				}
				programError.setText(msg);
				programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
				launchProgram.enable(false);
			}
			if (te.getData("error")!=null) {
				txRunning.setText(Lang.tr("pc.gui.weasel.core.crashed"));
			} else if ((Boolean)te.getData("isRunning")) {
				txRunning.setText(Lang.tr("pc.gui.weasel.core.running"));
			} else {
				txRunning.setText(Lang.tr("pc.gui.weasel.core.stoped"));
			}
			txStack.setText((Integer)te.getData("stackSize") + " " + Lang.tr("pc.gui.weasel.core.unitObjects"));
			txMemory.setText((Integer)te.getData("variableCount") + " " + Lang.tr("pc.gui.weasel.core.unitObjects"));
			txPeripherals.setText(""+(Integer)te.getData("networkMemberCount"));
			txLength.setText((Integer)te.getData("instructionCount")+" " + Lang.tr("pc.gui.weasel.core.unitInstructions"));
		}
	}
	
}
