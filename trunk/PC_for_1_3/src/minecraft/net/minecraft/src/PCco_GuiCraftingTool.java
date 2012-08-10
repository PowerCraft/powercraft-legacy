package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Crafting Tool GRES GUI
 * 
 * @author MightyPork & XOR19
 * @copy (c) 2012
 */
public class PCco_GuiCraftingTool implements PC_IGresBase {

	private PC_GresButton buttonPrev, buttonNext;
	private EntityPlayer player;
	private PC_GresInventory craftingToolInventory;
	private PC_GresInventoryBigSlot trashInventory;
	private PCco_CraftingToolManager craftingToolManager = new PCco_CraftingToolManager();
	private PC_GresButton buttonTrashAll;
	private PC_GresButton buttonSort;
	private PC_GresInventoryPlayer playerInventory;
	private static int page = 0;

	private static final int invWidth = 13;
	private static final int invHeight = 7;

	private List<Slot> slotList;
	
	/**
	 * @param player the player
	 */
	public PCco_GuiCraftingTool(EntityPlayer player) {
		this.player = player;
		player.addStat(AchievementList.openInventory, 1);
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.craftingTool.title"));
		w.padding.setTo(10, 4);
		w.gapUnderTitle = 10;

		w.setAlignH(PC_GresAlign.CENTER);
		w.setAlignV(PC_GresAlign.CENTER);


		PC_GresWidget hg, vg;

		craftingToolInventory = new PC_GresInventory(invWidth, invHeight);
		for (int i = 0; i < invWidth; i++) {
			for (int j = 0; j < invHeight; j++) {
				int indexSlot = 1 + j * invWidth + i;
				craftingToolInventory.setSlot(slotList.get(indexSlot), i, j);
			}
		}

		w.add(craftingToolInventory);

		hg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER);

		vg = new PC_GresLayoutV();
		vg.add(new PC_GresGap(0, 0).setWidgetMargin(2));
		vg.add(buttonPrev = (PC_GresButton) new PC_GresButton("<<<").setMinWidth(30));
		if (page <= 0) {
			page = 0;
			buttonPrev.enable(false);
		}

		trashInventory = new PC_GresInventoryBigSlot(slotList.get(0));
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.craftingTool.trashTitle")).setWidgetMargin(1));
		vg.add(trashInventory.setWidgetMargin(1));
		vg.add(buttonTrashAll = (PC_GresButton) new PC_GresButton(PC_Lang.tr("pc.gui.craftingTool.trashAll")).setButtonPadding(3, 3).setMinWidth(30)
				.setWidgetMargin(1));
		hg.add(vg);

		hg.add(playerInventory = new PC_GresInventoryPlayer(false));


		vg = new PC_GresLayoutV();
		vg.add(new PC_GresGap(0, 0).setWidgetMargin(2));
		vg.add(buttonNext = (PC_GresButton) new PC_GresButton(">>>").setMinWidth(30));
		if (page >= getMaxPages()) {
			page = getMaxPages();
			buttonNext.enable(false);
		}
		vg.add(new PC_GresGap(0, 0).setWidgetMargin(4)).setAlignH(PC_GresAlign.CENTER);
		vg.add(buttonSort = (PC_GresButton) new PC_GresButton(PC_Lang.tr("pc.gui.craftingTool.sort")).setButtonPadding(2, 4).setMinWidth(30));
		hg.add(vg);

		w.add(hg);
		gui.add(w);

		loadSlotsForCorrentPage(gui);

