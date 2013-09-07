package powercraft.transport.blocks;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockRotated;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.core.blocks.PC_BlockRoaster;
import powercraft.minertnt.PC_TileEntityMinerTNT;

@PC_BlockInfo(name = "PrimitivePlate", blockid = "primitivePlate", defaultid = 2050)
public class PCtr_BlockPrimitivePlate extends PC_BlockRotated
{
	protected AxisAlignedBB boundingbox;

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
	
	public PCtr_BlockPrimitivePlate(int id)
	{
		super(id, Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
		boundingbox.maxX = this.maxX;
		boundingbox.maxZ = this.maxZ;
		boundingbox.maxY = this.maxY + 16;
		boundingbox.minX = this.minX;
		boundingbox.minZ = this.minZ;
		boundingbox.minY = this.minY + 16;
		System.out.println("Finished Constructor");
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("primitiveplate");
		System.out.println("LoadedIcon");
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
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + HEIGHT_COLLISION + 0.0F), (k + 1));
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        float f = 0;
        f = 0.0F + HEIGHT_SELECTED;
        return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + HEIGHT, 1.0F);
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + HEIGHT, 1.0F);
    }

    @Override
    public int tickRate(World world)
    {
        return 1;
    }
}
