package powercraft.management;

import java.util.List;

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
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int textureID, int type)
    {
        super(id-256 ,material, textureID, type);
    }

    public void setItemID(int id){
		int oldID = shiftedIndex;
    	if(ValueWriting.setPrivateValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id)){
    		if(oldID!=-1){
    			Item.itemsList[oldID] = replacedItem;
    		}
    		if(id!=-1){
    			replacedItem = Item.itemsList[id];
    			Item.itemsList[id] = this;
    		}else{
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
