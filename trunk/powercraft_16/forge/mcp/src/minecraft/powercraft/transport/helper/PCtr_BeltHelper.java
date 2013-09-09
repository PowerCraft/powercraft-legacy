package powercraft.transport.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3I;
import powercraft.api.PC_MathHelper;

public class PCtr_BeltHelper {

	public static final float HEIGHT = 0.0625F;
	public static final float HEIGHT_SELECTED = HEIGHT;
	public static final float HEIGHT_COLLISION = HEIGHT;
	public static final double MAX_HORIZONTAL_SPEED = 0.5F;
	public static final double HORIZONTAL_BOOST = 0.14D;
	public static final double BORDERS = 0.35D;
	public static final double BORDER_BOOST = 0.063D;
	public static final float STORAGE_BORDER = 0.5F;
	public static final float STORAGE_BORDER_LONG = 0.8F;
	public static final float STORAGE_BORDER_V = 0.6F;

	public static void moveEntityOnBelt(World world, Entity entity,
			PC_Vec3I current_pos, boolean bordersEnabled,
			boolean motionEnabled, PC_Direction direction)
	{
		System.out.println(direction);
		switch (direction)
		{
			case NORTH:
				if (entity.motionZ >= -MAX_HORIZONTAL_SPEED && motionEnabled) {
					entity.addVelocity(0, 0, -HORIZONTAL_BOOST);
				}

				if (bordersEnabled) {
					if (entity.posX > current_pos.x + (1D - BORDERS)) {
						entity.addVelocity(-BORDER_BOOST, 0, 0);
					}

					if (entity.posX < current_pos.x + BORDERS) {
						entity.addVelocity(BORDER_BOOST, 0, 0);
					}
				}

				break;

			case EAST:
				if (entity.motionX <= MAX_HORIZONTAL_SPEED && motionEnabled) {
					entity.addVelocity(HORIZONTAL_BOOST, 0, 0);
				}

				if (bordersEnabled) {
					if (entity.posZ > current_pos.z + BORDERS) {
						entity.addVelocity(0, 0, -BORDER_BOOST);
					}

					if (entity.posZ < current_pos.z + (1D - BORDERS)) {
						entity.addVelocity(0, 0, BORDER_BOOST);
					}
				}

				break;

			case SOUTH:
				if (entity.motionZ <= MAX_HORIZONTAL_SPEED && motionEnabled) {
					entity.addVelocity(0, 0, HORIZONTAL_BOOST);
				}

				if (bordersEnabled) {
					if (entity.posX > current_pos.x + (1D - BORDERS)) {
						entity.addVelocity(-BORDER_BOOST, 0, 0);
					}

					if (entity.posX < current_pos.x + BORDERS) {
						entity.addVelocity(BORDER_BOOST, 0, 0);
					}
				}

				break;

			case WEST:
				if (entity.motionX >= -MAX_HORIZONTAL_SPEED && motionEnabled) {
					entity.addVelocity(-HORIZONTAL_BOOST, 0, 0);
				}

				if (bordersEnabled) {
					if (entity.posZ > current_pos.z + BORDERS) {
						entity.addVelocity(0, 0, -BORDER_BOOST);
					}

					if (entity.posZ < current_pos.z + (1D - BORDERS)) {
						entity.addVelocity(0, 0, BORDER_BOOST);
					}
				}

				break;

			case UP:

				if (Math.abs(entity.motionY) > 0.4D) {
					entity.motionY *= 0.3D;
				}

				entity.fallDistance = 0;

				if (entity.motionY < (motionEnabled ? 0.2D : 0.3D)) {
					entity.motionY = (motionEnabled ? 0.2D : 0.3D);
				}

				if (bordersEnabled) {
					if (entity.posX > current_pos.x + (1D - BORDERS)) {
						entity.motionX -= BORDER_BOOST;
					}

					if (entity.posX < current_pos.x + BORDERS) {
						entity.motionX += BORDER_BOOST;
					}

					if (entity.posZ > current_pos.z + BORDERS) {
						entity.motionZ -= BORDER_BOOST;
					}

					if (entity.posZ < current_pos.z + (1D - BORDERS)) {
						entity.motionZ += BORDER_BOOST;
					}

					entity.motionZ = PC_MathHelper.clamp_float(
							(float)entity.motionZ,
							(float)-(BORDER_BOOST * 1.5D),
							(float)(BORDER_BOOST * 1.5D));
					entity.motionX = PC_MathHelper.clamp_float(
							(float)entity.motionX,
							(float)-(BORDER_BOOST * 1.5D),
							(float)(BORDER_BOOST * 1.5D));

				}

				break;

			case DOWN:

				if (Math.abs(entity.motionY) > 0.4D) {
					entity.motionY *= 0.3D;
				}

				entity.fallDistance = 0;

				if (bordersEnabled) {
					if (entity.posX > current_pos.x + (1D - BORDERS)) {
						entity.motionX -= BORDER_BOOST;
					}

					if (entity.posX < current_pos.x + BORDERS) {
						entity.motionX += BORDER_BOOST;
					}

					if (entity.posZ > current_pos.z + BORDERS) {
						entity.motionZ -= BORDER_BOOST;
					}

					if (entity.posZ < current_pos.z + (1D - BORDERS)) {
						entity.motionZ += BORDER_BOOST;
					}

					entity.motionZ = PC_MathHelper.clamp_float(
							(float)entity.motionZ,
							(float)-(BORDER_BOOST * 1.5D),
							(float)(BORDER_BOOST * 1.5D));
					entity.motionX = PC_MathHelper.clamp_float(
							(float)entity.motionX,
							(float)-(BORDER_BOOST * 1.5D),
							(float)(BORDER_BOOST * 1.5D));

				}
			default:
				break;
		}
	}
	
	public static boolean isOpaqueCube() {
	    return false;
	}

	public static boolean renderAsNormalBlock() {
	    return false;
	}

	public static AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
			    return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + HEIGHT_COLLISION + 0.0F), (k + 1));
			}

	public static AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i,
			int j, int k) {
			    float f = 0;
			    f = 0.0F + HEIGHT_SELECTED;
			    return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
			}

	public static void setBlockBoundsBasedOnState(Block b, IBlockAccess iblockaccess, int i,
			int j, int k) {
			    b.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + HEIGHT, 1.0F);
			}

	public static void setBlockBoundsForItemRender(Block b) {
	    b.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + HEIGHT, 1.0F);
	}

	public static int tickRate(World world) {
	    return 1;
	}
}
