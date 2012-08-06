package net.minecraft.src;


import java.util.HashSet;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * belt elevator
 * 
 * @author MightyPork
 */
public class PCtr_BlockItemElevator extends Block implements PC_IBlockType, PC_ISwapTerrain, ITextureProvider {

	private static final double BORDERS = 0.25D;
	private static final double BORDER_BOOST = 0.062D;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	/**
	 * elevator and descender
	 * 
	 * @param i id
	 * @param tx texture
	 */
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

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (PCtr_BeltBase.isEntityIgnored(entity)) {
			return;
		}

		if (entity instanceof EntityItem) {
			PCtr_BeltBase.packItems(world, pos);
			if (PCtr_BeltBase.storeItemIntoMinecart(world, pos, (EntityItem) entity) || PCtr_BeltBase.storeAllSides(world, pos, (EntityItem) entity)) {
				entity.setDead();
				return;
			}
		}

		boolean down = (pos.getMeta(world) == 1);

		PCtr_BeltBase.entityPreventDespawning(world, pos, true, entity);

		boolean halted = world.isBlockGettingPowered(i, j, k);

		double BBOOST = (entity instanceof EntityPlayer) ? BORDER_BOOST / 4.0D : BORDER_BOOST;

		int id = world.getBlockId(i, j + (down ? -1 : 1), k);

		if (Math.abs(entity.motionY) > 0.4D) {
			entity.motionY *= 0.3D;
		}

		entity.fallDistance = 0;
		entity.updateFallState(0, true);

		if (id != blockID || halted) {
			if (entity instanceof EntityLiving) {

				int side = -1;

				if ((PCtr_BeltBase.isConveyorAt(world, pos.offset(1, 0, 0)) && world.isAirBlock(i + 1, j + 1, k))) {

					side = 1;

				} else if ((PCtr_BeltBase.isConveyorAt(world, pos.offset(-1, 0, 0)) && world.isAirBlock(i - 1, j + 1, k))) {

					side = 3;

				} else if ((PCtr_BeltBase.isConveyorAt(world, pos.offset(0, 0, 1)) && world.isAirBlock(i, j + 1, k + 1))) {

					side = 2;

				} else if ((PCtr_BeltBase.isConveyorAt(world, pos.offset(0, 0, -1)) && world.isAirBlock(i, j + 1, k - 1))) {

					side = 0;

				} else if ((world.isAirBlock(i + 1, j, k) && !world.isAirBlock(i + 1, j - 1, k))) {

					side = 1;

				} else if ((world.isAirBlock(i - 1, j, k) && !world.isAirBlock(i - 1, j - 1, k))) {

					side = 3;

				} else if ((world.isAirBlock(i, j, k + 1) && !world.isAirBlock(i, j - 1, k + 1))) {

					side = 2;

				} else if ((world.isAirBlock(i, j, k - 1) && !world.isAirBlock(i, j - 1, k - 1))) {

					side = 0;
				}
				if (side != -1) {
					PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, true, side, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
							PCtr_BeltBase.HORIZONTAL_BOOST);
				}

			} else {

				if ((down && entity.posY < j + 0.6D) || (!down && entity.posY > j + 0.1D)) {
					if (PCtr_BeltBase.isConveyorAt(world, pos.offset(1, 0, 0))) {

						PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, true, 1, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
								PCtr_BeltBase.HORIZONTAL_BOOST * (down ? 1.2D : 1));

					} else if (PCtr_BeltBase.isConveyorAt(world, pos.offset(-1, 0, 0))) {


						PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, true, 3, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
								PCtr_BeltBase.HORIZONTAL_BOOST * (down ? 1.2D : 1));

					} else if (PCtr_BeltBase.isConveyorAt(world, pos.offset(0, 0, 1))) {

						PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, true, 2, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
								PCtr_BeltBase.HORIZONTAL_BOOST * (down ? 1.2D : 1));

					} else if (PCtr_BeltBase.isConveyorAt(world, pos.offset(0, 0, -1))) {

						PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, true, 0, PCtr_BeltBase.MAX_HORIZONTAL_SPEED,
								PCtr_BeltBase.HORIZONTAL_BOOST * (down ? 1.2D : 1));

					}
				}
			}
		} else {

			if (!down) {
				if (entity.motionY < ((id != blockID || halted) ? 0.2D : 0.3D)) {
					entity.motionY = ((id != blockID || halted) ? 0.2D : 0.3D);
					if (entity.onGround) {
						entity.moveEntity(0, 0.01D, 0);
					}
				}
			}

			if (entity.posX > pos.x + (1D - BORDERS)) {
				entity.motionX -= BBOOST;
			}

			if (entity.posX < pos.x + BORDERS) {
				entity.motionX += BBOOST;
			}
			if (entity.posZ > pos.z + BORDERS) {
				entity.motionZ -= BBOOST;
			}

			if (entity.posZ < pos.z + (1D - BORDERS)) {
				entity.motionZ += BBOOST;
			}

			if (!(id != blockID || halted)) {
				entity.motionZ = MathHelper.clamp_float((float) entity.motionZ, (float) -(BORDER_BOOST * 1.5D), (float) (BORDER_BOOST * 1.5D));
				entity.motionX = MathHelper.clamp_float((float) entity.motionX, (float) -(BORDER_BOOST * 1.5D), (float) (BORDER_BOOST * 1.5D));
			}
		}

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		boolean down = world.getBlockMetadata(i, j, k) == 1;
		boolean bottom = world.getBlockId(i, j - 1, k) != blockID;
		if (down && bottom) {
			return mod_PCtransport.conveyorBelt.getCollisionBoundingBoxFromPool(world, i, j, k);
		}
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

		if (pos.getMeta(world) == 0) {
			set.add("LIFT_UP");
		} else {
			set.add("LIFT_DOWN");
		}

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("LIFT");
		return set;
	}
}
