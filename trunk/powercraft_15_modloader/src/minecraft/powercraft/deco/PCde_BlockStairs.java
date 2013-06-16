package powercraft.deco;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Stairs", tileEntity=PCde_TileEntityStairs.class, canPlacedRotated=true)
public class PCde_BlockStairs extends PC_Block {

	public PCde_BlockStairs(int id) {
		super(id, Material.rock, "ironplate", "fence");
		setHardness(1.5F);
		setResistance(30.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Get fences that are shown for stairs.
	 * 
	 * @param world world
	 * @param pos block pos
	 * @return bool{X+, X-, Z+, Z-}
	 */
	private boolean[] getFencesShownStairsAbsolute(World world, PC_VecI pos) {
		boolean fences[] = { false, false, false, false };

		PC_Direction dir = getRotation(PC_Utils.getMD(world, pos)).mirror();

		if (dir == PC_Direction.FRONT) {
			fences[0] = fences[1] = true;
		} else if (dir == PC_Direction.RIGHT) {
			fences[2] = fences[3] = true;
		} else if (dir == PC_Direction.BACK) {
			fences[0] = fences[1] = true;
		} else if (dir == PC_Direction.LEFT) {
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
	public boolean[] getFencesShownStairsRelative(World world, PC_VecI pos) {
		boolean fences[] = getFencesShownStairsAbsolute(world, pos);
		boolean rel[] = { false, false };

		PC_Direction dir = getRotation(PC_Utils.getMD(world, pos)).mirror();

		if (dir == PC_Direction.FRONT) {
			rel[0] = fences[0];
			rel[1] = fences[1];
		} else if (dir == PC_Direction.RIGHT) {
			rel[0] = fences[2];
			rel[1] = fences[3];
		} else if (dir == PC_Direction.BACK) {
			rel[0] = fences[1];
			rel[1] = fences[0];
		} else if (dir == PC_Direction.LEFT) {
			rel[0] = fences[3];
			rel[1] = fences[2];
		}

		return rel;
	}

	private static boolean isFallBlock(World world, PC_VecI pos) {
		int id = PC_Utils.getBID(world, pos);
		if (id == 0 || Block.blocksList[id] == null) {
			return true;
		}

		if (id == Block.ladder.blockID || id == Block.vine.blockID) {
			return false;
		}

		if (Block.blocksList[id].getCollisionBoundingBoxFromPool(world, pos.x, pos.y, pos.z) == null) {
			return true;
		}
		if (Block.blocksList[id].blockMaterial.isLiquid() || !Block.blocksList[id].blockMaterial.isSolid()) {
			return true;
		}
		if (PC_MSGRegistry.hasFlag(world, pos, "BELT")) {
			return true;
		}
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	public void addACollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity entity) {
		AxisAlignedBB axisalignedbb1 = super.getCollisionBoundingBoxFromPool(world, x, y, z);

        if (axisalignedbb1 != null && axisalignedbb.intersectsWith(axisalignedbb1))
        {
        	arraylist.add(axisalignedbb1);
        }
	}
	
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity entity) {

		PC_Direction dir = getRotation(PC_Utils.getMD(world, x, y, z)).mirror();

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		if (dir == PC_Direction.FRONT) {
			setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.RIGHT) {
			setBlockBounds(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.BACK) {
			setBlockBounds(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);

		} else if (dir == PC_Direction.LEFT) {
			setBlockBounds(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}

		// X+, X-, Z+, Z-
		boolean[] fences = getFencesShownStairsAbsolute(world, new PC_VecI(x, y, z));

		if (fences[0]) {
			setBlockBounds(1 - 0.0625F, 0, 0, 1, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[1]) {
			setBlockBounds(0, 0, 0, 0.0625F, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[2]) {
			setBlockBounds(0, 0, 1 - 0.0625F, 1, 1.8F, 1);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}
		if (fences[3]) {
			setBlockBounds(0, 0, 0, 1, 1.8F, 0.0625F);
			addACollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, entity);
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		float p = 0.0625F;

		setBlockBounds(0, 0F, 0.5F, 1, p, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[0]);

		setBlockBounds(0, 0.5F - p, 0, 1, 0.5F, 0.5F);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[0]);

		setBlockBounds(0, 0, 0.5F, p, 0.5F, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[1]);
		setBlockBounds(0, 0.5F, 0, p, 0.5F + 0.5F, 0.5F);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[1]);
		
		return true;
		
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}

	@Override
	public PC_VecI moveBlockTryToPlaceAt(World world, int x, int y, int z,
			PC_Direction dir, float xHit, float yHit, float zHit,
			ItemStack itemStack, EntityPlayer entityPlayer) {
		
		Item item = itemStack.getItem();
		if(item instanceof ItemBlock){
			Block block = Block.blocksList[((ItemBlock) item).getBlockID()];
			PC_Direction rot = getRotation(PC_Utils.getMD(world, x, y, z));
			PC_Direction pRot = PC_Direction.getFromPlayerDir(MathHelper.floor_double(((entityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3);
			PC_VecI offset = pRot.getOffset();
			if(rot==pRot){
				if(block==PCde_App.stairs || block==PCde_App.platform){
					offset.y++;
				}
			}else if(rot==pRot.mirror()){
				if(block==PCde_App.stairs && PC_KeyRegistry.isPlacingReversed(entityPlayer)){
					offset.y--;
				}
			}
			return offset;
		}
		return null;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		setBlockBounds(0, 0, 0, 1, 1.5f, 1);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	
}
