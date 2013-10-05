package powercraft.api.multiblocks.covers;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import org.lwjgl.opengl.GL11;

import powercraft.api.PC_Direction;
import powercraft.api.PC_Renderer;
import powercraft.api.items.PC_ItemInfo;
import powercraft.api.multiblocks.PC_BlockMultiblock;
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
		
	}

	@Override
	public void loadIcons() {
		
	}
	
	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.FACE;
	}
	
	private static String[] names = new String[]{"1/16", "1/8", "3/16", "1/4", "5/16", "3/8", "7/16", "1/2", "9/16", "5/8", "11/16", "3/4", "13/16", "7/8", "15/16", "1"}; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean advancedItemTooltips) {
		int thickness = getThickness(itemStack);
		ItemStack is = getInner(itemStack);
		list.add(names[thickness-1] + " of block: "+is.getDisplayName());
	}
	
	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		return type!=ItemRenderType.FIRST_PERSON_MAP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemStack item, ItemRenderType type, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemStack item, ItemRenderType type, Object... data) {
		if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON){
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}else if(type==ItemRenderType.ENTITY){
			GL11.glScalef(0.5f, 0.5f, 0.5f);
		}
		RenderBlocks renderBlocks = (RenderBlocks)data[0];
		ItemStack inner = getInner(item);
		Block block = getBlock(inner.itemID);
		int thickness = getThickness(item);
		Icon icons[] = new Icon[6];
		for(int i=0; i<6; i++){
			icons[i] = block.getIcon(i, inner.getItemDamage());
		}
		PC_BlockMultiblock.setIcons(icons);
		PC_BlockMultiblock.colorMultiplier = 0xFFFFFFFF;
		PC_BlockMultiblock.block.setBlockBounds(0, 0, 0.5f-thickness/32.0f, 1, 1, 0.5f+thickness/32.0f);
		PC_Renderer.renderInvBox(PC_BlockMultiblock.block, 0, renderBlocks);
		PC_BlockMultiblock.block.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public int getSpriteNumber(){
		return 0;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
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

	public static ItemStack getCoverItem(int thickness, ItemStack inner) {
		ItemStack is = new ItemStack(item, 1, thickness);
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		inner.writeToNBT(nbtTagCompound);
		is.setTagInfo("inner", nbtTagCompound);
		return is;
	}
	
}
