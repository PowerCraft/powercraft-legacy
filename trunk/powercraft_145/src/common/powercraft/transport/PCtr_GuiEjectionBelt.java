package powercraft.transport;

import net.minecraft.src.EntityPlayer;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresGap;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresRadioButton;
import powercraft.management.PC_GresRadioButton.PC_GresRadioGroup;
import powercraft.management.PC_GresSeparatorH;
import powercraft.management.PC_GresTextEdit;
import powercraft.management.PC_GresTextEdit.PC_GresInputType;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;

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
	
	public PCtr_GuiEjectionBelt(EntityPlayer player, Object[] o){
		teb = (PCtr_TileEntityEjectionBelt)GameInfo.getTE(player.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr("tile.PCtr_BlockBeltEjector.name"));
		w.setAlignH(PC_GresAlign.STRETCH);
		w.gapUnderTitle = 13;

		PC_GresWidget vg, hg;


		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup actionMode = new PC_GresRadioGroup();


		vg.add(new PC_GresLabel(Lang.tr("pc.gui.ejector.modeEjectTitle")));

		vg.setWidgetMargin(0);

		hg = new PC_GresLayoutH();
		hg.setWidgetMargin(0);
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.add(radioModeStacks = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeStacks"), actionMode));
		radioModeStacks.setMinWidth(100);
		radioModeStacks.check(teb.actionType == 0);
		hg.add(editSlots = new PC_GresTextEdit(teb.numStacksEjected + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeItems = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeItems"), actionMode));
		radioModeItems.setMinWidth(100);
		radioModeItems.check(teb.actionType == 1);
		hg.add(editItems = new PC_GresTextEdit(teb.numItemsEjected + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeAll = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeAll"), actionMode));
		radioModeAll.setMinWidth(100);
		radioModeAll.check(teb.actionType == 2);
		vg.add(hg);

		w.add(vg);

		w.add(new PC_GresSeparatorH(0, 5));

		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup selectMode = new PC_GresRadioGroup();

		vg.add(new PC_GresLabel(Lang.tr("pc.gui.ejector.modeSelectTitle")));
		vg.add(radioSelectFirst = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeSelectFirst"), selectMode));
		vg.add(radioSelectLast = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeSelectLast"), selectMode));
		vg.add(radioSelectRandom = new PC_GresRadioButton(Lang.tr("pc.gui.ejector.modeSelectRandom"), selectMode));
		radioSelectFirst.check(teb.itemSelectMode == 0);
		radioSelectLast.check(teb.itemSelectMode == 1);
		radioSelectRandom.check(teb.itemSelectMode == 2);

		w.add(vg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(btnCANCEL = new PC_GresButton(Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(btnOK = new PC_GresButton(Lang.tr("pc.gui.ok")).setId(0));
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
		PC_PacketHandler.setTileEntity(teb, "actionType", actionType);
		
		int itemSelectMode=0;
		if (radioSelectFirst.isChecked())
			itemSelectMode = 0;
		if (radioSelectLast.isChecked()) 
			itemSelectMode = 1;
		if (radioSelectRandom.isChecked()) 
			itemSelectMode = 2;
		PC_PacketHandler.setTileEntity(teb, "itemSelectMode", itemSelectMode);
		
		try {
			PC_PacketHandler.setTileEntity(teb, "numStacksEjected", Integer.parseInt(editSlots.getText()));
		} catch (NumberFormatException e) {}

		try {
			PC_PacketHandler.setTileEntity(teb, "numItemsEjected", Integer.parseInt(editItems.getText()));
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

}
