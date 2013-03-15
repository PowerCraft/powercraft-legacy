package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_ISpecialInventoryTextures;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;

@PC_BlockInfo(tileEntity=PCma_TileEntityAutomaticWorkbench.class)
public class PCma_BlockAutomaticWorkbench extends PC_Block implements PC_ISpecialInventoryTextures, PC_IItemInfo
{
    private static final int TXTOP = 0, TXSIDE = 1, TXFRONT = 2;

    public PCma_BlockAutomaticWorkbench(int id)
    {
        super(id, Material.ground, "workbench_top", "side", "workbench_front");
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
            PC_InventoryUtils.dropInventoryContents(tew, world, tew.getCoord());
        }

        super.breakBlock(world, i, j, k, par5, par6);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemStack)
    {
        int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 2.5D) & 3;

        if (entityliving instanceof EntityPlayer && PC_KeyRegistry.isPlacingReversed(((EntityPlayer)entityliving)))
        {
            l = ValueWriting.reverseSide(l);
        }

        ValueWriting.setMD(world, i, j, k, l);
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int s, int m)
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
            return icons[TXTOP];
        }

        if (s == 0)
        {
            return icons[TXSIDE];
        }
        else
        {
            if (m == s)
            {
                return icons[TXSIDE];
            }

            if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4))
            {
                return icons[TXFRONT];
            }

            return icons[TXSIDE];
        }
    }

    @Override
    public Icon getInvTexture(int i, int m)
    {
        if (i == 1)
        {
            return icons[TXTOP];
        }

        if (i == 0)
        {
            return icons[TXSIDE];
        }

        if (i == 3)
        {
            return icons[TXFRONT];
        }
        else if (i == 4)
        {
            return icons[TXSIDE];
        }
        else
        {
            return icons[TXSIDE];
        }
    }

    @Override
    public int tickRate(World world)
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
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
            }
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)
                || world.isBlockIndirectlyGettingPowered(i, j - 1, k))
        {
            ((PCma_TileEntityAutomaticWorkbench)GameInfo.getTE(world, i, j, k)).doCrafting();
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
            if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID)
            {
                return false;
            }
        }

        PC_GresRegistry.openGres("AutomaticWorkbench", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
        return true;
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCma_TileEntityAutomaticWorkbench();
    }
    
    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Automatic Workbench";
		case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}
		}
		return null;
	}
   	
}
