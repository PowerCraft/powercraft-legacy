package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Experience bank block.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockXPBank extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, ITextureProvider {
	@Override
	public String getTerrainFile() {
		return mod_PCmachines.getTerrainFile();
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCma_TileEntityXPBank();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public int getRenderType() {
		return PCma_Renderer.xpbankRenderer;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		return 24;
	}

	/**
	 * XP bank block
	 * 
	 * @param i id
	 */
	protected PCma_BlockXPBank(int i) {
		super(i, Material.ground);
		setStepSound(Block.soundPowderFootstep);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	/** Flag that it is being renderer. */
	public boolean rendering = false;

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		if (rendering) {
			int xp = ((PCma_TileEntityXPBank) iblockaccess.getBlockTileEntity(i, j, k)).xp;

			setBlockBounds(0.15F, 0.29F - 0.2F * calculateHeightMultiplier(xp), 0.15F, 0.85F, 0.71F + 0.2F * calculateHeightMultiplier(xp), 0.85F);
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	private float calculateHeightMultiplier(int xp) {
		// int level = 0;
		// for(;xp>0;){
		// xp -= (7 + (level++ * 7 >> 1));
		// }

		return Math.min(xp / 500F, 1F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.9F, 0.8F);
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				if (Block.blocksList[ihold.getItem().shiftedIndex] instanceof PC_IBlockType) {
					return false;
				}
			}
		}

		PC_Utils.openGres(entityplayer, new PCma_GuiXPBank((PCma_TileEntityXPBank) world.getBlockTileEntity(i, j, k)));

		return true;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return ((PCma_TileEntityXPBank) iblockaccess.getBlockTileEntity(i, j, k)).xp > 0;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		try {
			((PCma_TileEntityXPBank) world.getBlockTileEntity(i, j, k)).withdrawXP(entityplayer);
		} catch (NullPointerException npe) {

		}

		super.harvestBlock(world, entityplayer, i, j, k, l);
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("HARVEST_STOP");
		set.add("XP_BANK");
		set.add("MACHINE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("XP_BANK");
		return set;
	}
}
