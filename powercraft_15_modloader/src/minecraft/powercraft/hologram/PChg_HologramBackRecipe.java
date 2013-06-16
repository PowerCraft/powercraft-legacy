package powercraft.hologram;

import java.util.List;

import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_ShapelessRecipes;
import powercraft.api.reflect.PC_ReflectHelper;

public class PChg_HologramBackRecipe extends PC_ShapelessRecipes {

	public PChg_HologramBackRecipe() {
		super(null, (Object)new PC_ItemStack(PChg_App.hologramBlock));
	}

	public ItemStack getRecipeOutput(){
    	return null;
    }

	public boolean matches(InventoryCrafting inventoryCrafting, World world)
    {
    	if(!canBeCrafted())
    		return false;
    	
    	boolean[] used = new boolean[getRecipeSize()];

    	int craftSizeY = inventoryCrafting.getSizeInventory();
		int craftSizeX = PC_ReflectHelper.getValue(InventoryCrafting.class, inventoryCrafting, 1, int.class);
		craftSizeY = craftSizeY/craftSizeX;
		
		for (int y = 0; y < craftSizeY; y++) {
			for (int x = 0; x < craftSizeX; x++) {
				ItemStack get = inventoryCrafting.getStackInRowAndColumn(x, y);
				if(get!=null){
					boolean ok = false;
					for(int i=0; i<getRecipeSize(); i++){
						if(!used[i]){
							List<PC_ItemStack> expect = getExpectedInputFor(i);
							for(PC_ItemStack is:expect){
								if(is.getID()==get.itemID){
									ok = true;
									break;
								}
							}
							if(ok){
								used[i] = true;
								break;
							}
						}
					}
					if(!ok)
						return false;
				}
			}
		}
		
		for(int i=0; i<used.length; i++){
			if(!used[i])
				return false;
		}
		
        return true;
    }
	
    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting){
    	if(!canBeCrafted())
    		return null;
    	ItemStack hb = null;
    	for(int i=0; i<par1InventoryCrafting.getSizeInventory(); i++){
    		if(par1InventoryCrafting.getStackInSlot(i)!=null){
    			hb = par1InventoryCrafting.getStackInSlot(i);
    			break;
    		}
    	}
    	NBTTagCompound nbtTag = hb.getTagCompound();
    	if(nbtTag==null)
    		return null;
    	ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag("Item"));
    	if(itemStack.getItem() instanceof PC_Item){
    		((PC_Item)itemStack.getItem()).doCrafting(itemStack, par1InventoryCrafting);
    	}
    	if (itemStack.getItem() instanceof PC_ItemBlock) {
			((PC_ItemBlock) itemStack.getItem()).doCrafting(itemStack,
					par1InventoryCrafting);
		}
        return itemStack;
    }
	
}
