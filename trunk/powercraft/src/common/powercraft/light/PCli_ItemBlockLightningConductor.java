package powercraft.light;

import net.minecraft.src.Block;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_ItemBlock;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;
import powercraft.transport.PCtr_BeltHelper;

public class PCli_ItemBlockLightningConductor extends PC_ItemBlock {

	protected PCli_ItemBlockLightningConductor(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(false);
	}

	@Override
	public String[] getDefaultNames() {
		return new String[]{
				getItemName(), "Lightning Conductor"
		};
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (PC_Utils.getBID(world, x, y+1, z)!=0)
			return false;
		
		if (!world.setBlockAndMetadataWithNotify(x, y, z, getBlockID(), 0)){
			return false;
	    }

		if (!world.setBlockAndMetadataWithNotify(x, y+1, z, getBlockID(), 1)){
			return false;
	    }
		
		if (world.getBlockId(x, y, z) == getBlockID()){
			Block block =  Block.blocksList[getBlockID()];
			block.updateBlockMetadata(world, x, y, z, side, hitX, hitY, hitZ);
			block.onBlockPlacedBy(world, x, y, z, player);
		}
		if (world.getBlockId(x, y+1, z) == getBlockID()){
			Block block =  Block.blocksList[getBlockID()];
			block.updateBlockMetadata(world, x, y+1, z, side, hitX, hitY, hitZ);
			block.onBlockPlacedBy(world, x, y+1, z, player);
			PC_TileEntity te = (PC_TileEntity)PC_Utils.getTE(world, x, y+1, z);
			if(te==null){
				te = (PC_TileEntity)PC_Utils.setTE(world, x, y+1, z, block.createTileEntity(world, 0));
			}
			if(te!=null)
				te.create(stack, player, world, x, y+1, z, side, hitX, hitY, hitZ);
		}
		
		return true;
	}

	
	
}
