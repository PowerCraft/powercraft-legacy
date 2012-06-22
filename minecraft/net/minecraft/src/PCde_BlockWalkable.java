package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

/**
 * Decorative block, non solid;<br>
 * Subtypes: iron ledge
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_BlockWalkable extends BlockContainer implements PC_IBlockType, PC_ISwapTerrain, ITextureProvider {

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public TileEntity getBlockEntity() {
		return new PCde_TileEntityWalkable();
	}

	@Override
	public String getTerrainFile() {
		return mod_PCdeco.getTerrainFile();
	}

	/**
	 * Decorative block;
	 * 
	 * @param i id
	 * @param j texture index
	 * @param material block material
	 */
	public PCde_BlockWalkable(int i, int j, Material material) {
		super(i, j, material);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if (meta == 0) return 22;
		return 0;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
		// colors particles
		PCde_TileEntityWalkable ted = getTE(iblockaccess, x, y, z);
		if (ted.type == 0) return 0xffffff;
		return 0xffffff;

	}

	@Override
	public int getRenderColor(int i) {
		if (i == 0) return 0xcccccc;
		return 0xffffff;
	}

	/**
	 * Get fences that are shown.
	 * 
	 * @param world world
	 * @param pos block pos
	 * @return bool{X+, X-, Z+, Z-}
	 */
	public static boolean[] getFencesShownLedge(World world, PC_CoordI pos) {
		boolean sides[] = { false, false, false, false, true };
		sides[0] = isFallBlock(world, pos.offset(1, 0, 0)) && isFallBlock(world, pos.offset(1, -1, 0));
		sides[1] = isFallBlock(world, pos.offset(-1, 0, 0)) && isFallBlock(world, pos.offset(-1, -1, 0));
		sides[2] = isFallBlock(world, pos.offset(0, 0, 1)) && isFallBlock(world, pos.offset(0, -1, 1));
		sides[3] = isFallBlock(world, pos.offset(0, 0, -1)) && isFallBlock(world, pos.offset(0, -1, -1));
		sides[4] = !isClimbBlock(world, pos.offset(0, -1, 0));
		return sides;
	}

	/**
	 * Get fences that are shown for stairs.
	 * 
	 * @param world world
	 * @param pos block pos
	 * @return bool{X+, X-, Z+, Z-}
	 */
	public static boolean[] getFencesShownStairsAbsolute(World world, PC_CoordI pos) {
		boolean fences[] = { false, false, false, false };

		int j = pos.getMeta(world);

		if (j == 0) {
			fences[0] = fences[1] = true;
		} else if (j == 1) {
			fences[2] = fences[3] = true;
		} else if (j == 2) {
			fences[0] = fences[1] = true;
		} else if (j == 3) {
			fences[2] = fences[3] = true;
		}

		fences[0] &= isFallBlock(world, pos.offset(1, 0, 0)) && isFallBlock(world, pos.offset(1, -1, 0));
		fences[1] &= isFallBlock(world, pos.offset(-1, 0, 0)) && isFallBlock(world, pos.offset(-1, -1, 0));
		fences[2] &= isFallBlock(world, pos.offset(0, 0, 1)) && isFallBlock(world, pos.offset(0, -1, 1));
		fences[3] &= isFallBlock(world, pos.offset(0, 0, -1)) && isFallBlock(world, pos.offset(0, -1, -1));
		return fences;
	}

	/**
	 * Get which stair sides should be shown (left,right)
	 * 
	 * @param world
	 * @param pos
	 * @return left, right
	 */
	public static boolean[] getFencesShownStairsRelative(World world, PC_CoordI pos) {
		boolean fences[] = getFencesShownStairsAbsolute(world, pos);
		boolean rel[] = { false, false };

		int j = pos.getMeta(world);

		if (j == 0) {
			rel[0] = fences[0];
			rel[1] = fences[1];
		} else if (j == 1) {
			rel[0] = fences[2];
			rel[1] = fences[3];
		} else if (j == 2) {
			rel[0] = fences[1];
			rel[1] = fences[0];
		} else if (j == 3) {
			rel[0] = fences[3];
			rel[1] = fences[2];
		}

		return rel;
	}

	private static boolean isFallBlock(World world, PC_CoordI pos) {
		int id = pos.getId(world);
		if (id == 0 || Block.blocksList[id] == null) return true;

		if (id == Block.ladder.blockID || id == Block.vine.blockID) return false;

		if (Block.blocksList[id].getCollisionBoundingBoxFromPool(world, pos.x, pos.y, pos.z) == null) return true;
		if (Block.blocksList[id].blockMaterial.isLiquid() || !Block.blocksList[id].blockMaterial.isSolid()) return true;
		if (PC_BlockUtils.getBlockFlags(world, pos).contains("BELT")) return true;
		return false;
	}

	private static boolean isClimbBlock(World world, PC_CoordI pos) {
		int id = pos.getId(world);
		if (id == 0 || Block.blocksList[id] == null) return false;

		if (id == Block.ladder.blockID || id == Block.vine.blockID) return true;
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		PCde_TileEntityWalkable ted = getTE(iblockaccess, i, j, k);

		// ledge
		if (ted.type == 0) {
			setBlockBounds(0, 0, 0, 1, 0.0625F, 1);
			return;
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);


	}

	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, ArrayList arraylist) {

		PCde_TileEntityWalkable ted = getTE(world, x, y, z);

		if (ted.type == 0) {

			boolean[] fences = getFencesShownLedge(world, new PC_CoordI(x, y, z));

			if (fences[0]) {
				setBlockBounds(1 - 0.0625F, 0, 0, 1, 1.3F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[1]) {
				setBlockBounds(0, 0, 0, 0.0625F, 1.3F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[2]) {
				setBlockBounds(0, 0, 1 - 0.0625F, 1, 1.3F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[3]) {
				setBlockBounds(0, 0, 0, 1, 1.3F, 0.0625F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[4]) {
				setBlockBounds(0, 0, 0, 1, 0.0625F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}

			setBlockBounds(0, 0, 0, 1, 1, 1);
			return;


		} else if (ted.type == 1) {

			int j = world.getBlockMetadata(x, y, z);

			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);



			if (j == 0) {
				setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);

			} else if (j == 1) {
				setBlockBounds(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);

			} else if (j == 2) {
				setBlockBounds(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);

			} else if (j == 3) {
				setBlockBounds(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}

			// X+, X-, Z+, Z-
			boolean[] fences = getFencesShownStairsAbsolute(world, new PC_CoordI(x, y, z));

			if (fences[0]) {
				setBlockBounds(1 - 0.0625F, 0, 0, 1, 1.8F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[1]) {
				setBlockBounds(0, 0, 0, 0.0625F, 1.8F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[2]) {
				setBlockBounds(0, 0, 1 - 0.0625F, 1, 1.8F, 1);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}
			if (fences[3]) {
				setBlockBounds(0, 0, 0, 1, 1.8F, 0.0625F);
				super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
			}

			setBlockBounds(0, 0, 0, 1, 1, 1);
			return;
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);
		super.getCollidingBoundingBoxes(world, x, y, z, axisalignedbb, arraylist);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
		PCde_TileEntityWalkable tew = getTE(world, x, y, z);

		if (tew.type == 1) {
			int dir = ((MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
			if (PC_Utils.isPlacingReversed()) dir = PC_Utils.reverseSide(dir);
			world.setBlockMetadataWithNotify(x, y, z, dir);
		}

	}

	/**
	 * Get tile entity at position
	 * 
	 * @param iblockaccess block access
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity or null
	 */
	public PCde_TileEntityWalkable getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) { return null; }
		return (PCde_TileEntityWalkable) te;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		// in world - block
		PCde_TileEntityWalkable ted = getTE(iblockaccess, x, y, z);
		if (ted.type == 0) return 22;
		return 0;

	}

	@Override
	protected int damageDropped(int i) {
		return 0;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return -1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (iblockaccess.getBlockMetadata(i, j, k) == 0) { return true; }
		return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {

		PCde_TileEntityWalkable teg = getTE(world, x, y, z);

		if (teg != null) {

			if (!PC_Utils.isCreative()) {
				dropBlockAsItem_do(world, x, y, z, new ItemStack(mod_PCdeco.walkable, 1, teg.type));
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public int getRenderBlockPass() {
		return 0;// solid
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
		return PCde_Renderer.walkableBlockRenderer;
	}


	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {
		// NO_HARVEST, HARVEST_STOP, TRANSLUCENT

		PCde_TileEntityWalkable tew = getTE(world, pos.x, pos.y, pos.z);

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("IRON_LEDGE");
		set.add("DECORATIVE");
		set.add("PASSIVE");
		
		if (tew != null && tew.type != 1) set.add("TRANSLUCENT");

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}

}
