package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_Property;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.annotation.PC_Shining.OFF;
import powercraft.management.annotation.PC_Shining.ON;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.management.registry.PC_MSGRegistry;

@PC_Shining
@PC_BlockInfo(tileEntity=PClo_TileEntityPulsar.class)
public class PClo_BlockPulsar extends PC_Block implements PC_IItemInfo
{
    @ON
    public static PClo_BlockPulsar on;
    @OFF
    public static PClo_BlockPulsar off;

    public PClo_BlockPulsar(int id, boolean on)
    {
        super(id, 74, Material.wood, false);
        setHardness(0.8F);
        setResistance(30.0F);
        setRequiresSelfNotify();
        setStepSound(Block.soundWoodFootstep);

        if (on)
        {
            setCreativeTab(CreativeTabs.tabRedstone);
        }
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata)
    {
        return new PClo_TileEntityPulsar();
    }

    @Override
   	public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
       	return isActive(world, x, y, z);
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
	public void updateTick(World world, int x, int y, int z, Random par5Random) {
    	PClo_TileEntityPulsar tep = GameInfo.getTE(world, x, y, z);
    	if(tep.getShould() != tep.isActive()){
    		ValueWriting.setBlockState(world, x, y, z, tep.getShould());
    	}
	}

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID)
            {
                Block bhold = Block.blocksList[ihold.getItem().itemID];

                if (bhold instanceof PC_Block)
                {
                    return false;
                }
            }

            if (ihold.getItem().itemID == Item.stick.itemID)
            {
                changeDelay(world, player, i, j, k, player.isSneaking() ? -1 : 1);
                return true;
            }
        }

        if (world.isRemote)
        {
            PC_GresRegistry.openGres("Pulsar", player, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
        }

        return true;
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        printDelay(world, i, j, k);
    }

    public static void changeDelay(World world, EntityPlayer player, int x, int y, int z, int delay)
    {
        PClo_TileEntityPulsar ent = (PClo_TileEntityPulsar) world.getBlockTileEntity(x, y, z);
        ent.setTimes(delay, ent.getHold());
        ent.printDelay();
    }

    public static void printDelay(World world, int x, int y, int z)
    {
        PClo_TileEntityPulsar ent = (PClo_TileEntityPulsar) world.getBlockTileEntity(x, y, z);

        if (!world.isRemote)
        {
            ent.printDelayTime();
        }
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (isActive(world, i, j, k) && world.isRemote)
        {
            world.spawnParticle("reddust", i + 0.5D, j + 1.0D, k + 0.5D, 0D, 0D, 0D);
        }
    }

    public boolean isActive(IBlockAccess iblockaccess, int x, int y, int z)
    {
        TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);

        if (te == null || !(te instanceof PClo_TileEntityPulsar))
        {
            return false;
        }

        PClo_TileEntityPulsar tep = (PClo_TileEntityPulsar) te;
        return tep.isActive();
    }

    @Override
    public int getRenderColor(int i)
    {
        return 0xff3333;
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        if (isActive(iblockaccess, i, j, k))
        {
            return 0xff3333;
        }
        else
        {
            return 0x771111;
        }
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_LOAD_FROM_CONFIG:
			on.setLightValue(((PC_Property)obj[0]).getInt("brightness", 7) * 0.0625F);
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Redstone Pulsar";
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			if(this==off)
				return true;
			return false;
		}
		return null;
	}
}
