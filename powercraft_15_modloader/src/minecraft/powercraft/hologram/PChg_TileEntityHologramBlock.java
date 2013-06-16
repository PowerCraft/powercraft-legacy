package powercraft.hologram;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.tileentity.PC_TileEntity;

public class PChg_TileEntityHologramBlock extends PC_TileEntity {
	
	@PC_ClientServerSync(clientChangeAble=false)
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
