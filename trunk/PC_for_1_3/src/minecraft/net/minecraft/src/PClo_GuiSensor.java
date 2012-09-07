package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui for delayer and repeater gates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiSensor implements PC_IGresBase {

	private PClo_TileEntitySensor sensor;

	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresProgressBar slider;
	
	EntityPlayer player;

	/**
	 * @param tes Sensor tile entity
	 */
	public PClo_GuiSensor(EntityPlayer player, TileEntity tes) {
		sensor = (PClo_TileEntitySensor) tes;
		this.player = player;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		String title = "";
		if (sensor.getGroup() == 0) {
			title = PC_Lang.tr("tile.PCloSensorRanged.item.name");
		}
		if (sensor.getGroup() == 1) {
			title = PC_Lang.tr("tile.PCloSensorRanged.living.name");
		}
		if (sensor.getGroup() == 2) {
			title = PC_Lang.tr("tile.PCloSensorRanged.player.name");
		}

		// window
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg;
		PC_GresWidget vg;

		// layout with the input
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.sensor.range")));
		vg.add(slider = new PC_GresProgressBar(0x00ff00, 200));
		slider.configureLabel("", "32", 32);
		slider.setEditable(true);
		slider.setFraction(sensor.range / 32F);
		w.add(vg);

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);


		gui.setPausesGame(false);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			sensor.range = Math.round(slider.getFraction() * 32);

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

	@Override
	public List<Slot> getAllSlots(Container c) {
		return null;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}

}
