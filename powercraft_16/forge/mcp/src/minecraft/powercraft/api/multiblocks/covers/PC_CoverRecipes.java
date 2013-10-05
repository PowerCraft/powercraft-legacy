package powercraft.api.multiblocks.covers;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class PC_CoverRecipes implements IRecipe {

	private Item splitter;
	
    public PC_CoverRecipes(Item splitter){
    	this.splitter = splitter;
    }
    
    @Override
    public ItemStack getRecipeOutput(){
        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world){
        return getCraftingResult(inventoryCrafting)!=null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting){
    	int count = 0;
    	for(int i=0; i<3; i++){
    		for(int j=0; j<3; j++){
    			if(inventoryCrafting.getStackInRowAndColumn(i, j)!=null){
    				count++;
    			}
    		}
    	}
    	if(count!=2)
    		return null;
    	for(int i=0; i<3; i++){
    		for(int j=0; j<3; j++){
    			ItemStack itemStack = inventoryCrafting.getStackInRowAndColumn(i, j);
    			if(itemStack != null && itemStack.getItem() == splitter){
    				itemStack =  getStackInRowAndColumn(inventoryCrafting, i, j+1);
    				if(itemStack != null){
    					return makeHalfStack(itemStack);
    				}
    				return null;
    			}
    		}
    	}
    	return null;
    }

    private static ItemStack getStackInRowAndColumn(InventoryCrafting inventoryCrafting, int x, int y){
    	if(x<0 || x>=3 || y<0 || y>=3){
    		return null;
    	}
    	return inventoryCrafting.getStackInRowAndColumn(x, y);
    }
    
    private static ItemStack makeHalfStack(ItemStack itemStack){
    	Item item = itemStack.getItem();
    	ItemStack is;
    	int thickness = 8;
    	if(item==PC_CoverItem.item){
    		is = PC_CoverItem.getInner(itemStack);
    		thickness = PC_CoverItem.getThickness(itemStack);
    		if(thickness % 2 != 0)
    			return null;
    		thickness /= 2;
    	}else{
    		if(item instanceof ItemBlock){
    			is = new ItemStack(item, 2, itemStack.getItemDamage());
    		}else{
    			return null;
    		}
    	}
    	Block block = Block.blocksList[is.itemID];
    	if(block!=null && Block.isNormalCube(block.blockID)){
    		is = PC_CoverItem.getCoverItem(thickness, is.getItemDamage(), block);
    		is.stackSize = 2;
    		return is;
    	}
    	return null;
    }
    
    @Override
    public int getRecipeSize(){
        return 2;
    }
	
}
