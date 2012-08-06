package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Redirection belt with redstone control
 * 
 * @author MightyPork
 */
public class PCtr_BlockBeltRedirector extends BlockContainer implements PC_IBlockType, PC_IRotatedBox, PC_ISwapTerrain, ITextureProvider {

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public boolean renderItemHorizontal() {
		return true;
	}

	/**
	 * @param i ID
	 */
	protected PCtr_BlockBeltRedirector(int i) {
		super(i, new PCtr_MaterialConveyor());
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltBase.HEIGHT, 1.0F);
		blockIndexInTexture = 0;
		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public int tickRate() {
		return 1;
	}

	@Override
	public boolean canProvidePower() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		return PCtr_BeltBase.blockActivated(world, i, j, k, entityplayer);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = PCtr_BeltBase.getPlacedMeta(entityliving);
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {}

	// from interface, but also used locally
	@Override
	public int getRotation(int meta) {
		return PCtr_BeltBase.getRotation(meta);
	}

	/**
	 * Check whether this belt is powered.
	 * 
	 * @param world the world
	 * @param pos belt pos
	 * @return is powered
	 */
	public boolean isPowered(World world, PC_CoordI pos) {
		return pos.isPoweredIndirectly(world) || pos.offset(0, 1, 0).isPoweredIndirectly(world) || pos.offset(0, -1, 0).isPoweredIndirectly(world);
	}

	// -------------MOVEMENT------------------
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (PCtr_BeltBase.isEntityIgnored(entity)) {
			return;
		}


		if (entity instanceof EntityItem) {
			PCtr_BeltBase.doSpecialItemAction(world, pos, (EntityItem) entity);
			if (PCtr_BeltBase.storeNearby(world, pos, (EntityItem) entity, false)) {
				return;
			}
		}

		PCtr_TileEntityRedirectionBeltBase teRedir = (PCtr_TileEntityRedirectionBeltBase) world.getBlockTileEntity(i, j, k);

		// get relative direction.
		int redir = teRedir.getDirection(entity);

		int direction = getRotation(pos.getMeta(world)) + redir;
		if (direction == -1) {
			direction = 3;
		}
		if (direction == 4) {
			direction = 0;
		}

		PC_CoordI pos_leading_to = pos.copy();
		switch (direction) {
			case 0: // Z--
				pos_leading_to.z--;
				break;

			case 1: // X++
				pos_leading_to.x++;
				break;

			case 2: // Z++
				pos_leading_to.z++;
				break;

			case 3: // X--
				pos_leading_to.x--;
				break;
		}

		boolean leadsToNowhere = PCtr_BeltBase.isBlocked(world, pos_leading_to);
		leadsToNowhere = leadsToNowhere && PCtr_BeltBase.isBeyondStorageBorder(world, direction, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

		// longlife!
		if (!leadsToNowhere) {
			PCtr_BeltBase.entityPreventDespawning(world, pos, true, entity);
		}

		double speed_max = PCtr_BeltBase.MAX_HORIZONTAL_SPEED;
		double boost = PCtr_BeltBase.HORIZONTAL_BOOST;

		PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, direction, speed_max, boost);

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + PCtr_BeltBase.HEIGHT_COLLISION + 0.0F), (k + 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0;
		f = 0.0F + PCtr_BeltBase.HEIGHT_SELECTED;
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + PCtr_BeltBase.HEIGHT, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 0.6F, 1.0F);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) {
			return 1;
		}
		if (i == 1) {
			return 8;
		} else {
			return 2;
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l != 1;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return getBlockTextureFromSideAndMetadata(l, iblockaccess.getBlockMetadata(i, j, k));
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.rotatedBoxRenderer;
	}

	@Override
	public String getTerrainFile() {
		return mod_PCtransport.getTerrainFile();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("BELT");
		set.add("BELT_REDIRECTOR");
		set.add("REDSTONE");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("BELT");
		return set;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCtr_TileEntityRedirectionBelt();
	}

}
