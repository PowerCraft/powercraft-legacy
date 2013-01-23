package powercraft.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.management.PC_Utils.ValueWriting;

public class PC_ShapedFurnaceRecipes implements PC_IFurnaceRecipe {

	private PC_VecI size;
	private List<PC_ItemStack>[][] recipeItems;
	private List<PC_ItemStack> recipeOutput;
	private int smeltTime;
	private String op;
	
	public PC_ShapedFurnaceRecipes(PC_ItemStack itemStack, Object... recipe) {
		this(null, itemStack, recipe);
	}
	
	public PC_ShapedFurnaceRecipes(String op, PC_ItemStack itemStack, Object... o) {
		this.op = op;
		recipeOutput = new ArrayList<PC_ItemStack>();
		recipeOutput.add(itemStack);
		size = new PC_VecI();
		smeltTime = 400;
		
		List<String> lines = new ArrayList<String>();
		HashMap<Character, List<PC_ItemStack>> map = new HashMap<Character, List<PC_ItemStack>>();
		
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
		
		while(o[i] instanceof String){
			lines.add((String)o[i]);
			i++;
		}
		
		while(i<o.length && o[i] instanceof Character){
			char c = (Character)o[i];
			i++;
			List<PC_ItemStack> list = new ArrayList<PC_ItemStack>();
			while(i<o.length){
				if(o[i] instanceof Block){
					list.add(new PC_ItemStack((Block)o[i]));
				}else if(o[i] instanceof Item){
					list.add(new PC_ItemStack((Item)o[i]));
				}else if(o[i] instanceof ItemStack){
					list.add(new PC_ItemStack((ItemStack)o[i]));
				}else if(o[i] instanceof PC_ItemStack){
					list.add((PC_ItemStack)o[i]);	
				}else if(o[i] instanceof List){
					list.addAll((List)o[i]);
				}else{
					break;
				}
				i++;
			}
			map.put(c, list);
		}
		
		size.y = lines.size();
		for(String line:lines){
			if(line.length()>size.x)
				size.x = line.length();
		}
		
		recipeItems = new List[size.x][size.y];
		
		for(int y=0; y<size.y; y++){
			String line = lines.get(y);
			for(int x=0; x<line.length(); x++){
				char c = line.charAt(x);
				recipeItems[x][y] = map.get(c);
			}
		}
		
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

		int craftSizeY = inventoryCrafting.getSizeInventory();
		int craftSizeX = (Integer)ValueWriting.getPrivateValue(InventoryCrafting.class, inventoryCrafting, 1);
		craftSizeY = craftSizeY/craftSizeX;
		for (int y = 0; y <= craftSizeY - size.y; y++) {
			for (int x = 0; x <= craftSizeX - size.x; x++) {
				boolean otherEmpty = true;
				for (int yy = 0; yy < craftSizeY && otherEmpty; yy++) {
					for (int xx = 0; xx <= craftSizeX && otherEmpty; xx++) {
						if(inventoryCrafting.getStackInRowAndColumn(xx, yy)!=null){
							if(xx<x || xx>=x+size.x || yy<y || yy>=y+size.y)
								otherEmpty = false;
						}
					}
				}
				if (otherEmpty) {
					if (checkMatch(inventoryCrafting, x, y)) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	private boolean checkMatch(InventoryCrafting inventoryCrafting, int x, int y) {
		for(int j=0; j<size.y; j++){
			for(int i=0; i<size.x; i++){
				List<PC_ItemStack> expect = recipeItems[i][j];
				ItemStack get = inventoryCrafting.getStackInRowAndColumn(x+i, y+j);
				if(expect==null && get!=null){
					return false;
				}else if(expect==null && get==null){
					continue;
				}
				boolean ok = false;
				for(PC_ItemStack is:expect){
					if(is.equals(get)){
						ok = true;
						break;
					}
				}
				if(!ok)
					return false;
			}
		}
		return true;
	}
	
	@Override
	public PC_VecI getSize() {
		return size.copy();
	}

	@Override
	public List<PC_ItemStack> getExpectedInputFor(int index) {
		int y = index/size.x;
		int x = index%size.x;
		return recipeItems[x][y];
	}

	@Override
	public int getRecipeSize() {
		return size.x * size.y;
	}

}
