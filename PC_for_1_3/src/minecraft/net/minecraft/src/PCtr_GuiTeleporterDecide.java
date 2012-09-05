package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui where user decides what teleporter type he wants.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCtr_GuiTeleporterDecide implements PC_IGresBase {


	private PCtr_TileEntityTeleporter teleporter;

	/**
	 * @param te telep. TE
	 */
	public PCtr_GuiTeleporterDecide(PCtr_TileEntityTeleporter te) {
		teleporter = te;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		// window
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.teleporter.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg, vg;

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.teleporter.selectType")));
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.teleporter.selectTypeDescr")));
		w.add(vg);

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.teleporter.type.sender")).setId(0).setMinWidth(70));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.teleporter.type.target")).setId(1).setMinWidth(70));
		w.add(hg);

		gui.add(w);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {
			//teleporter.setIsSender();

		} else if (widget.getId() == 1) {
			//teleporter.setIsReceiver();

		}
		
		gui.close();
		
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public List<Slot> getAllSlots(Container c) {
		return null;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}

}