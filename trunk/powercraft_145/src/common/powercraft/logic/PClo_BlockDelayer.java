package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Block;
import powercraft.core.PC_VecI;
import powercraft.core.PC_IConfigLoader;
import powercraft.core.PC_IRotatedBox;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Shining;
import powercraft.core.PC_Utils;
import powercraft.core.PC_Shining.OFF;
import powercraft.core.PC_Shining.ON;

@PC_Shining
public class PClo_BlockDelayer extends PC_Block  implements PC_IRotatedBox, PC_IConfigLoader
{
    @ON
    public static PClo_BlockDelayer on;
    @OFF
    public static PClo_BlockDelayer off;

    public PClo_BlockDelayer(int id, boolean on)
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
    public int getRotation(int meta)
    {
        return getRotation_static(meta);
    }

    public static int getRotation_static(int meta)
    {
        return meta & 0x3;
    }

    @Override
    public boolean renderItemHorizontal()
    {
        return false;
    }

    @Override
    public String getDefaultName()
    {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PClo_TileEntityDelayer();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        PClo_TileEntityDelayer te = getTE(world, x, y, z);
        int rot = getRotation_static(PC_Utils.getMD(world, x, y, z));
        boolean stop = false;
        boolean reset = false;

        if (te.getType() == PClo_DelayerType.FIFO)
        {
            stop = PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.RIGHT, rot);
            reset = PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.LEFT, rot);
        }

        boolean data = PC_Utils.poweredFromInput(world, x, y, z, PC_Utils.BACK, rot);
        boolean[] stateBuffer = te.getStateBuffer();

        if (!stop && !reset)
        {
            boolean shouldState = stateBuffer[stateBuffer.length - 1];

            if (shouldState != isActive(world, x, y, z))
            {
                PC_Utils.setBlockState(world, x, y, z, shouldState);
            }

            for (int i = stateBuffer.length - 1; i > 0; i--)
            {
                stateBuffer[i] = stateBuffer[i - 1];
            }

            stateBuffer[0] = data;
            te.updateStateBuffer();
        }

        if (reset)
        {
            if (isActive(world, x, y, z))
            {
                PC_Utils.setBlockState(world, x, y, z, false);
            }

            for (int i = 0; i < stateBuffer.length; i++)
            {
                stateBuffer[i] = false;
            }

            te.updateStateBuffer();
        }
    }

    @Override
    public int tickRate()
    {
        return 1;
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int x, int y, int z, int side)
    {
        return isPoweringTo(world, x, y, z, side);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = PC_Utils.getMD(world, x, y, z);
        int rotation = getRotation(meta);

        if (!isActive(world, x, y, z))
        {
            return false;
        }

        if ((rotation == 0 && side == 3) || (rotation == 1 && side == 4) || (rotation == 2 && side == 2) || (rotation == 3 && side == 5))
        {
            return true;
        }

        return false;
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

    public static PClo_TileEntityDelayer getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);;

        if (te instanceof PClo_TileEntityDelayer)
        {
            return (PClo_TileEntityDelayer)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityDelayer te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getType();
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
            return getTopFaceFromEnum(getType(iblockaccess, x, y, z)) + (isActive(iblockaccess, x, y, z) ? 16 : 0);
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
            return getTopFaceFromEnum(meta) + 16;
        }
        else
        {
            return 5;
        }
    }

    private int getTopFaceFromEnum(int meta)
    {
        if (meta >= 0 && meta < PClo_DelayerType.TOTAL_DELAYER_COUNT)
        {
            return PClo_DelayerType.index[meta];
        }

        return 6;
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

        if (entityliving instanceof EntityPlayer)
        {
            PC_Utils.openGres("Delayer", (EntityPlayer)entityliving, x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        PC_Utils.openGres("Delayer", player, x, y, z);
        return true;
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
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.delayer, 1, type));
        }

        return remove;
    }

    @Override
    public void loadFromConfig(Configuration config)
    {
        on.setLightValue(PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "GatesLightValueOn", 7) / 16.0f);
    }
    
    @Override
	public List<String> getBlockFlags(World world, PC_VecI pos, List<String> list) {

		list.add(PC_Utils.NO_HARVEST);
		list.add(PC_Utils.NO_PICKUP);
		return list;
	}

	@Override
	public List<String> getItemFlags(ItemStack stack, List<String> list) {
		list.add(PC_Utils.NO_BUILD);
		return list;
	}
    
}
