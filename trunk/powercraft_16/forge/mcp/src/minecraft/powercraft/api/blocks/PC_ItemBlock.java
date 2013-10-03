package powercraft.api.blocks;


import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_CreativeTab;
import powercraft.api.PC_Direction;


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
		Block block = Block.blocksList[getBlockID()];
		if(block instanceof PC_Block)
			metadata = ((PC_Block)block).modifyMetadataPostPlace(world, x, y, z, PC_Direction.getOrientation(side), hitX, hitY, hitZ, metadata, stack, player);
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }
	
}
