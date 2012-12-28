package powercraft.management;

import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Utils.ValueWriting;

public abstract class PC_ItemArmor extends ItemArmor implements PC_IItemInfo, PC_IMSG
{
    public static final int HEAD = 0, TORSO = 1, LEGS = 2, FEET = 3;

    private PC_IModule module;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int textureID, int type)
    {
        super(id-256 ,material, textureID, type);
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
}
