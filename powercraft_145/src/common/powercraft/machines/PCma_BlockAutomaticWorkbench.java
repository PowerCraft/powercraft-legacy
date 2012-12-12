package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_ISpecialInventoryTextures;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCma_BlockAutomaticWorkbench extends PC_Block implements PC_ISpecialInventoryTextures, PC_IItemInfo
{
    private static final int TXDOWN = 109, TXTOP = 154, TXSIDE = 138, TXFRONT = 106, TXBACK = 122;

    public PCma_BlockAutomaticWorkbench(int id)
    {
        super(id, 62, Material.ground);
        setHardness(0.7F);
        setResistance(10.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int par5, int par6)
    {
        PCma_TileEntityAutomaticWorkbench tew = (PCma_TileEntityAutomaticWorkbench) world.getBlockTileEntity(i, j, k);

        if (tew != null)
        {
            PC_InvUtils.dropInventoryContents(tew, world, tew.getCoord());
        }

        super.breakBlock(world, i, j, k, par5, par6);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;

        if (entityliving instanceof EntityPlayer && PC_Utils.isPlacingReversed(((EntityPlayer)entityliving)))
        {
            l = PC_Utils.reverseSide(l);
        }

        world.setBlockMetadataWithNotify(i, j, k, l);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int s, int m)
    {
        if (m == 0)
        {
            m = 2;
        }
        else if (m == 1)
        {
            m = 5;
        }
        else if (m == 2)
        {
            m = 3;
        }
        else if (m == 3)
        {
            m = 4;
        }

        if (s == 1)
        {
            return TXTOP;
        }

        if (s == 0)
        {
            return TXDOWN;
        }
        else
        {
            if (m == s)
            {
                return TXBACK;
            }

            if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4))
            {
                return TXFRONT;
            }

            return TXSIDE;
        }
    }

    @Override
    public int getInvTexture(int i, int m)
    {
        if (i == 1)
        {
            return TXTOP;
        }

        if (i == 0)
        {
            return TXDOWN;
        }

        if (i == 3)
        {
            return TXFRONT;
        }
        else if (i == 4)
        {
            return TXBACK;
        }
        else
        {
            return TXSIDE;
        }
    }

    @Override
    public int tickRate()
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)
                    || world.isBlockIndirectlyGettingPowered(i, j - 1, k);

            if (flag)
            {
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
            }
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)
                || world.isBlockIndirectlyGettingPowered(i, j - 1, k))
        {
            ((PCma_TileEntityAutomaticWorkbench)PC_Utils.getTE(world, i, j, k)).doCrafting();
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = entityplayer.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID)
            {
                return false;
            }
        }

        PC_Utils.openGres("AutomaticWorkbench", entityplayer, i, j, k);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PCma_TileEntityAutomaticWorkbench();
    }
    
    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

	@Override
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Automatic Workbench";
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[1];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}
		}
		return null;
	}
   	
}
