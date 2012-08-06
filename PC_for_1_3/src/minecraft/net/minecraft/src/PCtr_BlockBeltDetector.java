package net.minecraft.src;


import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Detection belt
 * 
 * @author MightyPork
 */
public class PCtr_BlockBeltDetector extends Block implements PC_IBlockType, PC_IRotatedBox, PC_ISwapTerrain, ITextureProvider {

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
	public PCtr_BlockBeltDetector(int i) {
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
		return true;
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
		int meta = iblockaccess.getBlockMetadata(i, j, k);
		return PCtr_BeltBase.isActive(meta);
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isActive(world, i, j, k) && l == 1;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (isActive(world, i, j, k)) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}
	}


	@SuppressWarnings("rawtypes")
	private void setStateIfEntityInteractsWithDetector(World world, int i, int j, int k) {

		int meta = world.getBlockMetadata(i, j, k);
		boolean isAlreadyActive = PCtr_BeltBase.isActive(meta);
		boolean isPressed = false;
		List list = null;

		list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(i, j, k, (i + 1), j + 1D, (k + 1)));
		if (list.size() > 0) {
			isPressed = true;
		}

		boolean shallNotify = false;

		if (isPressed && !isAlreadyActive) { // turn on
			world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltBase.getActiveMeta(meta));
			shallNotify = true;

		} else if (!isPressed && isAlreadyActive) { // turn off
			world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltBase.getPassiveMeta(meta));
			shallNotify = true;

		}

		if (shallNotify) {
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);// below
			world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);// above
			world.markBlocksDirty(i, j, k, i, j, k);
			if (mod_PCcore.soundsEnabled) {
				world.playSoundEffect(i + 0.5D, j + 0.125D, k + 0.5D, "random.click", 0.15F, 0.5F);
			}
		}

		if (isPressed) {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}

		return;
	}

	private boolean isActive(World world, int i, int j, int k) {
		int meta = world.getBlockMetadata(i, j, k);
		return PCtr_BeltBase.isActive(meta);
	}

	// from interface, but also used locally
	@Override
	public int getRotation(int meta) {
		return PCtr_BeltBase.getRotation(meta);
	}

	// -------------MOVEMENT------------------
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (PCtr_BeltBase.isEntityIgnored(entity)) {
			return;
		}

		if (entity instanceof EntityItem) {
			PCtr_BeltBase.packItems(world, pos);
		}

		// detector activated
		if (!isActive(world, i, j, k)) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}

		if (entity instanceof EntityItem) {
			PCtr_BeltBase.doSpecialItemAction(world, pos, (EntityItem) entity);
			if (PCtr_BeltBase.storeNearby(world, pos, (EntityItem) entity, false)) {
				return;
			}
		}


		int direction = getRotation(world.getBlockMetadata(i, j, k));


		PC_CoordI pos_leading_to = pos.copy();
		switch (direction) {
			case 0: // Z--
				pos_leading_to.z--;
				break;

			case 1: // X++
				pos_leading_to.x++;
				break;

			// 6,7
			case 2: // Z++
				pos_leading_to.z++;
				break;

			case 3: // X--
				pos_leading_to.x--;
				break;
		}

		boolean leadsToNowhere = PCtr_BeltBase.isBlocked(world, pos_leading_to);
		leadsToNowhere = leadsToNowhere && PCtr_BeltBase.isBeyondStorageBorder(world, direction, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

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
			return 6;
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
		return 0;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("BELT");

		set.add("BELT_DETECTOR");
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
}
