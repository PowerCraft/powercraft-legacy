package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;

/**
 * Laser machine (sybtypes: tripwire, killer, senser, receiver)
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_BlockLaser extends BlockContainer implements PC_IBlockType, PC_IInvTextures, PC_ISwapTerrain, ITextureProvider {
	private static final int TXWOOD = 4, TXGUNON = 20;

	@Override
	public TileEntity getBlockEntity() {
		return new PCma_TileEntityLaser();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	// but also render as normal block
	@Override
	public int getRenderType() {
		return PCma_Renderer.laserRenderer;
	}

	// only for item renderer
	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		if (s == 1) { return TXWOOD; }
		if (s == 0) {
			return TXWOOD;
		} else {
			if (m == s) { return TXGUNON; }
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) { return TXWOOD; }
			return TXWOOD;
		}
	}

	@Override
	public int getInvTexture(int i, int m) {
		if (i == 1) { return TXWOOD; }
		if (i == 0) { return TXWOOD; }
		if (i == 3) {
			return TXGUNON;
		} else if (i == 4) {
			return TXWOOD;
		} else {
			return TXWOOD;
		}
	}

	/**
	 * @param i ID
	 */
	protected PCma_BlockLaser(int i) {
		super(i, Material.ground);
		setStepSound(Block.soundMetalFootstep);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (double) i + 1, (double) j + 0.7F, (double) k + 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.8F);
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		boolean reverse = PC_Utils.isPlacingReversed();


		if (reverse) {
			l = PC_Utils.reverseSide(l);
		}

		// translate
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

		if (entityliving instanceof EntityPlayer) {
			PCma_TileEntityLaser te = (PCma_TileEntityLaser) world.getBlockTileEntity(i, j, k);

			if (te == null) { return; }

			if (world.getBlockId(i, j - 1, k) == mod_PCmachines.roaster.blockID) {
				te.setType(PCma_LaserType.KILLER);
			} else {
				ModLoader.openGUI((EntityPlayer) entityliving, new PCma_GuiLaserTypeDecide(te));
			}
		}
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
		return ((PCma_TileEntityLaser) iblockaccess.getBlockTileEntity(i, j, k)).active;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		super.onBlockRemoval(world, i, j, k);
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public String getTerrainFile() {
		return mod_PCmachines.getTerrainFile();
	}

	//@formatter:off
	
	@Override
	public boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos) { return false; }
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
