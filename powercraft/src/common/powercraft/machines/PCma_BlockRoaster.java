package powercraft.machines;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PCma_BlockRoaster extends PC_Block {
	private static final int TXDOWN = 62, TXTOP = 61, TXSIDE = 46;
	
	protected PCma_BlockRoaster(int id) {
		super(id, TXDOWN, Material.ground);
		setLightOpacity(0);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}
	
	@Override
	public String getDefaultName() {
		return "Roaster";
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		if (s == 1) {
			return TXTOP;
		}
		if (s == 0) {
			return TXDOWN;
		} else {
			return TXSIDE;
		}
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
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				if (Block.blocksList[ihold.getItem().shiftedIndex] instanceof PC_IBlockType) {
					return false;
				}
			}
		}

		if (world.isRemote) {
			return true;
		}

		PC_Utils.openGres(entityplayer, PCma_GuiRoaster.class, i, j, k);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCma_TileEntityRoaster();
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		PCma_TileEntityRoaster te = (PCma_TileEntityRoaster) world.getBlockTileEntity(i, j, k);

		if (te != null) {
			PC_InvUtils.dropInventoryContents(te, world, te.getCoord());
		}
		super.breakBlock(world, i, j, k, par5, par6);
	}

	/**
	 * Is getting power - should burn
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return is powered
	 */
	public static boolean isIndirectlyPowered(World world, int x, int y, int z) {
		if (world.isBlockGettingPowered(x, y, z)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
			return true;
		}

		if (world.isBlockGettingPowered(x, y - 1, z)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(x, y - 1, z)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if device at pos has fuel
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return has fuel
	 */
	private static boolean hasFuel(World world, int x, int y, int z) {
		try {
			return ((PCma_TileEntityRoaster) world.getBlockTileEntity(x, y, z)).burnTime > 0;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Check if device is enthering.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return is making nether field
	 */
	private boolean isNethering(World world, int x, int y, int z) {
		try {
			return ((PCma_TileEntityRoaster) world.getBlockTileEntity(x, y, z)).netherTime > 0 && isIndirectlyPowered(world, x, y, z);
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Check if roaster is burning
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return is burning
	 */
	public static boolean isBurning(World world, int x, int y, int z) {
		return isIndirectlyPowered(world, x, y, z) && hasFuel(world, x, y, z);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (isBurning(world, i, j, k)) {
			if (random.nextInt(24) == 0) {
				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
			}

			for (int c = 0; c < 5; c++) {
				float y = j + 0.74F + (random.nextFloat() * 0.3F);
				float x = i + 0.2F + (random.nextFloat() * 0.6F);
				float z = k + 0.2F + (random.nextFloat() * 0.6F);

				world.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", x, y, z, 0.0D, 0.0D, 0.0D);
			}

			for (int c = 0; c < 5; c++) {
				float y = j + 1.3F;
				float x = i + 0.2F + (random.nextFloat() * 0.6F);
				float z = k + 0.2F + (random.nextFloat() * 0.6F);

				world.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
			}

		}

		if (isNethering(world, i, j, k)) {
			for (int c = 0; c < 8; c++) {
				float y = j + 0.74F + (random.nextFloat() * 0.3F);
				float x = i + 0.2F + (random.nextFloat() * 0.6F);
				float z = k + 0.2F + (random.nextFloat() * 0.6F);

				world.spawnParticle("reddust", x, y, z, 0.0D, 0.0D, 0.0D);
			}

			for (int c = 0; c < 20; c++) {
				float y = (float) j + -2 + (random.nextFloat() * 4F);
				float x = (float) i + -6 + (random.nextFloat() * 12F);
				float z = (float) k + -6 + (random.nextFloat() * 12F);

				world.spawnParticle("reddust", x, y, z, 0.6D, 0.001D, 0.001D);
			}
		}

	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("HARVEST_STOP");
		set.add("ROASTER");
		set.add("FURNACE");
		set.add("MACHINE");

		if (isBurning(world, pos.x, pos.y, pos.z)) {
			set.add("SMOKE");
		}

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("ROASTER");
		return set;
	}
	
}
