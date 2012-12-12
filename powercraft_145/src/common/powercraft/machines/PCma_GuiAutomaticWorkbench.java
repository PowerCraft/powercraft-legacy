package powercraft.machines;

import net.minecraft.src.EntityPlayer;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresFrame;
import powercraft.management.PC_GresGap;
import powercraft.management.PC_GresImage;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryBigSlot;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils;

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

		hg1.add(new PC_GresImage(PC_Utils.getGresImgDir() + "widgets.png", 44, 66, 12, 11));

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
