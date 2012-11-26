package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class PC_ItemBlock extends ItemBlock implements PC_ICraftingToolDisplayer
{
    private String craftingToolModule;

    protected PC_ItemBlock(int id)
    {
        super(id);
    }

    public abstract String[] getDefaultNames();

    @Override
    public String getCraftingToolModule()
    {
        return craftingToolModule;
    }

    public void setCraftingToolModule(String module)
    {
        craftingToolModule = module;
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
            PC_TileEntity te = (PC_TileEntity)PC_Utils.getTE(world, x, y, z);

            if (te == null)
            {
                te = (PC_TileEntity)PC_Utils.setTE(world, x, y, z, block.createTileEntity(world, metadata));
            }

            if (te != null)
            {
                te.create(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

}
