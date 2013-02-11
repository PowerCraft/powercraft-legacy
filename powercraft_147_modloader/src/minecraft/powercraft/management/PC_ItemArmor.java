package powercraft.management;

import java.util.List;

import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;

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
		int oldID = itemID;
		if(oldID == id)
			return;
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
    
    public String getArmorTextureFile(ItemStack itemstack) {
		return null;
	}
    
}
