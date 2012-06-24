package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui where user decides what laser type he wants. They share the same item.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PCtr_GuiTeleporterDecide implements PC_IGresBase {
	

	private PCtr_TileEntityTeleporter teleporter;

	public PCtr_GuiTeleporterDecide(PCtr_TileEntityTeleporter te) {
		teleporter = te;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		//window
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.teleporter.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg,vg;
		
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
			teleporter.setSender();
			
		} else if (widget.getId() == 1) {	
			teleporter.setReceiver();	
			
		}
		
		PC_Utils.openGres(getPlayer(), new PCtr_GuiTeleporter(teleporter, true));

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {		
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

}