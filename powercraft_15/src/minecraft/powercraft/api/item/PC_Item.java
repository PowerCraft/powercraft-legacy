package powercraft.api.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import powercraft.api.interfaces.PC_IIDChangeAble;
import powercraft.api.interfaces.PC_IMSG;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.loader.PC_ModuleObject;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IIDChangeAble {
	
	private PC_ModuleObject module;
	private boolean canSetTextureFile = true;
	private PC_ItemInfo replaced;
	private PC_ItemInfo thisItem;
	protected Icon[] icons;
	private String[] textureNames;
	
	protected PC_Item(int id, String textureName, String... textureNames) {
		super(id - 256);
		thisItem = new PC_ItemInfo(id);
		this.textureNames = new String[1 + textureNames.length];
		icons = new Icon[1 + textureNames.length];
		this.textureNames[0] = textureName;
		for (int i = 0; i < textureNames.length; i++) {
			this.textureNames[i + 1] = textureNames[i];
		}
	}
	
	public abstract List<LangEntry> getNames(ArrayList<LangEntry> names);
	
	@Override
	public void setID(int id) {
		int oldID = itemID;
		if (PC_ReflectHelper.setValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id, int.class)) {
			if (oldID != -1) {
				if(replaced==null){
					replaced = new PC_ItemInfo(-1); 
				}
				replaced.storeToID(oldID);
			}
			if (id != -1) {
				replaced = new PC_ItemInfo(id);
				thisItem.storeToID(id);
			} else {
				new PC_ItemInfo(-1).storeToID(oldID);
				replaced = null;
			}
		}
	}
	
	public PC_ModuleObject getModule() {
		return module;
	}
	
	public void setModule(PC_ModuleObject module) {
		this.module = module;
	}
	
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
	
	@Override
	public boolean showInCraftingTool() {
		return true;
	}
	
	public void getSubItems(int index, CreativeTabs creativeTab, List list) {
		list.addAll(getItemStacks(new ArrayList<ItemStack>()));
	}
	
	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
	}
	
	public Object areItemsEqual(PC_ItemStack pc_ItemStack, int otherMeta, NBTTagCompound otherNbtTag) {
		return null;
	}
	
	@Override
	public Item setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(PC_Utils.getCreativeTab(_default));
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		for (int i = 0; i < textureNames.length; i++) {
			icons[i] = par1IconRegister.registerIcon(PC_TextureRegistry.getTextureName(module, textureNames[i]));
		}
	}
	
	public Icon getIconFromDamage(int par1) {
		if (par1 >= icons.length) {
			par1 = icons.length - 1;
		}
		return icons[par1];
	}
	
	public int getBurnTime(ItemStack fuel) {
		return 0;
	}
	
}