		actionPerformed(craftingToolInventory, gui);
	}

	private void loadSlotsForCorrentPage(PC_IGresGui gui) {
		for (int i = 0; i < invWidth; i++) {
			for (int j = 0; j < invHeight; j++) {

				int indexInList = page * invWidth * invHeight + j * invWidth + i + 1;

				craftingToolInventory.setSlot(slotList.get(indexInList), i, j);
				//((PCco_SlotDirectCrafting) craftingToolInventory.getSlot(i, j)).setProduct(craftingToolManager.getItemForSlotNumber(indexInList));

			}
		}

		actionPerformed(craftingToolInventory, gui);
	}

	private int getMaxPages() {
		return (craftingToolManager.stacklist.size() / (invWidth * invHeight));
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		player.inventory.closeChest();
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == craftingToolInventory || widget == trashInventory || widget == playerInventory.inv1 || widget == playerInventory.inv2) {
			for (int i = 0; i < invWidth; i++) {
				for (int j = 0; j < invHeight; j++) {
					Slot slot = craftingToolInventory.getSlot(i, j);
					if (slot != null) {
						((PCco_SlotDirectCrafting) (slot)).updateAvailability();
					}
				}
			}

		} else if (widget == buttonPrev) {
			page--;
			if (page <= 0) {
				page = 0;
				buttonPrev.enable(false);
			}
			if (page < getMaxPages()) {
				buttonNext.enable(true);
			}
			loadSlotsForCorrentPage(gui);

		} else if (widget == buttonNext) {
			page++;
			if (page >= getMaxPages()) {
				page = getMaxPages();
				buttonNext.enable(false);
			}
			if (page > 0) {
				buttonPrev.enable(true);
			}
			loadSlotsForCorrentPage(gui);
		} else if (widget == buttonTrashAll) {
			IInventory inv = player.inventory;
			for (int i = 0; i < inv.getSizeInventory() - 4; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null) {
					if (stack.itemID != mod_PCcore.craftingTool.shiftedIndex) {
						inv.setInventorySlotContents(i, null);
					}
				}
			}
			actionPerformed(craftingToolInventory, gui);
		} else if (widget == buttonSort) {
			InventoryPlayer inv = player.inventory;
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for (int i = 0; i < inv.getSizeInventory() - 4; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null) {
					inv.setInventorySlotContents(i, null);
					stacks.add(stack);
				}
			}

			if (stacks.size() == 0) return;

			PC_InvUtils.groupStacks(stacks);

			List<ItemStack> sorted = new ArrayList<ItemStack>();

			while (stacks.size() > 0) {
				ItemStack lowest = null;
				int indexLowest = -1;
				for (int i = 0; i < stacks.size(); i++) {
					ItemStack checked = stacks.get(i);
					if (checked == null) {
						indexLowest = i;
						break;
					}

					if (lowest == null
							|| (checked.itemID == mod_PCcore.craftingTool.shiftedIndex && lowest.itemID != mod_PCcore.craftingTool.shiftedIndex)
							|| ((lowest.itemID * 32000 * 64 + lowest.getItemDamage() * 64 + lowest.stackSize) > (checked.itemID * 32000 * 64
									+ checked.getItemDamage() * 64 + checked.stackSize) && lowest.itemID != mod_PCcore.craftingTool.shiftedIndex)) {
						lowest = checked;
						indexLowest = i;
					}
				}
				if (lowest != null) sorted.add(stacks.remove(indexLowest));
			}

			//Collections.reverse(sorted);

			for (ItemStack stack : sorted) {
				inv.addItemStackToInventory(stack);
			}

			actionPerformed(craftingToolInventory, gui);
		}
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
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public List<Slot> getAllSlots(Container c) {
		slotList = new ArrayList<Slot>();
		slotList.add(new PC_SlotTrash());
		for (int p = 0; p <= getMaxPages(); p++){
			for (int j = 0; j < invHeight; j++) {
				for (int i = 0; i < invWidth; i++) {
					int indexSlot = p * invWidth * invHeight + j * invWidth + i;
					Slot s = new PCco_SlotDirectCrafting(player, craftingToolManager.getItemForSlotNumber(indexSlot), indexSlot, 0, 0);
					s.xDisplayPosition = -999;
					s.yDisplayPosition = -999;
					slotList.add(s);
				}
			}
		}
		return slotList;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}

}
