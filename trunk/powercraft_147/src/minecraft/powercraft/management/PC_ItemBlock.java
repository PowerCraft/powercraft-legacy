package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public abstract class PC_ItemBlock extends ItemBlock implements PC_IItemInfo, PC_IMSG
{
    private PC_IModule module;

    protected PC_ItemBlock(int id)
    {
        super(id);
    }

    @Override
    public PC_IModule getModule()
    {
        return module;
    }

    public void setModule(PC_IModule module)
    {
    	this.module = module;
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

    @Override
    public void getSubItems(int index, CreativeTabs creativeTab, List list)
    {
        list.addAll(getItemStacks(new ArrayList<ItemStack>()));
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!world.setBlockAndMetadataWithNotify(x, y, z, getBlockID(), metadata))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player);
            TileEntity te = (TileEntity)GameInfo.getTE(world, x, y, z);

            if (te == null)
            {
                te = (TileEntity)ValueWriting.setTE(world, x, y, z, block.createTileEntity(world, metadata));
            }

            if (te instanceof PC_TileEntity)
            {
                ((PC_TileEntity)te).create(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

    public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
	}
    
}
