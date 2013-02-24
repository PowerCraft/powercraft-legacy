package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresGap;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresRadioButton;
import powercraft.management.gres.PC_GresSeparatorH;
import powercraft.management.gres.PC_GresTextEdit;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.gres.PC_GresRadioButton.PC_GresRadioGroup;
import powercraft.management.gres.PC_GresTextEdit.PC_GresInputType;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;

public class PCtr_GuiEjectionBelt implements PC_IGresClient {
	
	private PCtr_TileEntityEjectionBelt teb;

	private PC_GresWidget btnOK;
	private PC_GresWidget btnCANCEL;
	
	private PC_GresWidget editItems;
	private PC_GresWidget editSlots;

	private PC_GresRadioButton radioModeStacks;
	private PC_GresRadioButton radioModeItems;
	private PC_GresRadioButton radioModeAll;

	private PC_GresRadioButton radioSelectFirst;
	private PC_GresRadioButton radioSelectLast;
	private PC_GresRadioButton radioSelectRandom;
	
	public PCtr_GuiEjectionBelt(EntityPlayer player, PC_TileEntity te, Object[] o){
		teb = (PCtr_TileEntityEjectionBelt)te;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("tile.PCtr_BlockBeltEjector.name");
		w.setAlignH(PC_GresAlign.STRETCH);
		w.gapUnderTitle = 13;

		PC_GresWidget vg, hg;


		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup actionMode = new PC_GresRadioGroup();


		vg.add(new PC_GresLabel("pc.gui.ejector.modeEjectTitle"));

		vg.setWidgetMargin(0);

		hg = new PC_GresLayoutH();
		hg.setWidgetMargin(0);
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.add(radioModeStacks = new PC_GresRadioButton("pc.gui.ejector.modeStacks", actionMode));
		radioModeStacks.setMinWidth(100);
		radioModeStacks.check(teb.getActionType() == 0);
		hg.add(editSlots = new PC_GresTextEdit(teb.getNumStacksEjected() + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeItems = new PC_GresRadioButton("pc.gui.ejector.modeItems", actionMode));
		radioModeItems.setMinWidth(100);
		radioModeItems.check(teb.getActionType() == 1);
		hg.add(editItems = new PC_GresTextEdit(teb.getNumItemsEjected() + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeAll = new PC_GresRadioButton("pc.gui.ejector.modeAll", actionMode));
		radioModeAll.setMinWidth(100);
		radioModeAll.check(teb.getActionType() == 2);
		vg.add(hg);

		w.add(vg);

		w.add(new PC_GresSeparatorH(0, 5));

		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup selectMode = new PC_GresRadioGroup();

		vg.add(new PC_GresLabel("pc.gui.ejector.modeSelectTitle"));
		vg.add(radioSelectFirst = new PC_GresRadioButton("pc.gui.ejector.modeSelectFirst", selectMode));
		vg.add(radioSelectLast = new PC_GresRadioButton("pc.gui.ejector.modeSelectLast", selectMode));
		vg.add(radioSelectRandom = new PC_GresRadioButton("pc.gui.ejector.modeSelectRandom", selectMode));
		radioSelectFirst.check(teb.getItemSelectMode() == 0);
		radioSelectLast.check(teb.getItemSelectMode() == 1);
		radioSelectRandom.check(teb.getItemSelectMode() == 2);

		w.add(vg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(btnCANCEL = new PC_GresButton("pc.gui.cancel").setId(1));
		hg.add(btnOK = new PC_GresButton("pc.gui.ok").setId(0));
		w.add(hg);

		w.add(new PC_GresGap(0, 0));

		gui.add(w);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget == btnCANCEL){
			onEscapePressed(gui);
		}else if(widget == btnOK){
			onReturnPressed(gui);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		int actionType=0;
		if (radioModeStacks.isChecked()) 
			actionType = 0;
		if (radioModeItems.isChecked()) 
			actionType = 1;
		if (radioModeAll.isChecked()) 
			actionType = 2;
		teb.setActionType(actionType);
		
		int itemSelectMode=0;
		if (radioSelectFirst.isChecked())
			itemSelectMode = 0;
		if (radioSelectLast.isChecked()) 
			itemSelectMode = 1;
		if (radioSelectRandom.isChecked()) 
			itemSelectMode = 2;
		teb.setItemSelectMode(itemSelectMode);
		
		try {
			teb.setNumStacksEjected(Integer.parseInt(editSlots.getText()));
		} catch (NumberFormatException e) {}

		try {
			teb.setNumItemsEjected(Integer.parseInt(editItems.getText()));
		} catch (NumberFormatException e) {}


		// save data

		gui.close();
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {
		if(key.equals("actionType")){
			radioModeStacks.check((Integer)value == 0);
			radioModeItems.check((Integer)value == 1);
			radioModeAll.check((Integer)value == 2);
		}else if(key.equals("numStacksEjected")){
			editSlots.setText(""+(Integer)value);
		}else if(key.equals("numItemsEjected")){
			editItems.setText(""+(Integer)value);
		}else if(key.equals("itemSelectMode")){
			radioSelectFirst.check((Integer)value == 0);
			radioSelectLast.check((Integer)value == 1);
			radioSelectRandom.check((Integer)value == 2);
		}
	}

}
