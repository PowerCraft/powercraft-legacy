package powercraft.api.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_IIDChangeAble;
import powercraft.api.PC_IMSG;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.launcher.loader.PC_ModuleObject;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG, PC_IIDChangeAble
{
    private PC_ModuleObject module;
    private boolean canSetTextureFile = true;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    protected Icon[] icons;
    private String[] textureNames;
    
    protected PC_Item(int id, String textureName, String...textureNames){
    	super(id-256);
    	this.textureNames = new String[1+textureNames.length];
    	icons = new Icon[1+textureNames.length];
    	this.textureNames[0] = textureName;
    	for(int i=0; i<textureNames.length; i++){
    		this.textureNames[i+1] = textureNames[i];
    	}
    }
    
    @Override
    public void setID(int id){
    	int oldID = itemID;
		Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
		ItemData thisItemData = map.get(oldID);
		if(PC_ReflectHelper.setValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id, int.class)){
    		if(oldID!=-1){
    			if(replacedItemData==null){
    				map.remove(oldID);
    			}else{
    				PC_ReflectHelper.setValue(ItemData.class, replacedItemData, 3, oldID, int.class);
    				map.put(oldID, replacedItemData);
    			}
    			Item.itemsList[oldID] = replacedItem;
    		}
    		if(id!=-1){
    			replacedItemData = map.get(id);
    			replacedItem = Item.itemsList[id];
    			if(thisItemData==null){
    				map.remove(id);
    			}else{
    				PC_ReflectHelper.setValue(ItemData.class, thisItemData, 3, id, int.class);
    				map.put(id, thisItemData);
    			}
    			Item.itemsList[id] = this;
    		}else{
    			replacedItemData = null;
    			replacedItem = null;
    		}
    	}
    }
    
    public PC_ModuleObject getModule()
    {
        return module;
    }

    public void setModule(PC_ModuleObject module)
    {
    	this.module = module;
    }

    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

    @Override
	public boolean showInCraftingTool() {
		return true;
	}
    
    public void getSubItems(int index, CreativeTabs creativeTab, List list)
    {
        list.addAll(getItemStacks(new ArrayList<ItemStack>()));
    }

	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
	}

	public Object areItemsEqual(PC_ItemStack pc_ItemStack, int otherMeta, NBTTagCompound otherNbtTag) {
		return null;
	}
	
	@Override
	public Item setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(GameInfo.getCreativeTab(_default));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister){
		for(int i=0; i<textureNames.length; i++){
			icons[i] = par1IconRegister.func_94245_a(PC_TextureRegistry.getTextureDirectory(module)+":"+textureNames[i]);
		}
	}

	public Icon getIconFromDamage(int par1){
		if(par1>=icons.length){
			par1 = icons.length-1;
		}
        return icons[par1];
    }
	
}
