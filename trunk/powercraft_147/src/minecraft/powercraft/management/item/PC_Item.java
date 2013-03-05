package powercraft.management.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.launcher.PC_ModuleObject;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_IIDChangeAble;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.reflect.PC_ReflectHelper;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG, PC_IIDChangeAble
{
    private PC_ModuleObject module;
    private boolean canSetTextureFile = true;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    
    protected PC_Item(int id)
    {
        this(id, true);
    }

    public PC_Item(int id, boolean canSetTextureFile)
    {
        this(id, 0, canSetTextureFile);
    }
    
    public PC_Item(int id, int iconIndex) {
		this(id, iconIndex, true);
	}

    public PC_Item(int id, int iconIndex, boolean canSetTextureFile) {
    	super(id-256);
        this.canSetTextureFile = canSetTextureFile;
        setIconIndex(iconIndex);
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

    @Override
    public Item setTextureFile(String texture)
    {
        if (canSetTextureFile)
        {
            super.setTextureFile(texture);
        }

        return this;
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
	
}
