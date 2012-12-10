package powercraft.logic;

import java.util.List;
import java.util.Random;

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
import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Block;
import powercraft.core.PC_VecI;
import powercraft.core.PC_IConfigLoader;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Shining;
import powercraft.core.PC_Utils;
import powercraft.core.PC_Shining.OFF;
import powercraft.core.PC_Shining.ON;
import powercraft.light.PCli_BlockLight;

@PC_Shining
public class PClo_BlockPulsar extends PC_Block implements PC_ICraftingToolDisplayer, PC_IConfigLoader
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
    public String getDefaultName()
    {
        return "Redstone Pulsar";
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PClo_TileEntityPulsar();
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return isActive(iblockaccess, i, j, k);
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return isActive(world, i, j, k);
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
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        world.markBlockAsNeedsUpdate(i, j, k);
        PC_Utils.hugeUpdate(world, i, j, k, blockID);
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
            PC_Utils.openGres("Pulsar", player, i, j, k);
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
    public String getCraftingToolModule()
    {
        return PClo_App.getInstance().getNameWithoutPowerCraft();
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
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
   		list.add(PC_Utils.HARVEST_STOP);
   		return list;
   	}

   	@Override
   	public List<String> getItemFlags(ItemStack stack, List<String> list) {
   		list.add(PC_Utils.NO_BUILD);
   		return list;
   	}
}
