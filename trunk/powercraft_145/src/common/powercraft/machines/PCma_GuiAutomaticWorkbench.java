package powercraft.machines;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCraftResult;
import powercraft.core.PC_GresCheckBox;
import powercraft.core.PC_GresFrame;
import powercraft.core.PC_GresGap;
import powercraft.core.PC_GresImage;
import powercraft.core.PC_GresInventory;
import powercraft.core.PC_GresInventoryBigSlot;
import powercraft.core.PC_GresInventoryPlayer;
import powercraft.core.PC_GresLayoutH;
import powercraft.core.PC_GresWidget;
import powercraft.core.PC_GresWindow;
import powercraft.core.PC_IGresClient;
import powercraft.core.PC_IGresGui;
import powercraft.core.PC_Module;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_Utils;
import powercraft.core.mod_PowerCraftCore;

public class PCma_GuiAutomaticWorkbench extends PCma_ContainerAutomaticWorkbench implements PC_IGresClient {

	private PC_GresCheckBox checkRedstone;
	
	public PCma_GuiAutomaticWorkbench(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(50, 50, PC_Utils.tr("tile.PCmaAutoWorkbench.name"));

		PC_GresWidget hg = new PC_GresLayoutH();
		PC_GresInventory inv = new PC_GresInventory(3, 3);

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				inv.setSlot(lSlot.get(x+y*3+1), x, y);
			}
		}

		hg.add(inv);

		PC_GresWidget hg1 = new PC_GresFrame();
		inv = new PC_GresInventory(3, 3);
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				inv.setSlot(lSlot.get(x+y*3+10), x, y);
			}
		}

		hg1.add(inv);

		hg1.add(new PC_GresImage(PC_Module.getModule("Core").getTextureDirectory() + "gres/widgets.png", 44, 66, 12, 11));

		hg1.add(new PC_GresInventoryBigSlot(lSlot.get(0)));

		hg.add(hg1);

		w.add(hg);

		w.add(checkRedstone = new PC_GresCheckBox(PC_Utils.tr("pc.gui.automaticWorkbench.redstoneActivated")));
		checkRedstone.check(teaw.redstoneActivated);
		w.add(new PC_GresGap(0, 3));

		w.add(new PC_GresInventoryPlayer(true));
		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		onCraftMatrixChanged(teaw);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		PC_PacketHandler.setTileEntity(teaw, "orderAndCraft");
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		PC_PacketHandler.setTileEntity(teaw, "redstoneActivated", checkRedstone.isChecked());
	}

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

}
