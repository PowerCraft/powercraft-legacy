package net.minecraft.src;


import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui for full chest sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiFullChest implements PC_IGresBase {

	private PClo_TileEntityGate gate;

	@SuppressWarnings("unused")
	private PC_GresWidget buttonOK, buttonCancel;

	private PC_GresCheckBox check;

	/**
	 * @param tes Sensor tile entity
	 */
	public PClo_GuiFullChest(PClo_TileEntityGate tes) {
		gate = tes;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		String title = PC_Lang.tr("tile.PCloLogicGate.chestFull.name");
		

		// window
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.CENTER);
		
		w.add(new PC_GresGap(0,8));
		w.add(check = new PC_GresCheckBox(PC_Lang.tr("pc.gui.chestFull.requireAllSlotsFull")));	
		check.check(gate.fullChestNeedsAllSlotsFull);		
		w.add(new PC_GresGap(0,8));
		// buttons
		PC_GresWidget hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);


		gui.setPausesGame(false);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			gate.fullChestNeedsAllSlotsFull = check.isChecked();

			gui.close();

		} else if (widget.getId() == 1) {
			gui.close();
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

}
