package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="Gate", itemBlock=PClo_ItemBlockGate.class, tileEntity=PClo_TileEntityGate.class, canPlacedRotated=true)
public class PClo_BlockGate extends PC_Block
{
    @ON
    public static PClo_BlockGate on;
    @OFF
    public static PClo_BlockGate off;

    public PClo_BlockGate(int id, boolean on)
    {
        super(id, Material.ground, PClo_GateType.getTextures());
        setHardness(0.35F);
        setStepSound(Block.soundWoodFootstep);
        disableStats();
        setResistance(30.0F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);

        if (on)
        {
            setCreativeTab(CreativeTabs.tabRedstone);
        }
    }
    
    @Override
	public void initConfig(PC_Property config) {
		super.initConfig(config);
		on.setLightValue(config.getInt("brightness", 7) * 0.0625F);
	}

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        boolean on = isActive(world, x, y, z);
        boolean outputActive = isOutputActive(world, x, y, z);

        if (on != outputActive)
        {
            PC_Utils.setBlockState(world, x, y, z, outputActive);
        }
    }

    @Override
    public int tickRate(World world)
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int side)
    {
        boolean on = isActive(world, x, y, z);
        boolean outputActive = isOutputActive(world, x, y, z);

        if (on != outputActive)
        {
            world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
        }
    }

    private boolean isOutputActive(World world, int x, int y, int z)
    {
        int rot = getRotation_static(PC_Utils.getMD(world, x, y, z));
        return PClo_GateType.getGateOutput(getType(world, x, y, z), 
        		getInp(world, x, y, z), 
        		getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.LEFT)>0,
        		getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.BACK)>0,
        		getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.RIGHT)>0);
    }
    
    @Override
   	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
   		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
   	}

   	@Override
   	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
       if (isActive(world, x, y, z) && dir==PC_Direction.FRONT){
           return 15;
       }

       return 0;
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

    public static PClo_TileEntityGate getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);;

        if (te instanceof PClo_TileEntityGate)
        {
            return (PClo_TileEntityGate)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityGate te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getType();
        }

        return 0;
    }

    public static int getInp(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityGate te = getTE(world, x, y, z);

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
    public Icon getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, PC_Direction side)
    {
        if (side == PC_Direction.TOP)
        {
        	int i = getType(iblockaccess, x, y, z);
        	if(i!=0){
        		i*=4;
        		i-=3;
        		i+=2;
        	}
        	if(!isActive(iblockaccess, x, y, z)){
        		i+=PClo_GateType.TOTAL_GATE_COUNT*4-3;
        	}
        	i += getInp(iblockaccess, x, y, z);
            return sideIcons[i];
        }

        if (side == PC_Direction.BOTTOM)
        {
            return sideIcons[0];
        }

        return sideIcons[1];
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(PC_Direction side, int meta)
    {
        if (side == PC_Direction.BOTTOM)
        {
            return sideIcons[0];
        }

        if (side == PC_Direction.TOP)
        {
        	if(meta!=0){
        		meta*=4;
        		meta-=3;
        	}
            return sideIcons[meta+2];
        }
        else
        {
            return sideIcons[1];
        }
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem().itemID == Item.stick.itemID)
            {
                if (!world.isRemote)
                {
                    getTE(world, x, y, z).rotInp();
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
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.gate, 1, type));
        }

        return remove;
    }
   	
}
