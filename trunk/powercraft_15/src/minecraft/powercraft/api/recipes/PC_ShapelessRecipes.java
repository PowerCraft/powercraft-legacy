package powercraft.api.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_VecI;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.reflect.PC_ReflectHelper;

public class PC_ShapelessRecipes implements IRecipe, PC_IRecipeInfo, PC_IRecipe {

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
    	
        List<List<PC_ItemStack>> recipeItems = new ArrayList<List<PC_ItemStack>>();
        
        for (Object obj:o){
        	List<PC_ItemStack> list = new ArrayList<PC_ItemStack>();
        	if(obj instanceof Block){
        		list.add(new PC_ItemStack((Block)obj));
			}else if(obj instanceof Item){
				list.add(new PC_ItemStack((Item)obj));
			}else if(obj instanceof ItemStack){
				list.add(new PC_ItemStack((ItemStack)obj));
			}else if(obj instanceof PC_ItemStack){
				list.add((PC_ItemStack)obj);	
			}else if(obj instanceof List){
				list.addAll((List)obj);
			}
        	recipeItems.add(list);
        }
        
        this.recipeItems = recipeItems.toArray(new List[0]);
        
    }

    @Override
    public boolean canBeCrafted(){
		if(op==null)
			return true;
		if(!PC_GlobalVariables.consts.containsKey(op))
			return true;
		Object o = PC_GlobalVariables.consts.get(op);
		if(o instanceof Boolean)
			return (Boolean)o;
		return recipeOutput.getID()!=-1;
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
		int craftSizeX = PC_ReflectHelper.getValue(InventoryCrafting.class, inventoryCrafting, 1, int.class);
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
    	if (itemStack.getItem() instanceof PC_ItemBlock) {
			((PC_ItemBlock) itemStack.getItem()).doCrafting(itemStack,
					par1InventoryCrafting);
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

	@Override
	public int getPCRecipeSize() {
		return getRecipeSize();
	}

}
