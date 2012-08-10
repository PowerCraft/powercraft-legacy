package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Roaster's GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiRoaster implements PC_IGresBase {

	private EntityPlayer player;
	private IInventory inventory;
	private List<Slot> lSlot = new ArrayList<Slot>();
	
	/**
	 * @param player player
	 * @param roaster device tile entity
	 */
	public PCma_GuiRoaster(EntityPlayer player, IInventory roaster) {
		this.player = player;
		inventory = roaster;
	}


	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_Lang.tr("tile.PCmaRoaster.name") + " - " + PC_Lang.tr("pc.roaster.insertFuel")).setWidthForInventory();


		w.setAlignH(PC_GresAlign.CENTER);

		PC_GresInventory inv = new PC_GresInventory(9, 1);
		for (int i = 0; i < 9; i++) {
			inv.setSlot(lSlot.get(i), i, 0);
		}
		w.add(inv);
		w.add(new PC_GresInventoryPlayer(true));

		gui.add(w);

		w.calcChildPositions();

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

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
		for (int i = 0; i < 9; i++) {
			if (i < inventory.getSizeInventory()) {
				lSlot.add(new PC_SlotSelective(inventory, i, 0, 0));
			}
		}
		return lSlot;
	}


	@Override
	public boolean canShiftTransfer() {
		return true;
	}

}
