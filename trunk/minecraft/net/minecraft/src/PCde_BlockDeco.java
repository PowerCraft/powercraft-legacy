package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

/**
 * Decorative block;<br>
 * Subtypes: iron frame, redstone storage
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_BlockDeco extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain, ITextureProvider {

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCde_TileEntityDeco();
	}

	@Override
	public String getTerrainFile() {
		return mod_PCdeco.getTerrainFile();
	}

	/**
	 * Decorative block;
	 * 
	 * @param i id
	 * @param j texture index
	 * @param material block material
	 */
	public PCde_BlockDeco(int i, int j, Material material) {
		super(i, j, material);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		// item + particle
		if (meta == 0) { return 22; }
		if (meta == 1) { return 129; }
		return 0;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
		// colors particles
		PCde_TileEntityDeco ted = getTE(iblockaccess, x, y, z);
		if (ted.type == 0) { return 0xffffff; }
		if (ted.type == 1) { return 0xcc0000; }
		return 0xffffff;

	}

	@Override
	public int getRenderColor(int i) {
		if (i == 0) { return 0x999999; }
		if (i == 1) { return 0xcc0000; }
		return 0xffffff;
	}


	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		PCde_TileEntityDeco ted = getTE(iblockaccess, i, j, k);

		// frames
		if (ted.type == 0) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			return;
		}

		// storage block RS
		if (ted.type == 1) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			return;
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);


	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, ArrayList arraylist) {

		PCde_TileEntityDeco ted = getTE(world, x, y, z);

		if (ted.type == 0 || ted.type == 1) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			return;
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);
	}


	/**
	 * Get tile entity at position
	 * 
	 * @param iblockaccess block access
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity or null
	 */
	public PCde_TileEntityDeco getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) { return null; }
		return (PCde_TileEntityDeco) te;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		// in world - block
		PCde_TileEntityDeco ted = getTE(iblockaccess, x, y, z);
		if (ted.type == 0) { return 22; }
		if (ted.type == 1) { return 129; // only this used
		}
		return 0;

	}

	@Override
	protected int damageDropped(int i) {
		return 0;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return -1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (iblockaccess.getBlockMetadata(i, j, k) == 0) { return true; }
		return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {

		PCde_TileEntityDeco teg = getTE(world, x, y, z);

		if (teg != null) {

			if (!PC_Utils.isCreative()) {
				dropBlockAsItem_do(world, x, y, z, new ItemStack(mod_PCdeco.deco, 1, teg.type));
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public int getRenderBlockPass() {
		return 0;// solid
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
		return PCde_Renderer.decorativeBlockRenderer;
	}



	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		PCde_TileEntityDeco ted = getTE(world, pos.x, pos.y, pos.z);

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("DECORATIVE");
		set.add("PASSIVE");


		if (ted != null && ted.type == 1) {
			set.add("HARVEST_STOP");
		}
		if (ted != null && ted.type == 0) {
			set.add("TRANSLUCENT");
		}

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}

}
