package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresRadioButton.PC_GresRadioGroup;
import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui for item ejection belt.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCtr_GuiEjectionBelt extends PC_GresBase {

	private PCtr_TileEntityEjectionBelt teb;

	private PC_GresWidget btnOK;

	private PC_GresWidget editItems;
	private PC_GresWidget editSlots;

	private PC_GresRadioButton radioModeStacks;
	private PC_GresRadioButton radioModeItems;
	private PC_GresRadioButton radioModeAll;

	private PC_GresRadioButton radioSelectFirst;
	private PC_GresRadioButton radioSelectLast;
	private PC_GresRadioButton radioSelectRandom;

	/**
	 * ejection belt gui
	 * 
	 * @param tilee belt tile entity
	 */
	public PCtr_GuiEjectionBelt(EntityPlayer player, TileEntity tilee) {
		teb = (PCtr_TileEntityEjectionBelt)tilee;
		this.player = player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("tile.PCconveyorFurnace.name"));
		w.setAlignH(PC_GresAlign.STRETCH);
		w.gapUnderTitle = 13;

		PC_GresWidget vg, hg;


		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup actionMode = new PC_GresRadioGroup();


		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.ejector.modeEjectTitle")));

		vg.setWidgetMargin(0);

		hg = new PC_GresLayoutH();
		hg.setWidgetMargin(0);
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.add(radioModeStacks = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeStacks"), actionMode));
		radioModeStacks.setMinWidth(100);
		radioModeStacks.check(teb.actionType == 0);
		hg.add(editSlots = new PC_GresTextEdit(teb.numStacksEjected + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeItems = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeItems"), actionMode));
		radioModeItems.setMinWidth(100);
		radioModeItems.check(teb.actionType == 1);
		hg.add(editItems = new PC_GresTextEdit(teb.numItemsEjected + "", 6, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(radioModeAll = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeAll"), actionMode));
		radioModeAll.setMinWidth(100);
		radioModeAll.check(teb.actionType == 2);
		vg.add(hg);

		w.add(vg);

		w.add(new PC_GresSeparatorH(0, 5));

		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		PC_GresRadioGroup selectMode = new PC_GresRadioGroup();

		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.ejector.modeSelectTitle")));
		vg.add(radioSelectFirst = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeSelectFirst"), selectMode));
		vg.add(radioSelectLast = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeSelectLast"), selectMode));
		vg.add(radioSelectRandom = new PC_GresRadioButton(PC_Lang.tr("pc.gui.ejector.modeSelectRandom"), selectMode));
		radioSelectFirst.check(teb.itemSelectMode == 0);
		radioSelectLast.check(teb.itemSelectMode == 1);
		radioSelectRandom.check(teb.itemSelectMode == 2);

		w.add(vg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(btnOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		w.add(new PC_GresGap(0, 0));

		gui.add(w);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		switch (widget.getId()) {
			case 1:
				gui.close();
				break;

			case 0: //OK
				int actionType=0;
				if (radioModeStacks.isChecked()) actionType = 0;
				if (radioModeItems.isChecked()) actionType = 1;
				if (radioModeAll.isChecked()) actionType = 2;
				PC_Utils.setTileEntity(getPlayer(), teb, "actionType", actionType);
				
				int itemSelectMode=0;
				if (radioSelectFirst.isChecked()) itemSelectMode = 0;
				if (radioSelectLast.isChecked()) itemSelectMode = 1;
				if (radioSelectRandom.isChecked()) itemSelectMode = 2;
				PC_Utils.setTileEntity(getPlayer(), teb, "itemSelectMode", itemSelectMode);
				
				try {
					PC_Utils.setTileEntity(getPlayer(), teb, "numStacksEjected", Integer.parseInt(editSlots.getText()));
				} catch (NumberFormatException e) {}

				try {
					PC_Utils.setTileEntity(getPlayer(), teb, "numItemsEjected", Integer.parseInt(editItems.getText()));
				} catch (NumberFormatException e) {}


				// save data

				gui.close();
				break;
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(btnOK, gui);
	}

}
