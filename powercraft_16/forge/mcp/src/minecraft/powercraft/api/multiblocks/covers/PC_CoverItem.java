package powercraft.api.multiblocks.covers;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.PC_Direction;
import powercraft.api.items.PC_ItemInfo;
import powercraft.api.multiblocks.PC_MultiblockItem;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_MultiblockType;

@PC_ItemInfo(name="Cover", itemid="cover", defaultid = 17004)
public class PC_CoverItem extends PC_MultiblockItem {

	public static PC_CoverItem item;
	
	public PC_CoverItem(int id) {
		super(id);
		if(getClass()==PC_CoverItem.class)
			item = this;
		setCreativeTab(CreativeTabs.tabBlock);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(int itemID, CreativeTabs creativeTab, List list) {
		list.add(getCoverItem(2, 0, Block.stone));
	}

	@Override
	public Class<? extends PC_MultiblockTileEntity> getTileEntityClass() {
		return PC_CoverTileEntity.class;
	}

	@Override
	public void loadMultiblockItem() {
		
	}

	@Override
	public void registerRecipes() {
		
	}

	@Override
	public void loadIcons() {
		
	}
	
	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.FACE;
	}
	
	public static int getThickness(ItemStack itemStack){
		return (itemStack.getItemDamage() & 7) + 1;
	}
	
	public static int getMetadata(ItemStack itemStack){
		return (itemStack.getItemDamage()>>3 & 15);
	}
	
	public static Block getBlock(ItemStack itemStack){
		return Block.blocksList[(itemStack.getItemDamage()>>7)];
	}
	
	public static Icon getIconFormSide(ItemStack itemStack, PC_Direction side){
		return getBlock(itemStack).getIcon(side.ordinal(), getMetadata(itemStack));
	}
	
	public static ItemStack getCoverItem(int thickness, int metadata, Block block){
		int damage = (thickness & 7)|((metadata & 15)<<3)|(block.blockID<<7);
		return new ItemStack(item, 1, damage);
	}
	
}
