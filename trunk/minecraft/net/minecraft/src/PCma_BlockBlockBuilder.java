package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;

/**
 * Block dispenser machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockBlockBuilder extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, PC_IInvTextures, ITextureProvider {
	private static final int TXDOWN = 109, TXTOP = 156, TXSIDE = 140, TXFRONT = 108, TXBACK = 124;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
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
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		if (s == 1) { return TXTOP; }
		if (s == 0) {
			return TXDOWN;
		} else {
			if (m == s) { return TXFRONT; }
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) { return TXBACK; }
			return TXSIDE;
		}
	}

	@Override
	public int getInvTexture(int i, int m) {
		if (i == 1) { return TXTOP; }
		if (i == 0) { return TXDOWN; }
		if (i == 3) {
			return TXFRONT;
		} else if (i == 4) {
			return TXBACK;
		} else {
			return TXSIDE;
		}
	}

	private Random random;

	/**
	 * @param i ID
	 */
	protected PCma_BlockBlockBuilder(int i) {
		super(i, Material.ground);
		random = new Random();
		blockIndexInTexture = TXDOWN;
	}

	@Override
	public int tickRate() {
		return 4;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return blockID;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		setDispenserDefaultDirection(world, i, j, k);
	}

	private void setDispenserDefaultDirection(World world, int i, int j, int k) {
		if (!world.isRemote) {
			int l = world.getBlockId(i, j, k - 1);
			int i1 = world.getBlockId(i, j, k + 1);
			int j1 = world.getBlockId(i - 1, j, k);
			int k1 = world.getBlockId(i + 1, j, k);
			byte byte0 = 3;
			if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				byte0 = 3;
			}
			if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				byte0 = 2;
			}
			if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				byte0 = 5;
			}
			if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				byte0 = 4;
			}
			world.setBlockMetadataWithNotify(i, j, k, byte0);
		}
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {

				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) { return false; }

			}
		}

		if (world.isRemote) { return true; }

		PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			ModLoader.openGUI(entityplayer, new PCma_GuiBlockBuilder(entityplayer.inventory, tileentity));
		}

		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower()) {
			boolean flag = isIndirectlyPowered(world, i, j, k);
			if (flag) {
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (!world.isRemote && isIndirectlyPowered(world, i, j, k)) {
			PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);
			if (tileentity != null) {
				tileentity.useItem();
			}
		}
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCma_TileEntityBlockBuilder();
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		if (l == 0) {
			l = 2;
		} else if (l == 1) {
			l = 5;
		} else if (l == 2) {
			l = 3;
		} else if (l == 3) {
			l = 4;
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			for (int l = 0; l < tileentity.getSizeInventory(); l++) {
				ItemStack itemstack = tileentity.getStackInSlot(l);
				if (itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					float f2 = random.nextFloat() * 0.8F + 0.1F;
					while (itemstack.stackSize > 0) {
						int i1 = random.nextInt(21) + 10;
						if (i1 > itemstack.stackSize) {
							i1 = itemstack.stackSize;
						}
						itemstack.stackSize -= i1;
						EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, new ItemStack(itemstack.itemID, i1,
								itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float) random.nextGaussian() * f3;
						entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) random.nextGaussian() * f3;
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

		}
		super.onBlockRemoval(world, i, j, k);
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		if (world.isBlockGettingPowered(i, j, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) { return true; }

		if (world.isBlockGettingPowered(i, j - 1, k)) { return true; }

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) { return true; }
		return false;
	}

	//@formatter:off
	
	@Override
	public boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos) { return false; }
	@Override
	public boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos) { return false; }
	@Override
	public boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos) { return true; }
	@Override
	public boolean isBuilderIgnored() { return true; }
	@Override
	public boolean isConveyor(IBlockAccess world, PC_CoordI pos){ return false; }
	@Override
	public boolean isElevator(IBlockAccess world, PC_CoordI pos) { return false; }
	
	//@formatter:on
}
