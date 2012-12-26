package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

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
        if (GameInfo.getBID(world, x, y + 1, z) != 0)
        {
            return false;
        }

        if (!world.setBlockAndMetadataWithNotify(x, y, z, getBlockID(), 0))
        {
            return false;
        }

        if (!world.setBlockAndMetadataWithNotify(x, y + 1, z, getBlockID(), 1))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player);
        }

        if (world.getBlockId(x, y + 1, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y + 1, z, player);
            PC_TileEntity te = (PC_TileEntity)GameInfo.getTE(world, x, y + 1, z);

            if (te == null)
            {
            	if(block instanceof BlockContainer){
            		te = (PC_TileEntity)ValueWriting.setTE(world, x, y + 1, z, ((BlockContainer)block).createNewTileEntity(world));
            	}
            }

            if (te != null)
            {
                te.create(stack, player, world, x, y + 1, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Lightning Conductor", null));
            return names;
		}
		return null;
	}
}
