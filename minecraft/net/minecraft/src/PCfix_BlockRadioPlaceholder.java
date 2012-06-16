package net.minecraft.src;

import java.util.Random;

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

	//@formatter:off
	
	@Override
	public boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos) { return true; }
	@Override
	public boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos) { return true; }
	@Override
	public boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos) { return false; }
	@Override
	public boolean isBuilderIgnored() { return true; }
	@Override
	public boolean isConveyor(IBlockAccess world, PC_CoordI pos){ return false; }
	@Override
	public boolean isElevator(IBlockAccess world, PC_CoordI pos) { return false; }

	//@formatter:on

	@Override
	public TileEntity getBlockEntity() {
		return new PCfix_TileEntityRadioPlaceholder();
	}
}
