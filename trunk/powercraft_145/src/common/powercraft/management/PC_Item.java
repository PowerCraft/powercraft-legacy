package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.PC_Utils.ValueWriting;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG
{
    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private Item replacedItem = null;
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
    	if(ValueWriting.setPrivateValue(Item.class, this, 160, id)){
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
