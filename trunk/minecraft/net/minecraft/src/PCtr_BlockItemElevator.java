package net.minecraft.src;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


public class PCtr_BlockItemElevator extends Block implements PC_IBlockType, PC_ISwapTerrain, ITextureProvider {

	public static final double MAX_HORIZONTAL_SPEED = PCtr_BeltBase.MAX_HORIZONTAL_SPEED * 0.4D;
	public static final double HORIZONTAL_BOOST = PCtr_BeltBase.HORIZONTAL_BOOST * 0.5D;
	public static final double BORDERS3 = 0.45D;
	public static final double BORDERS2 = 0.25D;
	public static final double BORDERS = 0.1D;
	public static final double BORDER_BOOST = 0.07D;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	public PCtr_BlockItemElevator(int i, int tx) {
		super(i, tx, (new PCtr_MaterialElevator(MapColor.airColor)));
	}

	@Override
	protected int damageDropped(int i) {
		return i;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		PCtr_BlockConveyor.packItems(world, i, j, k);

		boolean down = world.getBlockMetadata(i, j, k) == 1;

		// longlife!
		if (entity instanceof EntityItem) {
			((EntityItem) entity).delayBeforeCanPickup = 10;
			if (((EntityItem) entity).age >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 60) {
					((EntityItem) entity).age = 4000;
				}
			}

			// storing

			if (((PCtr_BlockConveyor) mod_PCtransport.conveyorBelt).storeAllSides(world, i, j, k, (EntityItem) entity)) { return; }
		}

