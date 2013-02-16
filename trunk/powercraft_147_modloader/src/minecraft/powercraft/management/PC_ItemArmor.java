package powercraft.management;

import java.util.List;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;

public abstract class PC_ItemArmor extends ItemArmor implements PC_IItemInfo, PC_IMSG
{
    public static final int HEAD = 0, TORSO = 1, LEGS = 2, FEET = 3;

    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private String currentTexture = "/gui/items.png";
    private String armorTexture;
    private Item replacedItem = null;
    
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
    
    public void setArmorTextureFile(String armorTexture) {
		this.armorTexture = armorTexture;
	}
    
    public String getArmorTextureFile(ItemStack itemstack) {
		return armorTexture;
	}

    public Item setTextureFile(String texture){
    	if(canSetTextureFile){
    		currentTexture = texture;
    	}
    	return this;
    }
    
	public String getTextureFile() {
		return currentTexture;
	}
    
	@Override
	public Item setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(GameInfo.getCreativeTab(_default));
	}
	
}
