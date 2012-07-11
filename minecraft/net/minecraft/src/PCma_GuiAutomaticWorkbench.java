package net.minecraft.src;



/**
 * Automatic Workbench's GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiAutomaticWorkbench implements PC_IGresBase {



	private EntityPlayer entityplayer;
	private PCma_TileEntityAutomaticWorkbench tileentity;
	private IInventory craftResult;
	private PC_GresCheckBox checkRedstone;

	/**
	 * @param entityplayer player
	 * @param tileentity tile entity of the Automatic Workbench
	 */
	public PCma_GuiAutomaticWorkbench(EntityPlayer entityplayer, PCma_TileEntityAutomaticWorkbench tileentity) {
		this.entityplayer = entityplayer;
		this.tileentity = tileentity;
		craftResult = new InventoryCraftResult();
	}

	@Override
	public EntityPlayer getPlayer() {
		return entityplayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(50, 50, PC_Lang.tr("tile.PCmaAutoWorkbench.name"));

		PC_GresWidget hg = new PC_GresLayoutH();
		PC_GresInventory inv = new PC_GresInventory(3, 3);

		int cnt = 0;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				inv.setSlot(new PCma_SlotAutomaticWorkbenchInventory(tileentity, gui.getContainer(), false, cnt++, 0, 0), x, y);
			}
		}

		hg.add(inv);

		PC_GresWidget hg1 = new PC_GresFrame();
		inv = new PC_GresInventory(3, 3);
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				inv.setSlot(new PCma_SlotAutomaticWorkbenchInventory(tileentity, gui.getContainer(), true, cnt++, 0, 0), x, y);
			}
		}

		hg1.add(inv);

		hg1.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 44, 66, 12, 11));

		hg1.add(new PC_GresInventoryBigSlot(new PCma_SlotAutomaticWorkbenchResult(entityplayer, tileentity, craftResult, gui.getContainer(), 0, 0, 0)));

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
		tileentity.redstoneActivated = checkRedstone.isChecked();
		tileentity.orderAndCraft();
	}

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
	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, tileentity.getRecipeProduct());
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}
}
