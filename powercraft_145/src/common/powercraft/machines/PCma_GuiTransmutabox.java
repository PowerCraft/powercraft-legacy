package powercraft.machines;

import net.minecraft.src.EntityPlayer;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryBigSlot;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresProgressBar;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_Utils.Lang;

public class PCma_GuiTransmutabox extends PCma_ContainerTransmutabox implements
		PC_IGresClient {

	private PC_GresProgressBar progress;
	
	public PCma_GuiTransmutabox(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(Lang.tr(PCma_App.transmutabox.getBlockName()+".name"));
		
		PC_GresWidget hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.JUSTIFIED);
		int id=0;
		hl.add(new PC_GresInventoryBigSlot(lSlot.get(id++)));
		PC_GresInventory inv = new PC_GresInventory(8, 1);
		for(int x=0; x<8; x++){
			inv.setSlot(lSlot.get(id++), x, 0);
		}
		hl.add(inv);
		w.add(hl);
		w.add(progress = new PC_GresProgressBar(0xFFFF0000));
		inv = new PC_GresInventory(10, 4);
		for(int y=0; y<4; y++){
			for(int x=0; x<10; x++){
				inv.setSlot(lSlot.get(id++), x, y);
			}
		}
		w.add(inv);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
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
	public void updateTick(PC_IGresGui gui) {
		progress.setFraction(te.getLoadTime()/1000.0f);
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

}
