package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

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
            PC_TileEntity te = (PC_TileEntity)GameInfo.getTE(world, x, y, z);

            if (te == null)
            {
                te = (PC_TileEntity)ValueWriting.setTE(world, x, y, z, block.createTileEntity(world, metadata));
            }

            if (te != null)
            {
                te.create(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

}
