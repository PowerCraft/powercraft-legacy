package powercraft.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Utils.ValueWriting;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG
{
    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    protected int iconIndexRenderPass2;
    
    protected PC_Item(int id)
    {
        this(id, true);
    }

    public PC_Item(int id, boolean canSetTextureFile)
    {
        this(id, 0, 0, canSetTextureFile);
    }
    
    public PC_Item(int id, int iconIndex) {
		this(id, iconIndex, 0);
	}
    
    public PC_Item(int id, int iconIndex, int iconIndexRenderPass2) {
		this(id, iconIndex, iconIndexRenderPass2, true);
	}

    public PC_Item(int id, int iconIndex, int iconIndexRenderPass2, boolean canSetTextureFile) {
    	super(id-256);
        this.canSetTextureFile = canSetTextureFile;
        setIconIndex(iconIndex);
        this.iconIndexRenderPass2 = iconIndexRenderPass2;
	}
    
    public void setItemID(int id){
    	int oldID = shiftedIndex;
		Map<Integer, ItemData> map = (Map<Integer, ItemData>)ValueWriting.getPrivateValue(GameData.class, GameData.class, 0);
		ItemData thisItemData = map.get(oldID);
		if(ValueWriting.setPrivateValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id)){
    		if(oldID!=-1){
    			if(replacedItemData==null){
    				map.remove(oldID);
    			}else{
    				ValueWriting.setPrivateValue(ItemData.class, replacedItemData, 3, oldID);
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
    				ValueWriting.setPrivateValue(ItemData.class, thisItemData, 3, id);
    				map.put(id, thisItemData);
    			}
    			Item.itemsList[id] = this;
    		}else{
    			replacedItemData = null;
    			replacedItem = null;
    		}
    	}
    }
    
    public PC_IModule getModule()
    {
        return module;
    }

    public void setModule(PC_IModule module)
    {
    	this.module = module;
    }

    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
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
	
}
