package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PC_ShapelessFurnaceRecipes implements PC_IFurnaceRecipe {

	private final List<PC_ItemStack> recipeOutput;
    private final List<PC_ItemStack>[] recipeItems;
    private int smeltTime;
    private String op;
    
    public PC_ShapelessFurnaceRecipes(PC_ItemStack itemStack, Object... recipe) {
		this(null, itemStack, recipe);
	}
    
    public PC_ShapelessFurnaceRecipes(String op, PC_ItemStack itemStack, Object...o)
    {
    	this.op = op;
    	recipeOutput = new ArrayList<PC_ItemStack>();
		recipeOutput.add(itemStack);
        
        int i=0;
		
		while(true){
			if(o[i] instanceof Block){
				recipeOutput.add(new PC_ItemStack((Block)o[i]));
			}else if(o[i] instanceof Item){
				recipeOutput.add(new PC_ItemStack((Item)o[i]));
			}else if(o[i] instanceof ItemStack){
				recipeOutput.add(new PC_ItemStack((ItemStack)o[i]));
			}else if(o[i] instanceof PC_ItemStack){
				recipeOutput.add((PC_ItemStack)o[i]);	
			}else if(o[i] instanceof List){
				recipeOutput.addAll((List)o[i]);
			}else{
				break;
			}
			i++;
		}
		
		if(o[i] instanceof Integer){
			smeltTime = (Integer)o[i];
			i++;
		}
		
		List<List<PC_ItemStack>> recipeItems = new ArrayList<List<PC_ItemStack>>();
        
        while (i<o.length){
        	Object obj = o[i];
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
        	i++;
        }
        
        this.recipeItems = recipeItems.toArray(new List[0]);
        
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
	public int getRecipeSize() {
		return recipeItems.length;
	}

	@Override
	public boolean canBeCrafted() {
		if(op==null)
			return true;
		if(!PC_GlobalVariables.consts.containsKey(op))
			return true;
		Object o = PC_GlobalVariables.consts.get(op);
		if(o instanceof Boolean)
			return (Boolean)o;
		return true;
	}

	@Override
	public List<PC_ItemStack> getRecipeOutput() {
		List<PC_ItemStack> out = new ArrayList<PC_ItemStack>();
		for(PC_ItemStack is:recipeOutput){
			out.add(is.copy());
		}
		return out;
	}

	@Override
	public int getSmeltTime() {
		return smeltTime;
	}

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world, ItemStack fuel) {
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

}
