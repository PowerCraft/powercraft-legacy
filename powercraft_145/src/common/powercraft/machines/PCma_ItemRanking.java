package powercraft.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_IRecipeInputInfo;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PCma_ItemRanking {

	private static TreeMap<PC_ItemStack, PC_Struct2<Float, Integer>> ranking = new TreeMap<PC_ItemStack, PC_Struct2<Float, Integer>>();
	private static boolean hasInit = false;
	
	private static void setRank(PC_ItemStack itemstack, int rank){
		PC_Struct2<Float, Integer> s;
		if(ranking.containsKey(itemstack)){
			s = ranking.get(itemstack);
		}else{
			ranking.put(itemstack, s = new PC_Struct2<Float, Integer>(0.0f, 0));
		}
		s.a *= s.b;
		s.a += rank;
		s.b++;
		s.a /= s.b;
	}
	
	public static float getRank(PC_ItemStack itemstack){
		if(ranking.containsKey(itemstack)){
			return ranking.get(itemstack).a;
		}
		return 0;
	}
	
	private static boolean makeTree(){
		boolean anyDone = false;
		for(Item item: Item.itemsList){
			if(item!=null){
				List<ItemStack> l = null;
				if(item instanceof PC_IItemInfo){
					l = ((PC_IItemInfo)item).getItemStacks(new ArrayList<ItemStack>());
				}else if (item instanceof ItemBlock) {
                    int id = ((ItemBlock)item).getBlockID();
                    Block b = Block.blocksList[id];
                    if (b != null && b instanceof PC_IItemInfo){
                        l = ((PC_IItemInfo)b).getItemStacks(new ArrayList<ItemStack>());
                    }
                }else if (item.getHasSubtypes()){
                	l = new ArrayList<ItemStack>();
                    for (int j = 0; true; j++){
                        ItemStack is = new ItemStack(item, 1, j);

                        if (GameInfo.getRecipesForProduct(is).size() > 0) {
                        	l.add(is);
                        }else{
                        	break;
                        }
                    }
                }
				for(ItemStack is:l){
					List<IRecipe> recipes = GameInfo.getRecipesForProduct(is);
					for(IRecipe recipe:recipes){
						ItemStack[] input;
						if (recipe instanceof PC_IRecipeInputInfo){
							input = ((PC_IRecipeInputInfo) recipe).getExpectedInput(new ArrayList<ItemStack>()).toArray(new ItemStack[0]);
			            }else if (recipe instanceof ShapedRecipes){
			            	input = (ItemStack[]) ValueWriting.getPrivateValue(ShapedRecipes.class, recipe, 2);
			            }else if (recipe instanceof ShapelessRecipes){
			                List<ItemStack> foo = ((List<ItemStack>) ValueWriting.getPrivateValue(ShapelessRecipes.class, recipe, 1));
			                input = foo.toArray(new ItemStack[foo.size()]);
			            }else{
			                continue;
			            }
						int rank=0;
						for(ItemStack itemstack:input){
							PC_ItemStack pcis = new PC_ItemStack(itemstack);
							if(ranking.containsKey(pcis)){
								
							}
						}
					}
				}
			}
		}
		return anyDone;
	}
	
	public static void init(){
		if(hasInit)
			return;
		setRank(new PC_ItemStack(Item.appleRed), 1);
		setRank(new PC_ItemStack(Item.coal), 1);
		setRank(new PC_ItemStack(Item.diamond), 1);
		setRank(new PC_ItemStack(Item.ingotIron), 1);
		setRank(new PC_ItemStack(Item.ingotGold), 1);
		setRank(new PC_ItemStack(Item.silk), 1);
		setRank(new PC_ItemStack(Item.feather), 1);
		makeTree();
		hasInit = true;
	}
	
}
