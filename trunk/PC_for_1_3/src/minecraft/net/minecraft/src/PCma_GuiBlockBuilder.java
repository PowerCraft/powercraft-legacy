package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Block Builder GUI
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiBlockBuilder implements PC_IGresBase {

	private EntityPlayer player;
	private IInventory inventory;
	private List<Slot> lSlot = new ArrayList<Slot>();
	
	/**
	 * @param player player
	 * @param tilee device tile entity
	 */
	public PCma_GuiBlockBuilder(EntityPlayer player, PCma_TileEntityBlockBuilder tilee) {
		this.player = player;
		inventory = tilee;
	}


	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_Lang.tr("tile.PCmaBlockBuilder.name")).setWidthForInventory();

		w.setAlignH(PC_GresAlign.CENTER);

		PC_GresInventory inv = new PC_GresInventory(3, 3);
		
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				inv.setSlot(lSlot.get(i+j*3), i, j);
			}
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
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (i + j * 3 < inventory.getSizeInventory()) {
					lSlot.add(new PC_SlotSelective(inventory, i + j * 3, 0, 0));
				}
			}
		}
		return lSlot;
	}


	@Override
	public boolean canShiftTransfer() {
		return true;
	}
}
