package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Indication light
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_BlockLight extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, ITextureProvider {
	private static boolean changingState = false;
	private boolean on;

	/**
	 * Light block
	 * 
	 * @param id block id
	 * @param lit is glowing
	 */
	protected PClo_BlockLight(int id, boolean lit) {
		super(id, 66, Material.circuits);
		on = lit;
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PClo_TileEntityLight();
	}


	@Override
	public String getTerrainFile() {
		return mod_PClogic.getTerrainFile();
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.swapTerrainRenderer;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
		if (l == 0 && world.isBlockNormalCube(i, j + 1, k)) {
			return true;
		}
		if (l == 1 && world.isBlockNormalCube(i, j - 1, k)) {
			return true;
		}
		if (l == 2 && world.isBlockNormalCube(i, j, k + 1)) {
			return true;
		}
		if (l == 3 && world.isBlockNormalCube(i, j, k - 1)) {
			return true;
		}
		if (l == 4 && world.isBlockNormalCube(i + 1, j, k)) {
			return true;
		}
		return l == 5 && world.isBlockNormalCube(i - 1, j, k);
	}

	@Override
	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		int i1 = 0;

		if (l == 1 && world.isBlockNormalCube(i, j - 1, k)) {
			i1 = 0;
		}
		if (l == 2 && world.isBlockNormalCube(i, j, k + 1)) {
			i1 = 1;
		} else if (l == 3 && world.isBlockNormalCube(i, j, k - 1)) {
			i1 = 2;
		} else if (l == 4 && world.isBlockNormalCube(i + 1, j, k)) {
			i1 = 3;
		} else if (l == 5 && world.isBlockNormalCube(i - 1, j, k)) {
			i1 = 4;
		}
		if (l == 0 && world.isBlockNormalCube(i, j + 1, k)) {
			i1 = 5;
		}
		world.setBlockMetadataWithNotify(i, j, k, i1);
		onPoweredBlockChange(world, i, j, k, world.isBlockIndirectlyGettingPowered(i, j, k) || isAttachmentBlockPowered(world, i, j, k, i1) || isBlockUnderAttachmentPowered(world, i, j, k, i1));
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (world.isBlockNormalCube(i, j - 1, k)) {
			return true;
		}
		if (world.isBlockNormalCube(i - 1, j, k)) {
			return true;
		}
		if (world.isBlockNormalCube(i + 1, j, k)) {
			return true;
		}
		if (world.isBlockNormalCube(i, j, k - 1)) {
			return true;
		}
		if (world.isBlockNormalCube(i, j + 1, k)) {
			return true;
		}
		return world.isBlockNormalCube(i, j, k + 1);
	}

	private int[] meta2side = { 1, 2, 3, 4, 5, 0 };

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (changingState) {
			return;
		}
		int sidemeta = world.getBlockMetadata(i, j, k);

		if (!canPlaceBlockOnSide(world, i, j, k, meta2side[sidemeta])) {
			world.setBlockWithNotify(i, j, k, 0);// drop -> onremoval
			return;
		}

		boolean powered = world.isBlockIndirectlyGettingPowered(i, j, k) || isAttachmentBlockPowered(world, i, j, k, sidemeta) || isBlockUnderAttachmentPowered(world, i, j, k, sidemeta);
		if (on && !powered) {
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
		} else if (!on && powered) {
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		int sidemeta = world.getBlockMetadata(i, j, k);
		boolean powered = world.isBlockIndirectlyGettingPowered(i, j, k) || isAttachmentBlockPowered(world, i, j, k, sidemeta) || isBlockUnderAttachmentPowered(world, i, j, k, sidemeta);
		if (on && !powered) {
			onPoweredBlockChange(world, i, j, k, false);
		} else if (!on && powered) {
			onPoweredBlockChange(world, i, j, k, true);
		}
	}

	/**
	 * Is block this light is attached to powered?
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side light side (meta)
	 * @return is powering the gate
	 */
	private boolean isAttachmentBlockPowered(World world, int x, int y, int z, int side) {
		if (side == 0) {
			return world.isBlockGettingPowered(x, y - 1, z);
		}
		if (side == 1) {
			return world.isBlockGettingPowered(x, y, z + 1);
		}
		if (side == 2) {
			return world.isBlockGettingPowered(x, y, z - 1);
		}
		if (side == 3) {
			return world.isBlockGettingPowered(x + 1, y, z);
		}
		if (side == 4) {
			return world.isBlockGettingPowered(x - 1, y, z);
		}
		if (side == 5) {
			return world.isBlockGettingPowered(x, y + 1, z);
		}
		return false;
	}

	/**
	 * Is block under the [block this light is attached to] powered?
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side light side (meta)
	 * @return is powering the gate
	 */
	private boolean isBlockUnderAttachmentPowered(World world, int x, int y, int z, int side) {
		return false;
//		if (side == 0) {
//			return world.isBlockGettingPowered(x, y - 2, z) && world.getBlockId(x, y - 2, z) != 0;
//		}
//		if (side == 1) {
//			return world.isBlockGettingPowered(x, y - 1, z + 1) && world.getBlockId(x, y - 1, z + 1) != 0;
//		}
//		if (side == 2) {
//			return world.isBlockGettingPowered(x, y - 1, z - 1) && world.getBlockId(x, y - 1, z - 1) != 0;
//		}
//		if (side == 3) {
//			return world.isBlockGettingPowered(x + 1, y - 1, z) && world.getBlockId(x + 1, y - 1, z) != 0;
//		}
//		if (side == 4) {
//			return world.isBlockGettingPowered(x - 1, y - 1, z) && world.getBlockId(x - 1, y - 1, z) != 0;
//		}
//		if (side == 5) {
//			return world.isBlockGettingPowered(x, y + 2, z) && world.getBlockId(x - 1, y + 2, z) != 0;
//		}
//		return false;
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		if (!changingState) {
			TileEntity te = world.getBlockTileEntity(i, j, k);
			if (te != null) {
				if (!PC_Utils.isCreative()) {
					PClo_TileEntityLight teg = (PClo_TileEntityLight) te;
					dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_PClogic.lightOn, 1, teg.getColor()));
				}
			}
		}
		super.onBlockRemoval(world, i, j, k);
	}

	/**
	 * Perform update after neighbor block changed.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param rs_state
	 */
	public static void onPoweredBlockChange(World world, int x, int y, int z, boolean rs_state) {
		int l = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		changingState = true;
		if (rs_state) {
			world.setBlockWithNotify(x, y, z, mod_PClogic.lightOn.blockID);
		} else {
			world.setBlockWithNotify(x, y, z, mod_PClogic.lightOff.blockID);
		}

		world.setBlockMetadataWithNotify(x, y, z, l);
		changingState = false;
		if (tileentity != null) {
			tileentity.validate();
			world.setBlockTileEntity(x, y, z, tileentity);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int i1 = iblockaccess.getBlockMetadata(i, j, k);

		float sidehalf = 0.1875F;
		float height = 0.125F;

		if (i1 == 0) // y-1
		{
			setBlockBounds((float) 0.5 - sidehalf, 0, (float) 0.5 - sidehalf, (float) 0.5 + sidehalf, height, (float) 0.5 + sidehalf);
		} else if (i1 == 1) // z+1
		{
			setBlockBounds((float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 1 - height, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf, 1);
		} else if (i1 == 2) // z-1
		{
			setBlockBounds((float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 0, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf, height);
		} else if (i1 == 3) // x+1
		{
			setBlockBounds(1 - height, (float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 1, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf);
		} else if (i1 == 4) // x-1
		{
			setBlockBounds(0, (float) 0.5 - sidehalf, (float) 0.5 - sidehalf, height, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf);
		}
		if (i1 == 5) // y+1
		{
			setBlockBounds((float) 0.5 - sidehalf, 1 - height, (float) 0.5 - sidehalf, (float) 0.5 + sidehalf, 1, (float) 0.5 + sidehalf);
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float sidehalf = 0.1875F;
		float height = 0.15F;
		setBlockBounds(0.5F - sidehalf, 0.5F - sidehalf, 0.5F - height / 2F, 0.5F + sidehalf, 0.5F + sidehalf, 0.5F + height / 2F);
	}

	@Override
	public int getBlockColor() {
		return 0xf0f0f0;
	}

	@Override
	public int getRenderColor(int i) // item
	{
		return PClo_TileEntityLight.getHexColor(i, true);
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return getColorHex(iblockaccess, i, j, k);
	}

	private int getColorHex(IBlockAccess w, int i, int j, int k) {
		TileEntity te = w.getBlockTileEntity(i, j, k);
		if (te == null) {
			return 0xff0000;
		}
		PClo_TileEntityLight tei = (PClo_TileEntityLight) te;
		return tei.getHexColor(on);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (!on) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		int color_hex = getColorHex(world, i, j, k);
		double ii = i + 0.5D;
		double jj = j + 0.5D;
		double kk = k + 0.5D;
		double h = 0.22D;

		double r = PC_Color.red(color_hex), g = PC_Color.green(color_hex), b = PC_Color.blue(color_hex);

		r = (r == 0) ? 0.001D : r;
		g = (g == 0) ? 0.001D : g;
		b = (b == 0) ? 0.001D : b;

		if (l == 0) {
			world.spawnParticle("reddust", ii, j + h, kk, r, g, b);
		} else if (l == 1) {
			world.spawnParticle("reddust", ii, jj, k + 1 - h, r, g, b);
		} else if (l == 2) {
			world.spawnParticle("reddust", ii, jj, k + h, r, g, b);
		} else if (l == 3) {
			world.spawnParticle("reddust", i + 1 - h, jj, kk, r, g, b);
		} else if (l == 4) {
			world.spawnParticle("reddust", i + h, jj, kk, r, g, b);
		}
		if (l == 5) {
			world.spawnParticle("reddust", i, jj + 1 - h, kk, r, g, b);
		}
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("REDSTONE");
		set.add("LOGIC");
		set.add("LED");
		set.add("ATTACHED");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("LED");
		set.add("ATTACHED");
		return set;
	}
}
