package powercraft.api.blocks;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.api.PC_CreativeTab;
import powercraft.api.PC_Utils;


public class PC_ItemBlock extends ItemBlock {

	public PC_ItemBlock(int id) {

		super(id);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		if(getCreativeTab()==null)
			return new CreativeTabs[]{};
		return new CreativeTabs[]{ getCreativeTab(), PC_CreativeTab.getCrativeTab()};
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata){
		if(!PC_Utils.setBID(world, x, y, z, getBlockID(), metadata)){
			return false;
		}
		if(player!=null){
	    	TileEntity te = PC_Utils.getTE(world, x, y, z);
	    	if(te instanceof PC_TileEntity){
	    		((PC_TileEntity) te).setOwner(player.username);
	    	}
	    }
	    return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }
	
}
