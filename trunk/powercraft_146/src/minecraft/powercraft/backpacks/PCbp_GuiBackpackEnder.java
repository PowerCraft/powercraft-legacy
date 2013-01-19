package powercraft.backpacks;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_Utils.Lang;

public class PCbp_GuiBackpackEnder extends PCbp_ContainerBackpackEnder
		implements PC_IGresClient {

	public PCbp_GuiBackpackEnder(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr("pc.gui.backpack.ender"));
		PC_GresInventory inventory = new PC_GresInventory(9, 3);
		int i=0;
		for(int y=0; y<3; y++){
			for(int x=0; x<9; x++){
				inventory.setSlot(lSlot.get(i), x, y);
				i++;
			}
		}
		gui.add(inventory);
		gui.add(new PC_GresInventoryPlayer(true));
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
