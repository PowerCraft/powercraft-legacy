package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui for item separation belt.
 * 
 * @author MightyPork
 */
public class PCtr_GuiSeparationBelt implements PC_IGresBase {

	private EntityPlayer player;
	private PCtr_TileEntitySeparationBelt tes;
	private PC_GresCheckBox checkLogs;
	private PC_GresCheckBox checkPlanks;
	private PC_GresCheckBox checkAll;
	private List<Slot> lSlot = new ArrayList<Slot>();
	
	/**
	 * Item separation belt
	 * 
	 * @param player
	 * @param tileentityconveyorfilter
	 */
	public PCtr_GuiSeparationBelt(EntityPlayer player, PCtr_TileEntitySeparationBelt tileentityconveyorfilter) {
		this.player = player;
		this.tes = tileentityconveyorfilter;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(00, 00, PC_Lang.tr("tile.PCconveyorFilter.name"));
		w.setWidthForInventory();
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 56, 66, 8, 15));

		PC_GresInventory left, right;

		hg.add(left = new PC_GresInventory(3, 3));

		hg.add(right = new PC_GresInventory(3, 3));

		for (int i = 0; i < tes.getSizeInventory(); i++) {
			if (i % 6 >= 3) {
				left.setSlot(lSlot.get(i), i % 3, (int) Math.floor(i / 6));
			} else {
				right.setSlot(lSlot.get(i), i % 3, (int) Math.floor(i / 6));
			}
		}
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 64, 66, 8, 15));
		w.add(hg);

		PC_GresLayoutV vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);
		vg.setMinWidth(100);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.separationBelt.group")).setWidgetMargin(0));
		vg.setWidgetMargin(0);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(checkLogs = new PC_GresCheckBox(PC_Lang.tr("pc.gui.separationBelt.groupLogs")).check(tes.group_logs));
		hg.add(checkPlanks = new PC_GresCheckBox(PC_Lang.tr("pc.gui.separationBelt.groupPlanks")).check(tes.group_planks));
		hg.add(checkAll = new PC_GresCheckBox(PC_Lang.tr("pc.gui.separationBelt.groupAll")).check(tes.group_all));

		vg.add(hg);

		w.add(new PC_GresGap(0, 2));
		w.add(vg);
		w.add(new PC_GresGap(0, 2));

		w.add(new PC_GresInventoryPlayer(true));
		w.add(new PC_GresGap(0, 0));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {

		PC_Utils.setTileEntity(getPlayer(), tes, "logsPlanksAll", checkLogs.isChecked(), checkPlanks.isChecked(), checkAll.isChecked());


	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public List<Slot> getAllSlots(Container c) {
		for (int i = 0; i < tes.getSizeInventory(); i++)
			lSlot.add(new Slot(tes, i, 0, 0));
		return lSlot;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}
}
