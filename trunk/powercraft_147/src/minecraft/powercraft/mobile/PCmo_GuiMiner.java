package powercraft.mobile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.core.PCco_App;
import powercraft.management.PC_TileEntity;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresCheckBox;
import powercraft.management.gres.PC_GresGap;
import powercraft.management.gres.PC_GresInventory;
import powercraft.management.gres.PC_GresInventoryPlayer;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresTab;
import powercraft.management.gres.PC_GresTextEditMultiline;
import powercraft.management.gres.PC_GresTextEditMultiline.Keyword;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.registry.PC_LangRegistry;
import powercraft.weasel.PCws_WeaselHighlightHelper;

public class PCmo_GuiMiner extends PCmo_ContainerMiner implements PC_IGresClient {

	protected PC_GresTab tab;
	protected PC_GresWidget program;
	protected PC_GresWidget programError;
	protected PC_GresWidget launchProgram;
	protected PC_GresWidget restartProgram;
	protected PC_GresWidget stopProgram;
	protected PC_GresCheckBox checkBridge;
	protected PC_GresCheckBox checkMining;
	protected PC_GresCheckBox checkLava;
	protected PC_GresCheckBox checkWater;
	protected PC_GresCheckBox checkAir;
	protected PC_GresCheckBox checkKeepFuel;
	protected PC_GresCheckBox checkTorchFloor;
	protected PC_GresCheckBox checkTorch;
	protected PC_GresCheckBox checkCompress;
	protected PC_GresCheckBox checkCobble;
	protected int tick;
	
	public PCmo_GuiMiner(EntityPlayer player, PC_TileEntity te,  Object[] o) {
		super(player, te, o);
	}

	private void makeProgramTab(PC_GresTab tab){
		List<Keyword> kw = (List<Keyword>)miner.getInfo("keywords");
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.add(new PC_GresLabel((String)miner.getInfo("script")));
		lv.add(program = new PC_GresTextEditMultiline((String)miner.getInfo("program"), 300, 120, 
				PCws_WeaselHighlightHelper.colorDefault, PCws_WeaselHighlightHelper.colorBackground,
				kw, PCws_WeaselHighlightHelper.autoAdd));
		
		lv.add(programError = new PC_GresLabel("pc.gui.miner.noError"));
		programError.enable(false);
		
		PC_GresLayoutH lh = new PC_GresLayoutH();
		
		lh.add(launchProgram = new PC_GresButton("pc.gui.miner.launchProgram"));
		lh.add(restartProgram = new PC_GresButton("pc.gui.miner.restartProgram"));
		lh.add(stopProgram = new PC_GresButton("pc.gui.miner.stopProgram"));
		
		if(miner.getInfo("error")!=null){
			stopProgram.enable(false);
			programError.setText((String)miner.getInfo("error"));
			programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
		}else if(!(Boolean)miner.getInfo("isRunning")){
			stopProgram.enable(false);
			programError.setColor(PC_GresWidget.textColorDisabled, 0x00aa00);
		}else{
			programError.setText(PC_LangRegistry.tr("pc.gui.miner.running"));
		}
		
		lv.add(lh);
		
		tab.addTab(lv, new PC_GresLabel("pc.gui.miner.program"));
	}
	
	private void makeSettingsTab(PC_GresTab tab){
		
		PC_GresWidget vgSettings = new PC_GresLayoutV().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT).setMinWidth(100).setWidgetMargin(1);

		vg.add(checkMining = new PC_GresCheckBox("pc.gui.miner.opt.mining"));
		vg.add(checkBridge = new PC_GresCheckBox("pc.gui.miner.opt.bridge"));
		vg.add(checkAir = new PC_GresCheckBox("pc.gui.miner.opt.airFill"));
		vg.add(checkLava = new PC_GresCheckBox("pc.gui.miner.opt.lavaFill"));
		vg.add(checkWater = new PC_GresCheckBox("pc.gui.miner.opt.waterFill"));
		vg.add(checkCompress = new PC_GresCheckBox("pc.gui.miner.opt.compress"));
		vg.add(checkCobble = new PC_GresCheckBox("pc.gui.miner.opt.makeCobble"));
		vg.add(checkKeepFuel = new PC_GresCheckBox("pc.gui.miner.opt.keepFuel"));
		vg.add(checkTorch = new PC_GresCheckBox("pc.gui.miner.opt.torchPlacing"));
		vg.add(checkTorchFloor = new PC_GresCheckBox("pc.gui.miner.opt.torchesOnlyOnFloor"));

