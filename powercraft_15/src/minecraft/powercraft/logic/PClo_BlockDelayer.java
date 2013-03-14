package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_MathHelper;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(itemBlock=PClo_ItemBlockDelayer.class, tileEntity=PClo_TileEntityDelayer.class)
public class PClo_BlockDelayer extends PC_Block
{
    @ON
    public static PClo_BlockDelayer on;
    @OFF
    public static PClo_BlockDelayer off;

    public PClo_BlockDelayer(int id, boolean on)
    {
        super(id, Material.ground, "bottomplate", PClo_DelayerType.getTextures());
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
    
    public static int getRotation_static(int meta)
    {
        return meta & 0x3;
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PClo_TileEntityDelayer();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        PClo_TileEntityDelayer te = getTE(world, x, y, z);
        int rot = getRotation_static(GameInfo.getMD(world, x, y, z));
        boolean data = GameInfo.poweredFromInput(world, x, y, z, PC_Utils.BACK, rot);
        
        switch(te.getType()){
        case PClo_DelayerType.FIFO:
        	boolean stop = GameInfo.poweredFromInput(world, x, y, z, PC_Utils.RIGHT, rot);
        	boolean reset = GameInfo.poweredFromInput(world, x, y, z, PC_Utils.LEFT, rot);
       		boolean[] stateBuffer = te.getStateBuffer();

	        if (!stop && !reset)
	        {
	            boolean shouldState = stateBuffer[stateBuffer.length - 1];
	
	            if (shouldState != isActive(world, x, y, z))
	            {
	                ValueWriting.setBlockState(world, x, y, z, shouldState);
	            }
	
	            for (int i = stateBuffer.length - 1; i > 0; i--)
	            {
	                stateBuffer[i] = stateBuffer[i - 1];
	            }
	
	            stateBuffer[0] = data;
	        }
	
	        if (reset)
	        {
	            if (isActive(world, x, y, z))
	            {
	                ValueWriting.setBlockState(world, x, y, z, false);
	            }
	
	            for (int i = 0; i < stateBuffer.length; i++)
	            {
	                stateBuffer[i] = false;
	            }
	        }
	        break;
        case PClo_DelayerType.HOLD:
        	
        	boolean should = true;
        	
        	if(te.decRemainingTicks()){
        		should = false;
        	}
        	
        	if(data){
        		te.resetRemainingTicks();
        		should = true;
        	}
        	
        	if (isActive(world, x, y, z)!=should){
        		ValueWriting.setBlockState(world, x, y, z, should);
        	}
        	
        }
    }

    @Override
    public int tickRate(World par1World)
    {
        return 1;
    }

    @Override
   	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
    	int meta = GameInfo.getMD(world, x, y, z);
        int rotation = getRotation_static(meta);

        if (!isActive(world, x, y, z))
        {
            return 0;
        }

        if ((rotation == 0 && s == 3) || (rotation == 1 && s == 4) || (rotation == 2 && s == 2) || (rotation == 3 && s == 5))
        {
            return 15;
        }

        return 0;
   	}

   	@Override
   	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int s) {
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

    public static PClo_TileEntityDelayer getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = GameInfo.getTE(world, x, y, z);;

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
        return GameInfo.getBID(world, x, y, z) == on.blockID;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side)
    {
        if (side == 1)
        {
            return icons[getType(iblockaccess, x, y, z)+2+(isActive(iblockaccess, x, y, z) ? 0 : PClo_DelayerType.TOTAL_DELAYER_COUNT)];
        }

        if (side == 0)
        {
            return icons[0];
        }

        return icons[1];
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        if (side == 0)
        {
            return icons[0];
        }

        if (side == 1)
        {
            return icons[meta+2];
        }
        else
        {
            return icons[1];
        }
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itmeStack)
    {
        int type = getType(world, x, y, z);
        int l = ((PC_MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

        if (entityliving instanceof EntityPlayer && PC_KeyRegistry.isPlacingReversed(((EntityPlayer)entityliving)))
        {
            l = ValueWriting.reverseSide(l);
        }
        
        ValueWriting.setMD(world, x, y, z, l);
        onNeighborBlockChange(world, x, y, z, 0);

        if (entityliving instanceof EntityPlayer)
        {
            PC_GresRegistry.openGres("Delayer", (EntityPlayer)entityliving, GameInfo.<PC_TileEntity>getTE(world, x, y, z));
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        PC_GresRegistry.openGres("Delayer", player, GameInfo.<PC_TileEntity>getTE(world, x, y, z));
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

        if (remove && !GameInfo.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.delayer, 1, type));
        }

        return remove;
    }

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_LOAD_FROM_CONFIG:
			on.setLightValue(((PC_Property)obj[0]).getInt("brightness", 15) * 0.0625F);
			break;
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_RENDER_ITEM_HORIZONTAL:
			return false;
		case PC_MSGRegistry.MSG_ROTATION:
			return getRotation_static((Integer)obj[0]);
		default:
			return null;
		}
		return true;
	}
    
}
