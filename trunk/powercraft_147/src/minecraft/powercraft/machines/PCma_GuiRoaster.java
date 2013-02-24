package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_TileEntity;
import powercraft.management.gres.PC_GresInventory;
import powercraft.management.gres.PC_GresInventoryPlayer;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.registry.PC_LangRegistry;

public class PCma_GuiRoaster extends PCma_ContainerRoaster implements
		PC_IGresClient {

	public PCma_GuiRoaster(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_LangRegistry.tr("tile.PCma_BlockRoaster.name") + " - " + PC_LangRegistry.tr("pc.roaster.insertFuel")).setWidthForInventory();

		w.setAlignH(PC_GresAlign.CENTER);

		PC_GresInventory inv = new PC_GresInventory(9, 1);
		for (int i = 0; i < 9; i++) {
			inv.setSlot(i, 0, invSlots[i]);
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
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