		checkBridge.enable(miner.st.level >= PCmo_EntityMiner.LBRIDGE);
		checkLava.enable(miner.st.level >= PCmo_EntityMiner.LLAVA);
		checkWater.enable(miner.st.level >= PCmo_EntityMiner.LWATER);
		checkAir.enable(miner.st.level >= PCmo_EntityMiner.LAIR);
		checkCompress.enable(miner.st.level >= PCmo_EntityMiner.LCOMPRESS);
		checkTorchFloor.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
		checkTorch.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
		checkCobble.enable(miner.st.level >= PCmo_EntityMiner.LCOBBLE);
		
		checkMining.check(miner.getFlag(PCmo_EntityMiner.miningEnabled));
		checkBridge.check(miner.getFlag(PCmo_EntityMiner.bridgeEnabled));
		checkAir.check(miner.getFlag(PCmo_EntityMiner.airFillingEnabled));
		checkLava.check(miner.getFlag(PCmo_EntityMiner.lavaFillingEnabled));
		checkWater.check(miner.getFlag(PCmo_EntityMiner.waterFillingEnabled));
		checkCompress.check(miner.getFlag(PCmo_EntityMiner.compressBlocks));
		checkCobble.check(miner.getFlag(PCmo_EntityMiner.cobbleMake));
		checkKeepFuel.check(miner.getFlag(PCmo_EntityMiner.keepAllFuel));
		checkTorch.check(miner.getFlag(PCmo_EntityMiner.torches));
		checkTorchFloor.check(miner.getFlag(PCmo_EntityMiner.torchesOnlyOnFloor));
		
		vgSettings.add(vg);
		
