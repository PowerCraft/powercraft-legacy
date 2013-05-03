package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;

public class PCli_ItemBlockLightningConductor extends PC_ItemBlock
{
    public PCli_ItemBlockLightningConductor(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(false);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (PC_Utils.getBID(world, x, y + 1, z) != 0)
        {
            return false;
        }

        if (!PC_Utils.setBID(world, x, y, z, getBlockID(), 0))
        {
            return false;
        }

        if (!PC_Utils.setBID(world, x, y + 1, z, getBlockID(), 1))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            world.removeBlockTileEntity(x, y, z);
        }

        if (world.getBlockId(x, y + 1, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y + 1, z, player, stack);
            PC_TileEntity te = PC_Utils.getTE(world, x, y + 1, z);

            if (te != null)
            {
                te.create(stack, player, world, x, y + 1, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }
}
