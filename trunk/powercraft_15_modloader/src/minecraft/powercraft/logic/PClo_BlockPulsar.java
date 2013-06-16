package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="Redstone Pulsar", tileEntity=PClo_TileEntityPulsar.class)
public class PClo_BlockPulsar extends PC_Block implements PC_IItemInfo
{
    @ON
    public static PClo_BlockPulsar on;
    @OFF
    public static PClo_BlockPulsar off;

    public PClo_BlockPulsar(int id, boolean on)
    {
        super(id, Material.wood, "pulsar");
        setHardness(0.8F);
        setResistance(30.0F);
        setStepSound(Block.soundWoodFootstep);

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
	public boolean showInCraftingTool() {
    	if(this==on)
			return true;
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

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {

            if (ihold.getItem().itemID == Item.stick.itemID)
            {
                changeDelay(world, player, i, j, k, player.isSneaking() ? -1 : 1);
                return true;
            }
        }

        if (world.isRemote)
        {
            PC_GresRegistry.openGres("Pulsar", player, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
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
        return PC_Utils.getBID(iblockaccess, x, y, z) == PClo_BlockPulsar.on.blockID;
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        if (isActive(iblockaccess, i, j, k))
        {
            return 0xffffff;
        }
        else
        {
            return 0x777777;
        }
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

	@Override
	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
	}

	@Override
	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return isActive(world, x, y, z)?15:0;
	}
    
}
