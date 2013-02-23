package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.reflect.PC_ReflectHelper;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG, PC_IIDChangeAble
{
    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private String currentTexture = "/gui/items.png";
    private Item replacedItem = null;
    
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
		if(oldID==id)
			return;
    	if(PC_ReflectHelper.setValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id)){
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

    public void getSubItems(int index, CreativeTabs creativeTab, List list)
    {
        list.addAll(getItemStacks(new ArrayList<ItemStack>()));
    }

    public Item setTextureFile(String texture)
    {
    	if(canSetTextureFile)
    		currentTexture = texture;
    	
        return this;
    }

    public String getTextureFile(){
    	return currentTexture;
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
