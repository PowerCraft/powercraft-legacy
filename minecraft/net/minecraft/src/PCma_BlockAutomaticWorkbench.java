package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Automatic Crafting table block
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockAutomaticWorkbench extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain, PC_ISpecialInventoryTextures, ITextureProvider {
	private static final int TXDOWN = 109, TXTOP = 154, TXSIDE = 138, TXFRONT = 106, TXBACK = 122;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
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

		PCma_TileEntityAutomaticWorkbench inventory = getTE(world, i, j, k);

		PC_Utils.openGres(entityplayer, new PCma_GuiAutomaticWorkbench(entityplayer, inventory));
		return true;
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCma_TileEntityAutomaticWorkbench();
	}

	/**
	 * Get AW tile entity at coords
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return the entity
	 */
	public PCma_TileEntityAutomaticWorkbench getTE(World world, int x, int y, int z) {
		return (PCma_TileEntityAutomaticWorkbench) world.getBlockTileEntity(x, y, z);
	}

	/**
	 * AutoWorkbench
	 * 
	 * @param i id
	 */
	protected PCma_BlockAutomaticWorkbench(int i) {
		super(i, 62, Material.ground);
		blockIndexInTexture = 62;
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		PCma_TileEntityAutomaticWorkbench tileentityconveyoract = (PCma_TileEntityAutomaticWorkbench) world.getBlockTileEntity(i, j, k);
		for (int l = 0; l < tileentityconveyoract.getSizeInventory(); l++) {
			ItemStack itemstack = tileentityconveyoract.getStackInSlot(l);
			if (itemstack != null) {
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
				while (itemstack.stackSize > 0) {
					int i1 = world.rand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) world.rand.nextGaussian() * f3;
					entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}

		super.onBlockRemoval(world, i, j, k);
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {

		// act uses a bit strange metadata values, thus when using this piece copied from other machines, meta must be converted to the
		// usual values.
		if (m == 0) {
			m = 2;
		} else if (m == 1) {
			m = 5;
		} else if (m == 2) {
			m = 3;
		} else if (m == 3) {
			m = 4;
		}

		if (s == 1) {
			return TXTOP;
		}
		if (s == 0) {
			return TXDOWN;
		} else {
			if (m == s) {
				return TXBACK;
			}
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) {
				return TXFRONT;
			}
			return TXSIDE;
		}
	}

	@Override
	public int getInvTexture(int i, int m) {
		if (i == 1) {
			return TXTOP;
		}
		if (i == 0) {
			return TXDOWN;
		}
		if (i == 3) {
			return TXFRONT;
		} else if (i == 4) {
			return TXBACK;
		} else {
			return TXSIDE;
		}
	}

	@Override
	public int tickRate() {
		return 4;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower()) {
			boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k) || world.isBlockIndirectlyGettingPowered(i, j - 1, k);
			if (flag) {
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k) || world.isBlockIndirectlyGettingPowered(i, j - 1, k)) {
			getTE(world, i, j, k).doCrafting();
		}
	}

	@Override
	public String getTerrainFile() {
		return mod_PCmachines.getTerrainFile();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}


	@Override
	public int getRenderType() {
		return PC_Renderer.swapTerrainRenderer;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("HARVEST_STOP");
		set.add("ACT");
		set.add("REDSTONE");
		set.add("MACHINE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}
}
