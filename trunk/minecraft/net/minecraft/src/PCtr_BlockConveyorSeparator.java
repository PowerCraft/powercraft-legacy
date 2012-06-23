package net.minecraft.src;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

public class PCtr_BlockConveyorSeparator extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain, PC_IRotatedBox, ITextureProvider {
	public static final double MAX_HORIZONTAL_SPEED = 0.4D;
	public static final double HORIZONTAL_BOOST = 0.12D;
	public static final double BORDERS = 0.3D;
	public static final double BORDER_BOOST = 0.06D;

	public static final float HEIGHT_BOUNDS = PCtr_BeltBase.HEIGHT; // for
																				// detection
	public static final float HEIGHT_COLLISION = PCtr_BeltBase.HEIGHT_COLLISION;// to
																						// prevent
																						// falls
	public static final float HEIGHT_SELECTED = PCtr_BeltBase.HEIGHT_SELECTED;
																			// as
																			// collision.

	public static boolean group_wood_sort;

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
		if (i == 0) { return 1; // stone particles
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
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock) {
				if (Block.blocksList[ihold.itemID] instanceof PC_IBlockType && ihold.itemID != blockID) { return false; }
			}
		}

		PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
		PC_Utils.openGres(entityplayer, new PCtr_GuiConveyorSeparator(entityplayer, te));
		return true;
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCtr_TileEntitySeparationBelt();
	}

	protected PCtr_BlockConveyorSeparator(int i) {
		super(i, new PCtr_MaterialConveyor());

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, HEIGHT_BOUNDS, 1.0F);
		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
		for (int l = 0; l < te.getSizeInventory(); l++) {
			ItemStack itemstack = te.getStackInSlot(l);
			if (itemstack != null) {
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
				while (itemstack.stackSize > 0) {
					int i1 = world.rand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, new ItemStack(itemstack.itemID, i1,
							itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) world.rand.nextGaussian() * f3;
					entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}

		super.onBlockRemoval(world, i, j, k);
	}

	public boolean getIsBlockSolid(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
	}

	// public boolean canPlaceBlockAt(World world, int i, int j, int k)
	// {
	// return world.isAirBlock(i, j, k);
	// }

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		if (l == 2) {
			l = 8;
		}
		if (l == 3) {
			l = 9;
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public int getRotation(int meta) {
		switch (meta) {
			case 0:
			case 6:
				return 0;
			case 1:
			case 7:
				return 1;
			case 8:
			case 14:
				return 2;
			case 9:
			case 15:
				return 3;
		}
		return 0;
	}

	// MOVEMENT
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		
		PC_CoordI pos = new PC_CoordI(i,j,k);
		
		int redir = 0;

		if (entity instanceof EntityFX) { return; } // no derbish will be moved
		if (!entity.isEntityAlive()) { return; }
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) { return; }

		PCtr_TileEntitySeparationBelt filter = (PCtr_TileEntitySeparationBelt) world.getBlockTileEntity(i, j, k);
		redir = filter.newDirection(entity);

		if (world.rand.nextInt(25) == 0) {
			if (mod_PCcore.soundsEnabled) {
				world.playSoundEffect(i, j, k, "random.wood click", (world.rand.nextFloat() + 0.2F) / 6.0F,
						1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
			}
		}

		int meta = getRotation(world.getBlockMetadata(i, j, k));

		if (entity instanceof EntityItem && entity.motionY > 0.3) {
			entity.motionY *= 0.5;
		}

		// Z--
		if ((meta == 0 && redir == 0) || (meta == 1 && redir == 1) || (meta == 3 && redir == -1)) {
			if (!PCtr_BeltBase.isBlocked(world, pos.offset(0,0,-1))) {
				longlife(entity, world, i, j, k - 1);
				if (entity.motionZ >= -MAX_HORIZONTAL_SPEED) {
					entity.motionZ -= HORIZONTAL_BOOST;
				}
			}

			if (entity.posX > i + (1D - BORDERS)) {
				entity.motionX -= BORDER_BOOST;
			}
			if (entity.posX < i + BORDERS) {
				entity.motionX += BORDER_BOOST;
			}

			if (entity instanceof EntityItem && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0,0,-1), (EntityItem) entity)) { return; }
			return;
		}

		// X++
		if ((meta == 1 && redir == 0) || (meta == 0 && redir == -1) || (meta == 2 && redir == 1)) {
			if (!PCtr_BeltBase.isBlocked(world, pos.offset(1,0,0))) {
				longlife(entity, world, i + 1, j, k);
				if (entity.motionX <= MAX_HORIZONTAL_SPEED) {
					entity.motionX += HORIZONTAL_BOOST;
				}
			}

			if (entity.posZ > k + BORDERS) {
				entity.motionZ -= BORDER_BOOST;
			}
			if (entity.posZ < k + (1D - BORDERS)) {
				entity.motionZ += BORDER_BOOST;
			}
			if (entity instanceof EntityItem && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(1,0,0), (EntityItem) entity)) { return; }
			return;
		}

		// Z++
		if ((meta == 2 && redir == 0) || (meta == 1 && redir == -1) || (meta == 3 && redir == 1)) {
			if (!PCtr_BeltBase.isBlocked(world, pos.offset(0,0,1))) {
				longlife(entity, world, i, j, k + 1);
				if (entity.motionZ <= MAX_HORIZONTAL_SPEED) {
					entity.motionZ += HORIZONTAL_BOOST;
				}
			}

			if (entity.posX > i + (1D - BORDERS)) {
				entity.motionX -= BORDER_BOOST;
			}
			if (entity.posX < i + BORDERS) {
				entity.motionX += BORDER_BOOST;
			}

			if (entity instanceof EntityItem && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0,0,1), (EntityItem) entity)) { return; }
			return;
		}

		// X--
		if ((meta == 3 && redir == 0) || (meta == 0 && redir == 1) || (meta == 2 && redir == -1)) {
			if (!PCtr_BeltBase.isBlocked(world, pos.offset(-1,0,0))) {
				longlife(entity, world, i - 1, j, k);
				if (entity.motionX >= -MAX_HORIZONTAL_SPEED) {
					entity.motionX -= HORIZONTAL_BOOST;
				}// ok
			}
			if (entity.posX > k + BORDERS) {
				entity.motionX -= BORDER_BOOST;
			}
			if (entity.posX < k + (1D - BORDERS)) {
				entity.motionX += BORDER_BOOST;
			}

			if (entity instanceof EntityItem && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(-1,0,0), (EntityItem) entity)) { return; }
			return;
		}
	}

	public void longlife(Entity entity, World world, int i, int j, int k) {
		// longlife!
		if (entity instanceof EntityItem) {
			((EntityItem) entity).delayBeforeCanPickup = 10;
			if (((EntityItem) entity).age >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 50) {
					((EntityItem) entity).age = 4000;
				}
			}
		}

		if (entity instanceof EntityXPOrb) {
			if (((EntityXPOrb) entity).xpOrbAge >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 50) {
					((EntityXPOrb) entity).xpOrbAge = 4000;
				}
			}
		}
	}

	// collision and other stuff
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, 0.0F + j, k, (i + 1), (j + HEIGHT_COLLISION + 0.0F), (k + 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0;
		f = 0.0F + HEIGHT_SELECTED;
		return AxisAlignedBB.getBoundingBoxFromPool(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + HEIGHT_BOUNDS, 1.0F);
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
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}
}
