package net.minecraft.src;



/**
 * Slot of the automatic workbench - result
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_SlotAutomaticWorkbenchResult extends Slot {
	private final PCma_TileEntityAutomaticWorkbench storageInv;
	private EntityPlayer thePlayer;
	private Container parent;

	/**
	 * Automatic workbench's slot
	 * 
	 * @param entityplayer player
	 * @param storage Storage inventory (left-hand side)
	 * @param result result inventory (with one slot)
	 * @param parent parent container
	 * @param i index
	 * @param j x
	 * @param k y
	 */
	public PCma_SlotAutomaticWorkbenchResult(EntityPlayer entityplayer, PCma_TileEntityAutomaticWorkbench storage, IInventory result,
			Container parent, int i, int j, int k) {
		super(result, i, j, k);
		thePlayer = entityplayer;
		storageInv = storage;
		this.parent = parent;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public void onPickupFromSlot(ItemStack itemstack) {
		itemstack.onCrafting(thePlayer.worldObj, thePlayer, itemstack.stackSize);

		if (itemstack.itemID == Block.workbench.blockID) {
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		} else if (itemstack.itemID == Item.pickaxeWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		} else if (itemstack.itemID == Block.stoneOvenIdle.blockID) {
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		} else if (itemstack.itemID == Item.hoeWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildHoe, 1);
		} else if (itemstack.itemID == Item.bread.shiftedIndex) {
			thePlayer.addStat(AchievementList.makeBread, 1);
		} else if (itemstack.itemID == Item.cake.shiftedIndex) {
			thePlayer.addStat(AchievementList.bakeCake, 1);
		} else if (itemstack.itemID == Item.pickaxeStone.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		} else if (itemstack.itemID == Item.swordWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildSword, 1);
		} else if (itemstack.itemID == Block.enchantmentTable.blockID) {
			thePlayer.addStat(AchievementList.enchantments, 1);
		} else if (itemstack.itemID == Block.bookShelf.blockID) {
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
		ModLoader.takenFromCrafting(thePlayer, itemstack, storageInv);

		storageInv.decrementRecipe();
		parent.onCraftMatrixChanged(storageInv);

	}
}
