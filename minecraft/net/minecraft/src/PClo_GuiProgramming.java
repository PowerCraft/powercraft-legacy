package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;

public class PClo_GuiProgramming implements PC_IGresBase {

	private PClo_TileEntityGate tileEntity;
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txError;
	
	
	public PClo_GuiProgramming(PClo_TileEntityGate tileEntity){
		this.tileEntity = tileEntity;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("Programmin guide");
		PC_GresWidget hg;
		w.add(edit = new PC_GresTextEdit(tileEntity.programm, 10));
		w.add(txError = new PC_GresLabel(""));
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==buttonCancel)
			gui.close();
		else if(widget==buttonOK){
			tileEntity.programm = edit.getText();
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
	public void onCraftMatrixChanged(IInventory iinventory) {
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}
	
}
