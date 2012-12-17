package powercraft.logic;

import java.util.List;
import java.util.Random;

import javax.lang.model.element.VariableElement;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Shining;
import powercraft.management.PC_Shining.OFF;
import powercraft.management.PC_Shining.ON;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

@PC_Shining
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
            if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID)
            {
                Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];

                if (bhold instanceof PC_Block)
                {
                    return false;
                }
            }

            if (ihold.getItem().shiftedIndex == Item.stick.shiftedIndex)
            {
                changeDelay(world, player, i, j, k, player.isSneaking() ? -1 : 1);
                return true;
            }
        }

        if (world.isRemote)
        {
            Gres.openGres("Pulsar", player, i, j, k);
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
        PC_PacketHandler.setTileEntity(ent, "changeDelay", delay);
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
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_LOAD_FROM_CONFIG:
			on.setLightValue(((PC_Property)obj[0]).getInt("brightness", 7) * 0.0625F);
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Redstone Pulsar";
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		}
		return null;
	}
}
