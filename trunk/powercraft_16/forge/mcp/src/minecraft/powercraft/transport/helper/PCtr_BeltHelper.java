package powercraft.transport.helper;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
