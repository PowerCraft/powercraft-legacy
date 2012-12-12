package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Property;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Shining;
import powercraft.management.PC_Shining.OFF;
import powercraft.management.PC_Shining.ON;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

@PC_Shining
public class PClo_BlockRepeater extends PC_Block
{
    @ON
    public static PClo_BlockRepeater on;
    @OFF
    public static PClo_BlockRepeater off;

    public PClo_BlockRepeater(int id, boolean on)
    {
        super(id, 6, Material.ground);
        setHardness(0.35F);
        setStepSound(Block.soundWoodFootstep);
        disableStats();
        setRequiresSelfNotify();
        setResistance(30.0F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);

        if (on)
        {
            setCreativeTab(CreativeTabs.tabRedstone);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PClo_TileEntityRepeater();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        int rot = getRotation_static(PC_Utils.getMD(world, x, y, z));
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te.getType() == PClo_RepeaterType.CROSSING)
        {
            int[] inp = { PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.LEFT) ? 1 : 0, PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.BACK) ? 1 : 0,
                    PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.RIGHT) ? 1 : 0, PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.FRONT) ? 1 : 0
                        };
            int variant = te.getInp();
            int shouldState = 0;

            switch (variant)
            {
                case 0:
                    shouldState = inp[2] | inp[3] << 1;
                    break;

                case 1:
                    shouldState = inp[3] | inp[0] << 1;
                    break;

                case 2:
                    shouldState = inp[1] | inp[2] << 1;
                    break;

                case 3:
                    shouldState = inp[0] | inp[1] << 1;
                    break;
            }

            if (te.getState() != shouldState)
            {
                te.setState(shouldState);
                PC_Utils.setBlockState(world, x, y, z, shouldState != 0);
            }
        }
        else
        {
            boolean shouldState = PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.BACK, rot);

            if (isActive(world, x, y, z) != shouldState)
            {
                PC_Utils.setBlockState(world, x, y, z, shouldState);
            }
        }
    }

    @Override
    public int tickRate()
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int side)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te.getType() == PClo_RepeaterType.REPEATER_STRAIGHT_I || te.getType() == PClo_RepeaterType.REPEATER_CORNER_I)
        {
            updateTick(world, x, y, z, new Random());
        }
        else
        {
            world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
        }
    }

    @Override
   	public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
    	int meta = PC_Utils.getMD(world, x, y, z);
        int rotation = getRotation_static(meta);
        PClo_TileEntityRepeater te = getTE(world, x, y, z);
        int type = te.getType();
        boolean L = false, R = false, F = false, B = false;
        int variant = te.getInp();

        if (type == PClo_RepeaterType.CROSSING)
        {
            int state = te.getState();
            boolean power1 = (state & 1) != 0 ;
            boolean power2 = (state & 2) != 0 ;

            switch (variant)
            {
                case 0:
                    if (s == 5)
                    {
                        return power1;
                    }

                    if (s == 2)
                    {
                        return power2;
                    }

                    break;

                case 1:
                    if (s == 2)
                    {
                        return power1;
                    }

                    if (s == 4)
                    {
                        return power2;
                    }

                    break;

                case 2:
                    if (s == 3)
                    {
                        return power1;
                    }

                    if (s == 5)
                    {
                        return power2;
                    }

                    break;

                case 3:
                    if (s == 4)
                    {
                        return power1;
                    }

                    if (s == 3)
                    {
                        return power2;
                    }

                    break;
            }

            return false;
        }

        boolean power = isActive(world, x, y, z);

        if (!power)
        {
            return false;
        }

        if (type == PClo_RepeaterType.SPLITTER_I)
        {
            L = variant != 3;
            R = variant != 1;
            F = variant != 2;
            B = false;
        }
        else if (type == PClo_RepeaterType.REPEATER_STRAIGHT_I || type == PClo_RepeaterType.REPEATER_STRAIGHT)
        {
            F = true;
        }
        else if (type == PClo_RepeaterType.REPEATER_CORNER_I || type == PClo_RepeaterType.REPEATER_CORNER)
        {
            L = variant == 0;
            R = variant == 1;
        }

        switch (rotation)
        {
            case 0:
                switch (s)
                {
                    case 3:
                        return F;

                    case 4:
                        return R;

                    case 2:
                        return B;

                    case 5:
                        return L;
                }

                break;

            case 1:
                switch (s)
                {
                    case 3:
                        return L;

                    case 4:
                        return F;

                    case 2:
                        return R;

                    case 5:
                        return B;
                }

                break;

            case 2:
                switch (s)
                {
                    case 3:
                        return B;

                    case 4:
                        return L;

                    case 2:
                        return F;

                    case 5:
                        return R;
                }

                break;

            case 3:
                switch (s)
                {
                    case 3:
                        return R;

                    case 4:
                        return B;

                    case 2:
                        return L;

                    case 5:
                        return F;
                }

                break;
        }

        return false;
   	}

   	@Override
   	public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int s) {
   		return isProvidingWeakPower(world, x, y, z, s);
   	}

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return PC_Renderer.getRendererID(true);
    }

    public static PClo_TileEntityRepeater getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);

        if (te instanceof PClo_TileEntityRepeater)
        {
            return (PClo_TileEntityRepeater)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getType();
        }

        return 0;
    }

    public static int getInp(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getInp();
        }

        return 0;
    }

    public static boolean isActive(IBlockAccess world, int x, int y, int z)
    {
        return PC_Utils.getBID(world, x, y, z) == on.blockID;
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side)
    {
        if (side == 1)
        {
            int meta = getType(iblockaccess, x, y, z);
            return getTopFaceFromEnum(meta, getInp(iblockaccess, x, y, z)) + (PClo_RepeaterType.canBeOn[meta] && isActive(iblockaccess, x, y, z) ? 16 : 0);
        }

        if (side == 0)
        {
            return 6;
        }

        return 5;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        if (side == 0)
        {
            return 6;
        }

        if (side == 1)
        {
            return getTopFaceFromEnum(meta, 0) + (PClo_RepeaterType.canBeOn[meta] ? 16 : 0);
        }
        else
        {
            return 5;
        }
    }

    private int getTopFaceFromEnum(int meta, int rotation)
    {
        if (meta >= 0 && meta < PClo_RepeaterType.TOTAL_REPEATER_COUNT)
        {
            return PClo_RepeaterType.index[meta] + rotation;
        }

        return 6;
    }

    public static int getRotation_static(int meta)
    {
        return meta & 0x3;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving)
    {
        int type = getType(world, x, y, z);
        int l = ((PC_MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

        if (entityliving instanceof EntityPlayer && PC_Utils.isPlacingReversed(((EntityPlayer)entityliving)))
        {
            l = PC_Utils.reverseSide(l);
        }

        world.setBlockMetadataWithNotify(x, y, z, l);
        onNeighborBlockChange(world, x, y, z, 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem().shiftedIndex == Item.stick.shiftedIndex)
            {
                if (!world.isRemote)
                {
                    getTE(world, x, y, z).change();
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (!isActive(world, x, y, z))
        {
            return;
        }

        if (random.nextInt(3) != 0)
        {
            return;
        }

        double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        double d1 = (y + 0.2F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        world.spawnParticle("reddust", d, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return -1;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        int type = getType(world, x, y, z);
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !PC_Utils.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.repeater, 1, type));
        }

        return remove;
    }

	@Override
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_LOAD_FROM_CONFIG:
			on.setLightValue(((PC_Property)obj[0]).getInt("brightness", 15) * 0.0625F);
			break;
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Light";
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_RENDER_ITEM_HORIZONTAL:
			return false;
		case PC_Utils.MSG_ROTATION:
			return getRotation_static((Integer)obj[0]);
		default:
			return null;
		}
		return true;
	}
}
