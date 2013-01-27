package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_Utils.Lang;

public class PCis_GuiCompressor extends PCis_ContainerCompressor
		implements PC_IGresClient {

	public PCis_GuiCompressor(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr(PCis_App.compressor.getItemNameIS(thePlayer.getCurrentEquippedItem())+".name"));
		PC_GresInventory inventory = new PC_GresInventory(inv.getSize().x, inv.getSize().y);
		int i=0;
		for(int y=0; y<inv.getSize().y; y++){
			for(int x=0; x<inv.getSize().x; x++){
				inventory.setSlot(x, y, lSlot.get(i));
				i++;
			}
		}
		w.add(inventory);
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
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

}