		if (entity instanceof EntityXPOrb) {
			if (((EntityXPOrb) entity).xpOrbAge >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 60) {
					((EntityXPOrb) entity).xpOrbAge = 4000;
				}
			}
		}

		// no derbish
		if (entity instanceof EntityFX) { return; }

		if (!entity.isEntityAlive()) { return; }

		boolean halted = (world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j - 1, k));

		double BBOOST = (entity instanceof EntityPlayer) ? BORDER_BOOST / 4.0D : BORDER_BOOST;

		int id = world.getBlockId(i, j + (down ? -1 : 1), k);

		if (entity instanceof EntityItem || entity instanceof EntityXPOrb) {
			if (entity.motionX > MAX_HORIZONTAL_SPEED) {
				entity.motionX *= 0.2D;
			}
			if (entity.motionX < -MAX_HORIZONTAL_SPEED) {
				entity.motionX *= 0.2D;
			}
			if (entity.motionZ > MAX_HORIZONTAL_SPEED) {
				entity.motionZ *= 0.2D;
			}
			if (entity.motionZ < -MAX_HORIZONTAL_SPEED) {
				entity.motionZ *= 0.2D;
			}
			if (Math.abs(entity.motionY) > 0.25D) {
				entity.motionY *= 0.3D;
			}
		}

		entity.fallDistance = 0;
		entity.updateFallState(0, true);

		if ((/* id != blockID || */halted) && entity instanceof EntityPlayer) {
			if (world.isAirBlock(i + 1, j, k) && !world.isAirBlock(i + 1, j - 1, k)) {

				if (entity.motionX <= (MAX_HORIZONTAL_SPEED)) {
					entity.motionX += HORIZONTAL_BOOST;
				}
				if (entity.posZ > k + BORDERS) {
					entity.motionZ -= BBOOST;
				}
				if (entity.posZ < k + (1D - BORDERS)) {
					entity.motionZ += BBOOST;
				}

			} else if (world.isAirBlock(i - 1, j, k) && !world.isAirBlock(i - 1, j - 1, k)) {

				if (entity.motionX >= (-MAX_HORIZONTAL_SPEED)) {
					entity.motionX -= HORIZONTAL_BOOST; /* entity.motionY+=0.1; */
				}// ok
				if (entity.posZ > k + BORDERS) {
					entity.motionZ -= BBOOST;
				}
				if (entity.posZ < k + (1D - BORDERS)) {
					entity.motionZ += BBOOST;
				}

			} else if (world.isAirBlock(i, j, k + 1) && !world.isAirBlock(i, j - 1, k + 1)) {

				if (entity.motionZ <= (MAX_HORIZONTAL_SPEED)) {
					entity.motionZ += HORIZONTAL_BOOST;
				}
				if (entity.posX > i + (1D - BORDERS)) {
					entity.motionX -= BBOOST;
				}
				if (entity.posX < i + BORDERS) {
					entity.motionX += BBOOST;
				}

			} else if (world.isAirBlock(i, j, k - 1) && world.isAirBlock(i, j - 1, k - 1)) {

				if (entity.motionZ >= (-MAX_HORIZONTAL_SPEED)) {
					entity.motionZ -= HORIZONTAL_BOOST;
				}
				if (entity.posX > i + (1D - BORDERS)) {
					entity.motionX -= BBOOST;
				}
				if (entity.posX < i + BORDERS) {
					entity.motionX += BBOOST;
				}
			}
		}

		if (id != blockID || (halted && !(entity instanceof EntityPlayer))) {
			if (!(entity instanceof EntityPlayer) && Math.abs(entity.motionY) > 0.3) {
				entity.motionY *= 0.3D;
			} else {
				entity.motionY += 0.02D;
			}
			// if(!(entity instanceof EntityPlayer) && !down && entity.posY >
			// j+0.7D) entity.motionY*= 0.3D;
			if ((down && entity.posY < j + 0.7D) || (!down && entity.posY > j + 0.1D)) {
				if (PCtr_BlockConveyor.isConveyorAt(world, i + 1, j, k)) {

					if (entity.motionX <= MAX_HORIZONTAL_SPEED) {
						entity.motionX += HORIZONTAL_BOOST * (down ? 1.2D : 1);
					}
					if (entity.posZ > k + BORDERS) {
						entity.motionZ -= BBOOST;
					}
					if (entity.posZ < k + (1D - BORDERS)) {
						entity.motionZ += BBOOST;
					}

				} else if (PCtr_BlockConveyor.isConveyorAt(world, i - 1, j, k)) {

					if (entity.motionX >= -MAX_HORIZONTAL_SPEED) {
						entity.motionX -= HORIZONTAL_BOOST * (down ? 1.2D : 1);
					}
					if (entity.posZ > k + BORDERS) {
						entity.motionZ -= BBOOST;
					}
					if (entity.posZ < k + (1D - BORDERS)) {
						entity.motionZ += BBOOST;
					}

				} else if (PCtr_BlockConveyor.isConveyorAt(world, i, j, k + 1)) {

					if (entity.motionZ <= MAX_HORIZONTAL_SPEED) {
						entity.motionZ += HORIZONTAL_BOOST * (down ? 1.2D : 1);
					}
					if (entity.posX > i + (1D - BORDERS)) {
						entity.motionX -= BBOOST;
					}
					if (entity.posX < i + BORDERS) {
						entity.motionX += BBOOST;
					}

				} else if (PCtr_BlockConveyor.isConveyorAt(world, i, j, k - 1)) {

					if (entity.motionZ >= -MAX_HORIZONTAL_SPEED) {
						entity.motionZ -= HORIZONTAL_BOOST * (down ? 1.2D : 1);
					}
					if (entity.posX > i + (1D - BORDERS)) {
						entity.motionX -= BBOOST;
					}
					if (entity.posX < i + BORDERS) {
						entity.motionX += BBOOST;
					}
				}
			}
		} else {
			if (!down) {
				if (entity.motionY < ((halted) ? 0.15 : 0.3)) {
					entity.motionY += ((halted) ? 0.06D : 0.1D);
				}
			}

			if (entity.posZ < k + BORDERS3) {
				entity.motionZ += BBOOST / 7D;
			}
			if (entity.posZ > k + (1D - BORDERS3)) {
				entity.motionZ -= BBOOST / 7D;
			}
			if (entity.posX < i + (1D - BORDERS3)) {
				entity.motionX += BBOOST / 7D;
			}
			if (entity.posX > i + BORDERS3) {
				entity.motionX -= BBOOST / 7D;
			}

			if (entity.posZ < k + BORDERS2) {
				entity.motionZ += BBOOST / 4D;
			}
			if (entity.posZ > k + (1D - BORDERS2)) {
				entity.motionZ -= BBOOST / 4D;
			}
			if (entity.posX < i + (1D - BORDERS2)) {
				entity.motionX += BBOOST / 4D;
			}
			if (entity.posX > i + BORDERS2) {
				entity.motionX -= BBOOST / 4D;
			}

			if (entity.posZ < k + BORDERS) {
				entity.motionZ += BBOOST;
			}
			if (entity.posZ > k + (1D - BORDERS)) {
				entity.motionZ -= BBOOST;
			}
			if (entity.posX < i + (1D - BORDERS)) {
				entity.motionX += BBOOST;
			}
			if (entity.posX > i + BORDERS) {
				entity.motionX -= BBOOST;
			}
		}

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		boolean down = world.getBlockMetadata(i, j, k) == 1;
		boolean bottom = world.getBlockId(i, j - 1, k) != blockID;
		if (down && bottom) { return mod_PCtransport.conveyorBelt.getCollisionBoundingBoxFromPool(world, i, j, k); }
		return null;
	}

	@Override
	public int getRenderBlockPass() {
		return 0;
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
	public int getRenderType() {
		return PC_Renderer.swapTerrainRenderer;
	}

	@Override
	public String getTerrainFile() {
		return mod_PCtransport.getTerrainFile();
	}

	@Override
	public int getBlockColor() {
		return 0xffffff;
	}

	@Override
	public int getRenderColor(int i) {
		return i == 0 ? 0xffffff : 0xff9999;
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int i, int j, int k) {
		return getRenderColor(world.getBlockMetadata(i, j, k));
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("TRANSLUCENT");
		set.add("LIFT");
		
		if(pos.getMeta(world)==0){
			set.add("LIFT_UP");
		}else{
			set.add("LIFT_DOWN");
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
