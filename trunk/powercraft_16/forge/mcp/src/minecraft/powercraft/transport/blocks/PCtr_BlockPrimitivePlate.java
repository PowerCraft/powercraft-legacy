package powercraft.transport.blocks;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.minertnt.PC_TileEntityMinerTNT;

@PC_BlockInfo(name = "PrimitivePlate", blockid = "primitiveplate", defaultid = 2050, tileEntity = PC_TileEntityPrimitivePlate.class)
public class PCtr_BlockPrimitivePlate extends PC_Block
{
	protected AxisAlignedBB boundingbox;

	public PCtr_BlockPrimitivePlate(int id)
	{
		super(id, Material.rock);
		boundingbox.maxX = this.maxX;
		boundingbox.maxZ = this.maxZ;
		boundingbox.maxY = this.maxY + 16;
		boundingbox.minX = this.minX;
		boundingbox.minZ = this.minZ;
		boundingbox.minY = this.minY + 16;
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("primitiveplate");

	}

	@Override
	public void registerRecipes()
	{

	}

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity){
    	double mX = entity.motionX;
    	double mY = entity.motionY;
    	double mZ = entity.motionZ;
    	boolean moveX=false;
    	if(Math.abs(mX)>Math.abs(mZ)){
    		entity.motionZ+=k-mZ;
    	}else{
    		entity.motionX+=i-mX;
    	}	
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + PCtr_BeltHelper.HEIGHT_COLLISION + 0.0F), (k + 1));
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        float f = 0;
        f = 0.0F + PCtr_BeltHelper.HEIGHT_SELECTED;
        return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + PCtr_BeltHelper.HEIGHT, 1.0F);
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + PCtr_BeltHelper.HEIGHT, 1.0F);
    }

    @Override
    public int tickRate(World world)
    {
        return 1;
    }
}