		tab.addTab(vgSettings, new PC_GresLabel("pc.gui.miner.settings"));
	}
	
	private PC_GresWidget makeCargoTab(PC_GresTab tab){
		
		PC_GresWidget vgCargo = new PC_GresLayoutH().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP);
		vgCargo.setAlignH(PC_GresAlign.CENTER);
		
		PC_GresWidget vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(new PC_GresInventory(invSlots[0].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(0)))));
		vg.add(new PC_GresInventory(invSlots[1].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(1)))));
		vg.add(new PC_GresInventory(invSlots[2].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(2)))));
		vg.add(new PC_GresInventory(invSlots[3].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(3)))));

		vgCargo.add(vg);
		

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(1);
		PC_GresInventory cargoInv = new PC_GresInventory(11, 5);
		vg.add(cargoInv);
		int n=8;
		for(int j=0; j<5; j++){
			for(int i=0; i<11; i++){
				cargoInv.setSlot(i, j, invSlots[n]);
				n++;
			}
		}
		vg.add(new PC_GresInventoryPlayer(true));

		vgCargo.add(vg);
		
		vgCargo.add(new PC_GresGap(2, 0));
		
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(new PC_GresInventory(invSlots[7].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(7)))));
		vg.add(new PC_GresInventory(invSlots[6].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(6)))));
		vg.add(new PC_GresInventory(invSlots[5].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(5)))));
		vg.add(new PC_GresInventory(invSlots[4].
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(4)))));

		vgCargo.add(vg);
		
		tab.addTab(vgCargo, new PC_GresLabel("pc.gui.miner.cargo"));
		
		return vgCargo;
		
	}
	
	public void makeOtherTabs(PC_GresTab tab){
		
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("pc.gui.miner.title");
		
		PC_GresTab tab = new PC_GresTab();
		
		PC_GresWidget vgCargo = makeCargoTab(tab);
		makeSettingsTab(tab);
		makeProgramTab(tab);
		makeOtherTabs(tab);
		
		tab.makeTabVisible(vgCargo);
		
		w.add(tab);
		
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==launchProgram){
			miner.doInfoSet("launch", program.getText());
		}else if(widget==restartProgram){
			miner.doInfoSet("restart");
		}else if(widget==stopProgram){
			miner.doInfoSet("stop");
		}else if(widget==stopProgram){
			miner.doInfoSet("stop");
		}else if(widget==checkMining){
			miner.doInfoSet("set", PCmo_EntityMiner.miningEnabled, checkMining.isChecked());
		}else if(widget==checkBridge){
			miner.doInfoSet("set", PCmo_EntityMiner.bridgeEnabled, checkBridge.isChecked());
		}else if(widget==checkAir){
			miner.doInfoSet("set", PCmo_EntityMiner.airFillingEnabled, checkAir.isChecked());
		}else if(widget==checkLava){
			miner.doInfoSet("set", PCmo_EntityMiner.lavaFillingEnabled, checkLava.isChecked());
		}else if(widget==checkWater){
			miner.doInfoSet("set", PCmo_EntityMiner.waterFillingEnabled, checkWater.isChecked());
		}else if(widget==checkCompress){
			miner.doInfoSet("set", PCmo_EntityMiner.compressBlocks, checkCompress.isChecked());
		}else if(widget==checkCobble){
			miner.doInfoSet("set", PCmo_EntityMiner.cobbleMake, checkCobble.isChecked());
		}else if(widget==checkKeepFuel){
			miner.doInfoSet("set", PCmo_EntityMiner.keepAllFuel, checkKeepFuel.isChecked());
		}else if(widget==checkTorch){
			miner.doInfoSet("set", PCmo_EntityMiner.torches, checkTorch.isChecked());
		}else if(widget==checkTorchFloor){
			miner.doInfoSet("set", PCmo_EntityMiner.torchesOnlyOnFloor, checkTorchFloor.isChecked());
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
	public void updateScreen(PC_IGresGui gui) {
		tick++;
		if(tick%20==0){
			if(miner.getInfo("error")!=null){
				stopProgram.enable(false);
				programError.setText((String)miner.getInfo("error"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0xff0000);
			}else if((Boolean)miner.getInfo("isRunning")){
				stopProgram.enable(true);
				programError.setText(PC_LangRegistry.tr("pc.gui.miner.running"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0x000000);
			}else{
				stopProgram.enable(false);
				programError.setText(PC_LangRegistry.tr("pc.gui.miner.noError"));
				programError.setColor(PC_GresWidget.textColorDisabled, 0x00aa00);
			}
			try{
				miner.br.compileProgram(program.getText());
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
			checkBridge.enable(miner.st.level >= PCmo_EntityMiner.LBRIDGE);
			checkLava.enable(miner.st.level >= PCmo_EntityMiner.LLAVA);
			checkWater.enable(miner.st.level >= PCmo_EntityMiner.LWATER);
			checkAir.enable(miner.st.level >= PCmo_EntityMiner.LAIR);
			checkCompress.enable(miner.st.level >= PCmo_EntityMiner.LCOMPRESS);
			checkTorchFloor.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
			checkTorch.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
			checkCobble.enable(miner.st.level >= PCmo_EntityMiner.LCOBBLE);
			
			checkMining.check(miner.getFlag(PCmo_EntityMiner.miningEnabled));
			checkBridge.check(miner.getFlag(PCmo_EntityMiner.bridgeEnabled));
			checkAir.check(miner.getFlag(PCmo_EntityMiner.airFillingEnabled));
			checkLava.check(miner.getFlag(PCmo_EntityMiner.lavaFillingEnabled));
			checkWater.check(miner.getFlag(PCmo_EntityMiner.waterFillingEnabled));
			checkCompress.check(miner.getFlag(PCmo_EntityMiner.compressBlocks));
			checkCobble.check(miner.getFlag(PCmo_EntityMiner.cobbleMake));
			checkKeepFuel.check(miner.getFlag(PCmo_EntityMiner.keepAllFuel));
			checkTorch.check(miner.getFlag(PCmo_EntityMiner.torches));
			checkTorchFloor.check(miner.getFlag(PCmo_EntityMiner.torchesOnlyOnFloor));
		}
	}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
