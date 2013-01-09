package powercraft.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class PC_ShapelessRecipes implements IRecipe, PC_IRecipeInfo {

	private final PC_ItemStack recipeOutput;
    private final List<PC_ItemStack>[] recipeItems;
    private String op;
    
    public PC_ShapelessRecipes(PC_ItemStack recipeOutput, List<PC_ItemStack>[] recipeItems) {
		this.recipeOutput = recipeOutput;
		this.recipeItems = recipeItems;
	}
    
    public PC_ShapelessRecipes(PC_ItemStack itemStack, Object... recipe) {
		this(null, itemStack, recipe);
	}
    
    public PC_ShapelessRecipes(String op, PC_ItemStack itemStack, Object...o)
    {
    	this.op = op;
    	recipeOutput = itemStack;
    	
        List<PC_ItemStack> recipeItems = new ArrayList<PC_ItemStack>();
        
        for (Object obj:o){
        	if(obj instanceof Block){
        		recipeItems.add(new PC_ItemStack((Block)obj));
			}else if(obj instanceof Item){
				recipeItems.add(new PC_ItemStack((Item)obj));
			}else if(obj instanceof ItemStack){
				recipeItems.add(new PC_ItemStack((ItemStack)obj));
			}else if(obj instanceof PC_ItemStack){
				recipeItems.add((PC_ItemStack)obj);	
			}else if(obj instanceof List){
				recipeItems.addAll((List)obj);
			}
        }
        
        this.recipeItems = recipeItems.toArray(new List[0]);
        
    }

    private boolean canBeCrafted(){
		if(op==null)
			return true;
		if(!PC_GlobalVariables.consts.containsKey(op))
			return true;
		Object o = PC_GlobalVariables.consts.get(op);
		if(o instanceof Boolean)
			return (Boolean)o;
		return true;
	}
    
    public ItemStack getRecipeOutput()
    {
    	if(!canBeCrafted())
    		return null;
        return recipeOutput.toItemStack();
    }

    public boolean matches(InventoryCrafting inventoryCrafting, World world)
    {
    	if(!canBeCrafted())
    		return false;
       
    	boolean[] used = new boolean[recipeItems.length];

    	int craftSizeY = inventoryCrafting.getSizeInventory();
		int craftSizeX = (Integer)ValueWriting.getPrivateValue(InventoryCrafting.class, inventoryCrafting, 1);
		craftSizeY = craftSizeY/craftSizeX;
		
		for (int y = 0; y < craftSizeY; y++) {
			for (int x = 0; x < craftSizeX; x++) {
				ItemStack get = inventoryCrafting.getStackInRowAndColumn(x, y);
				if(get!=null){
					boolean ok = false;
					for(int i=0; i<recipeItems.length; i++){
						if(!used[i]){
							List<PC_ItemStack> expect = recipeItems[i];
							for(PC_ItemStack is:expect){
								if(is.equals(get)){
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

    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
    	if(!canBeCrafted())
    		return null;
    	ItemStack itemStack = getRecipeOutput().copy();
    	if(itemStack.getItem() instanceof PC_Item){
    		((PC_Item)itemStack.getItem()).doCrafting(itemStack, par1InventoryCrafting);
    	}
        return itemStack;
    }

    public int getRecipeSize()
    {
    	if(!canBeCrafted())
    		return 0;
        return recipeItems.length;
    }

	@Override
	public PC_VecI getSize() {
    	return null;
	}

	@Override
	public List<PC_ItemStack> getExpectedInputFor(int index) {
		return recipeItems[index];
	}

   

}
