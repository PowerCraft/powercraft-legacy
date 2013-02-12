package powercraft.management;

import java.util.List;
import java.util.Map;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;
import powercraft.management.PC_Utils.ValueWriting;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

public abstract class PC_ItemArmor extends ItemArmor implements PC_IItemInfo, PC_IMSG, IArmorTextureProvider
{
    public static final int HEAD = 0, TORSO = 1, LEGS = 2, FEET = 3;

    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private String armorTexture;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int type){
    	this(id, material, type, true);
    }

    protected PC_ItemArmor(int id, EnumArmorMaterial material, int type, boolean canSetTextureFile){
    	this(id, material, type, 0, canSetTextureFile);
    }
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int type, int iconIndex){
        this(id, material, type, iconIndex, true);
    }
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int type, int iconIndex, boolean canSetTextureFile){
        super(id-256, material, 2, type);
        this.canSetTextureFile = canSetTextureFile;
        setIconIndex(iconIndex);
    }
    
    public void setItemID(int id){
    	int oldID = itemID;
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
    
    @Override
    public Item setTextureFile(String texture){
    	if(canSetTextureFile){
    		super.setTextureFile(texture);
    	}
    	return this;
    }
    
    public void setArmorTextureFile(String armorTexture) {
		this.armorTexture = armorTexture;
	}
    
    @Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return armorTexture;
	}
    
}
