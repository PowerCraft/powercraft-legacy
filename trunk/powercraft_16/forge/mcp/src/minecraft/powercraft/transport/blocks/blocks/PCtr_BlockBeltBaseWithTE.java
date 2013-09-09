package powercraft.transport.blocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_Block;
import powercraft.transport.helper.PCtr_BeltHelper;

public abstract class PCtr_BlockBeltBaseWithTE extends PC_Block
{
	private boolean isElevator = false;
    public PCtr_BlockBeltBaseWithTE(int id, Material material, boolean b_isElevator)
    {
        super(id, material);
        isElevator = b_isElevator;
        if (isElevator) setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltHelper.ELEVATOR_HEIGHT, 1.0F);
        else
        	setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltHelper.HEIGHT, 1.0F);
        setHardness(0.22F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public abstract void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity);
    
    @Override	
	public boolean isOpaqueCube()
	{
		return PCtr_BeltHelper.isOpaqueCube();
	}

    
	@Override
	public boolean renderAsNormalBlock()
	{
		return PCtr_BeltHelper.renderAsNormalBlock();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k, isElevator);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return PCtr_BeltHelper.getSelectedBoundingBoxFromPool(world, i, j, k, isElevator);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
	{
		PCtr_BeltHelper.setBlockBoundsBasedOnState(this, iblockaccess, i, j, k, isElevator);
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		PCtr_BeltHelper.setBlockBoundsForItemRender(this, isElevator);
	}

	@Override
	public int tickRate(World world)
	{
		return PCtr_BeltHelper.tickRate(world);
	}
/*
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        //return PCtr_BeltHelper.blockActivated(world, i, j, k, entityplayer);
    	return true;
    }
    */
/*
	@Override
	public PC_Vec3I moveBlockTryToPlaceAt(World world, int x, int y, int z,
			PC_Direction dir, float xHit, float yHit, float zHit,
			ItemStack itemStack, EntityPlayer entityPlayer) {
		PC_Direction pDir = PC_Direction.getFromPlayerDir(PC_MathHelper.floor_double(((entityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3);
		return pDir.getOffset();
	}
 */   
}
