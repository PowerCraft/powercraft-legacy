package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

/**
 * Entity Proximity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PClo_BlockSensor extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain {

	/**
	 * proximity sensor
	 * 
	 * @param id block ID
	 */
	protected PClo_BlockSensor(int id) {
		super(id, 6, Material.circuits);
		setLightOpacity(0);
		opaqueCubeLookup[id] = false;

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PClo_TileEntitySensor();
	}

	@Override
	protected int damageDropped(int i) {
		return i;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		setBlockBounds(0F, 0F, 0F, 1F, 0.255F, 1F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(0.375F, 0.2F, 0.375F, 1F - 0.375F, 0.7F, 1F - 0.375F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(0.3125F, 0.5F, 0.3125F, 1F - 0.3125F, 0.875F, 1F - 0.3125F);
		super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer player) {
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) { return false; }
			}
		}

		changeDelay(world, new PC_CoordI(i, j, k), (player.isSneaking() ? -1 : 1));
		return true;
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		printRange(world, new PC_CoordI(i, j, k));
	}

	/**
	 * Change sensor delay (add/subtract one)
	 * 
	 * @param world the world
	 * @param pos position in world
	 * @param increment (+1/-1)
	 */
	public static void changeDelay(World world, PC_CoordI pos, int increment) {
		PClo_TileEntitySensor ent = (PClo_TileEntitySensor) pos.getTileEntity(world);
		ent.changeRange(increment);
	}

	/**
	 * SHow current range (distance) using chat.
	 * 
	 * @param world the world
	 * @param pos device position.
	 */
	public static void printRange(World world, PC_CoordI pos) {
		PClo_TileEntitySensor ent = (PClo_TileEntitySensor) pos.getTileEntity(world);
		ent.printRange();
	}

	@Override
	public String getTerrainFile() {
		return mod_PClogic.getTerrainFile();
	}

	@Override
	public int getRenderType() {
		return PClo_Renderer.sensorRenderer;
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
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (!world.getBlockMaterial(i, j - 1, k).isSolid()) {
			return false;
		} else {
			return super.canPlaceBlockAt(world, i, j, k);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return ((PClo_TileEntitySensor) iblockaccess.getBlockTileEntity(i, j, k)).active;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (!((PClo_TileEntitySensor) world.getBlockTileEntity(i, j, k)).active) { return; }

		double ii = i + 0.2D + random.nextDouble() * 0.6;
		double jj = j + 0.5D + random.nextDouble() * 0.4;
		double kk = k + 0.2D + random.nextDouble() * 0.6;

		world.spawnParticle("reddust", ii, jj, kk, 0, 0, 0);
	}


	@Override
	public int getBlockTexture(IBlockAccess world, int i, int j, int k, int l) {
		int meta = world.getBlockMetadata(i, j, k);

		// this is used only for particles (the lower side makes particles)
		switch (meta) {
			case 0:
				return 4;
			case 1:
				return 6;
			case 2:
				return 37;
		}
		return 48;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		switch (m) {
			case 0:
				return 4;
			case 1:
				return 6;
			case 2:
				return 37;
		}
		return 48;
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




}
