package net.minecraft.src;


import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Transmutator GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_GuiTransmutator implements PC_IGresBase {

	private EntityPlayer player;
	private PCde_InventoryTransmutationContainer inventory;
	private PC_GresProgressBar chargeMeter;

	/**
	 * @param player player
	 * @param box device tile entity
	 */
	public PCde_GuiTransmutator(EntityPlayer player, PCde_InventoryTransmutationContainer box) {
		this.player = player;
		inventory = box;
	}


	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	private void updateFraction() {
		float fraction = 0;
		if (inventory.chamber.conductor != null) {
			fraction = ((float) inventory.chamber.conductor.lightningCharge / (float) PCde_TileEntityDeco.FLASH_CHARGE_MAX);
		}
		if (fraction > 1) fraction = 1;
		chargeMeter.setFraction(fraction);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_Lang.tr("tile.PCdeDecoBlock.3.name")).setWidthForInventory();

		w.setAlignH(PC_GresAlign.CENTER);

		PC_GresWidget vg;

		// layout with the input
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.transmutationChamber.charge")).setWidgetMargin(1));
		vg.add(chargeMeter = (PC_GresProgressBar) new PC_GresProgressBar(0x6633ff, 140).setWidgetMargin(1));
		updateFraction();
		w.add(vg);

		w.add(new PC_GresInventory(inventory, 5, 3));
		w.add(new PC_GresInventoryPlayer(true));

		gui.add(w);
		gui.setCanShiftTransfer(true);

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
	public void updateTick(PC_IGresGui gui) {
		updateFraction();
	}

}
