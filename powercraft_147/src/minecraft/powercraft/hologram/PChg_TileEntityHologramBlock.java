package powercraft.hologram;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_TileEntity;
import powercraft.management.annotation.PC_ClientServerSync;

public class PChg_TileEntityHologramBlock extends PC_TileEntity {
	
	@PC_ClientServerSync()
	private int containingBlockID=0;
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag!=null){
			ItemStack is = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
			containingBlockID = is.itemID;
		}else{
			containingBlockID = 0;
		}
	}

	public Block getContainingBlock() {
		Block b = Block.blocksList[containingBlockID];
		if(b==null){
			b = Block.stone; 
		}
		return b;
	}
	
}
