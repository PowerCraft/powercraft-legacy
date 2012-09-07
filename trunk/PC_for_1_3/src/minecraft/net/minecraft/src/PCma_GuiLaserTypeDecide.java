package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui where user decides what laser type he wants. They share the same item.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiLaserTypeDecide extends PC_GresBase {


	private PCma_TileEntityLaser laser;

	/**
	 * @param te laser tile entity
	 */
	public PCma_GuiLaserTypeDecide(EntityPlayer player, TileEntity te) {
		laser = (PCma_TileEntityLaser)te;
		this.player = player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		// window
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.laserTypeDecide.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg;

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.laserTypeDecide.sensor")).setId(0).setMinWidth(70));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.laserTypeDecide.redstoneSender")).setId(1).setMinWidth(70));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.laserTypeDecide.redstoneReceiver")).setId(2).setMinWidth(70));
		w.add(hg);

		gui.add(w);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {
			laser.setType(PCma_LaserType.SENSOR);
			gui.close();

		} else if (widget.getId() == 1) {
			laser.setType(PCma_LaserType.RS_SEND);
			gui.close();

		} else if (widget.getId() == 2) {
			laser.setType(PCma_LaserType.RS_RECEIVE);
			gui.close();

		}

	}

}
