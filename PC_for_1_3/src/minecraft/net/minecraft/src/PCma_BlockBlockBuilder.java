package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Block dispenser machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockBlockBuilder extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, PC_ISpecialInventoryTextures, ITextureProvider {
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
		if (s == 1) {
			return TXTOP;
		}
		if (s == 0) {
			return TXDOWN;
		} else {
			if (m == s) {
				return TXFRONT;
			}
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) {
				return TXBACK;
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

	/**
	 * @param i ID
	 */
	protected PCma_BlockBlockBuilder(int i) {
		super(i, Material.ground);
		blockIndexInTexture = TXDOWN;
	}

	@Override
	public int tickRate() {
		return 1;
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
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {

				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {
					return false;
				}

			}
		}

		if (world.isRemote) {
			return true;
		}

		PC_Utils.openGres(entityplayer, "BlockBuilder", i, j, k);
		
		/*PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			PC_Utils.openGres(entityplayer, new PCma_GuiBlockBuilder(entityplayer, tileentity));
		}*/

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
	public TileEntity createNewTileEntity(World world) {
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
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		PCma_TileEntityBlockBuilder teb = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);


		if (teb != null) {
			PC_InvUtils.dropInventoryContents(teb, world, teb.getCoord());
		}
		super.breakBlock(world, i, j, k, par5, par6);
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		if (world.isBlockGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockGettingPowered(i, j - 1, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) {
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("HARVEST_STOP");
		set.add("BLOCK_DISPENSER");
		set.add("REDSTONE");
		set.add("MACHINE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("BLOCK_DISPENSER");
		return set;
	}
}
