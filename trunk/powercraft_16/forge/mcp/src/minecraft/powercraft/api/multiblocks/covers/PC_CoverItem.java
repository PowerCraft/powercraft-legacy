package powercraft.api.multiblocks.covers;


import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		GameRegistry.addRecipe(new PC_CoverRecipes(Item.axeStone));
	}

	@Override
	public void loadIcons() {
		
	}
	
	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.FACE;
	}
	
	public static int getThickness(ItemStack itemStack){
		return itemStack.getItemDamage();
	}
	
	public static ItemStack getInner(ItemStack itemStack){
		return itemStack.getTagCompound()==null?null:ItemStack.loadItemStackFromNBT(itemStack.getTagCompound().getCompoundTag("inner"));
	}
	
	public static Icon getIconFormSide(ItemStack itemStack, PC_Direction side){
		ItemStack inner = getInner(itemStack);
		return getBlock(inner.itemID).getIcon(side.ordinal(), inner.getItemDamage());
	}
	
	private static Block getBlock(int block) {
		return Block.blocksList.length<=block || block<0?null:Block.blocksList[block];
	}

	public static ItemStack getCoverItem(int thickness, int metadata, Block block){
		ItemStack inner = new ItemStack(block, 1, metadata);
		ItemStack is = new ItemStack(item, 1, thickness);
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		inner.writeToNBT(nbtTagCompound);
		is.setTagInfo("inner", nbtTagCompound);
		return is;
	}

	String[] names = new String[]{"1/16", "1/8", "3/16", "1/4", "5/16", "3/8", "7/16", "1/2", "9/16", "5/8", "11/16", "3/4", "13/16", "7/8", "15/16", "1"}; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean advancedItemTooltips) {
		int thickness = getThickness(itemStack);
		ItemStack is = getInner(itemStack);
		list.add(names[thickness-1] + " of block: "+is.getDisplayName());
	}
	
}
