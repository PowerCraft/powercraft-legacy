package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Block Builder GUI
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_GuiBlockBuilder implements PC_IGresBase {
	
	private EntityPlayer player;
	private IInventory inventory;

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
		
		w.add(new PC_GresInventory(inventory, 3, 3));
		w.add(new PC_GresInventoryPlayer(true));
		
		gui.add(w);
		gui.setCanShiftTransfer(true);

		w.calcChildPositions();
		
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
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
	public void onCraftMatrixChanged(IInventory iinventory) {
		// TODO Auto-generated method stub
		
	}
}
