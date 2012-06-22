package net.minecraft.src;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Radio device block with tile entity.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCfix_BlockRadioPlaceholder extends BlockContainer implements PC_IBlockType {

	/**
	 * @param id
	 * @param type 0-tx, 1-rx
	 */
	protected PCfix_BlockRadioPlaceholder(int id, int type) {
		super(id, Block.stone.blockIndexInTexture, Material.circuits);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.35F, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return 6;
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
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCfix_TileEntityRadioPlaceholder();
	}
	
	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {
		// NO_HARVEST, HARVEST_STOP, TRANSLUCENT

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("TRANSLUCENT");
		set.add("FIX_RADIO");

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}
}
