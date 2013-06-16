package powercraft.transport;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.api.block.PC_Block;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_VecI;

public abstract class PCtr_BlockBeltBase extends PC_Block
{
    public PCtr_BlockBeltBase(int id, String texture)
    {
        super(id, PCtr_MaterialConveyor.getMaterial(), "belt_down", texture, "belt_side");
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

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        return PCtr_BeltHelper.blockActivated(world, i, j, k, entityplayer);
    }

	@Override
	public PC_VecI moveBlockTryToPlaceAt(World world, int x, int y, int z,
			PC_Direction dir, float xHit, float yHit, float zHit,
			ItemStack itemStack, EntityPlayer entityPlayer) {
		PC_Direction pDir = PC_Direction.getFromPlayerDir(PC_MathHelper.floor_double(((entityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3);
		return pDir.getOffset();
	}
    
}
