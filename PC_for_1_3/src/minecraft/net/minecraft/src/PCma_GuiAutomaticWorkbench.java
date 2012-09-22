package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;



/**
 * Automatic Workbench's GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiAutomaticWorkbench extends PC_GresBase {

	private PCma_TileEntityAutomaticWorkbench tileentity;
	private IInventory craftResult;
	private PC_GresCheckBox checkRedstone;
	private List<Slot> lSlot = new ArrayList<Slot>();
	
	/**
	 * @param entityplayer player
	 * @param tileentity tile entity of the Automatic Workbench
	 */
	public PCma_GuiAutomaticWorkbench(EntityPlayer entityplayer, TileEntity tileentity) {
		this.player = entityplayer;
		this.tileentity = (PCma_TileEntityAutomaticWorkbench)tileentity;
		craftResult = new InventoryCraftResult();
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(50, 50, PC_Lang.tr("tile.PCmaAutoWorkbench.name"));

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

		hg1.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 44, 66, 12, 11));

		hg1.add(new PC_GresInventoryBigSlot(lSlot.get(0)));

		hg.add(hg1);

		w.add(hg);

		w.add(checkRedstone = new PC_GresCheckBox(PC_Lang.tr("pc.gui.automaticWorkbench.redstoneActivated")));
		checkRedstone.check(tileentity.redstoneActivated);
		w.add(new PC_GresGap(0, 3));

		w.add(new PC_GresInventoryPlayer(true));
		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		onCraftMatrixChanged(tileentity);
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		PC_Utils.setTileEntity(player, tileentity, "orderAndCraft");
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		PC_Utils.setTileEntity(player, tileentity, "redstoneActivated", checkRedstone.isChecked());
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
	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, tileentity.getRecipeProduct());
	}

	@Override
	public List<Slot> getAllSlots(Container c) {
		int cnt = 0;
		lSlot.add(new PCma_SlotAutomaticWorkbenchResult(player, tileentity, craftResult, c, 0, 0, 0));
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(tileentity, c, false, cnt++, 0, 0));
			}
		}
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(tileentity, c, true, cnt++, 0, 0));
			}
		}
		return lSlot;
	}

}
