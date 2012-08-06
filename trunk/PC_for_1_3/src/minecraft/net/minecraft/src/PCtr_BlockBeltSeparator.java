package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * item separation belt
 * 
 * @author MightyPork
 */
public class PCtr_BlockBeltSeparator extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain, PC_IRotatedBox, ITextureProvider {

	@Override
	public boolean renderItemHorizontal() {
		return true;
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) {
			return 1; // stone particles
		}
		if (i == 1) {
			return 7; // top face
		} else {
			return 2; // side
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l != 1;
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
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if (PCtr_BeltBase.blockActivated(world, i, j, k, entityplayer)) {
			return true;
		} else {
			ItemStack ihold = entityplayer.getCurrentEquippedItem();
			if (ihold != null) {
				if (ihold.getItem() instanceof ItemBlock) {
					if (Block.blocksList[ihold.itemID] instanceof PC_IBlockType && ihold.itemID != blockID) {
						return false;
					}
				}
			}

			PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
			PC_Utils.openGres(entityplayer, new PCtr_GuiSeparationBelt(entityplayer, te));
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCtr_TileEntitySeparationBelt();
	}

	/**
	 * belt sep.
	 * 
	 * @param i ID
	 */
	protected PCtr_BlockBeltSeparator(int i) {
		super(i, new PCtr_MaterialConveyor());

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltBase.HEIGHT, 1.0F);
		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);

		if (te != null) {
			PC_InvUtils.dropInventoryContents(te, world, te.getCoord());
		}

		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltBase.getPlacedMeta(entityliving));
	}

	@Override
	public int getRotation(int meta) {
		return PCtr_BeltBase.getRotation(meta);
	}

	// MOVEMENT
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		int redir = 0;

		if (entity instanceof EntityFX) {
			return;
		} // no derbish will be moved
		if (!entity.isEntityAlive()) {
			return;
		}
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
			return;
		}

		PCtr_TileEntitySeparationBelt tes = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);

		// get relative direction.
		redir = tes.getDirection(entity);

		int rotation = getRotation(world.getBlockMetadata(i, j, k));

		// calculate final rotation
		rotation += redir;
		while (rotation >= 4)
			rotation -= 4;
		while (rotation < 0)
			rotation += 4;

		// offset coordinate
		PC_CoordI pos_leading_to = pos.copy();
		switch (rotation) {
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

		// try to store at offset coordinate
		if (entity instanceof EntityItem && PCtr_BeltBase.storeEntityItemAt(world, pos_leading_to, (EntityItem) entity)) {
			return;
		}

		// check if target position is blocked
		boolean leadsToNowhere = PCtr_BeltBase.isBlocked(world, pos_leading_to);

		// longlife if not leading to nowhere
		if (!leadsToNowhere) {
			PCtr_BeltBase.entityPreventDespawning(world, pos, true, entity);
		}

		leadsToNowhere = leadsToNowhere && PCtr_BeltBase.isBeyondStorageBorder(world, rotation, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

		// add motion.
		PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, rotation, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
				PCtr_BeltBase.HORIZONTAL_BOOST);
	}

	// collision and other stuff
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
		set.add("TRANSLUCENT");
		set.add("BELT");
		set.add("BELT_SEPARATOR");

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
