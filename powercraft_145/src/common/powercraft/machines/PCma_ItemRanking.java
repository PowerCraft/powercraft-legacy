package powercraft.machines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PCma_ItemRanking {

	private static TreeMap<PC_ItemStack, PC_Struct2<Float, Integer>> ranking = new TreeMap<PC_ItemStack, PC_Struct2<Float, Integer>>();
	private static List<PC_ItemStack> allreadyDone = new ArrayList<PC_ItemStack>();
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
	
	private static boolean makeTree(boolean print){
		boolean anyDone = false;
		for(Item item: Item.itemsList){
			if(item!=null){
				List<ItemStack> l = null;
				if(item instanceof PC_IItemInfo){
					l = ((PC_IItemInfo)item).getItemStacks(new ArrayList<ItemStack>());
				}else if (item instanceof ItemBlock) {
                    int id = ((ItemBlock)item).getBlockID();
                    Block b = Block.blocksList[id];
                    if (b != null){
	                    if (b instanceof PC_IItemInfo){
	                        l = ((PC_IItemInfo)b).getItemStacks(new ArrayList<ItemStack>());
	                    }else{
	                    	if (item.getHasSubtypes()){
	                        	l = new ArrayList<ItemStack>();
	                        	boolean bo = false;
	                            for (int j = 0; true; j++){
	                                ItemStack is = new ItemStack(b, 1, j);
	                                if (GameInfo.getRecipesForProduct(is).size() > 0) {
	                                	l.add(is);
	                                	bo = false;
	                                }else{
	                                	if(bo)
	                                		break;
	                                	bo = true;
	                                }
	                            }
	                        }else{
	                        	l = new ArrayList<ItemStack>();
	                        	l.add(new ItemStack(b));
	                        }
	                    }
                    }
                }else if (item.getHasSubtypes()){
                	l = new ArrayList<ItemStack>();
                	boolean b = false;
                    for (int j = 0; true; j++){
                        ItemStack is = new ItemStack(item, 1, j);
                        if (GameInfo.getRecipesForProduct(is).size() > 0) {
                        	l.add(is);
                        	b = false;
                        }else{
                        	if(b)
                        		break;
                        	b = true;
                        }
                    }
                }else{
                	l = new ArrayList<ItemStack>();
                	l.add(new ItemStack(item));
                }
				if(l!=null){
					if(print)
						System.out.println(item.getItemName()+":stacks:"+l.size());
					for(ItemStack is:l){
						PC_ItemStack pcis = new PC_ItemStack(is);
						if(allreadyDone.contains(pcis))
							continue;
						List<IRecipe> recipes = GameInfo.getRecipesForProduct(is);
						if(print)
							System.out.println("Recipes:"+recipes.size());
						for(IRecipe recipe:recipes){
							ItemStack[] input = GameInfo.getExpectedInput(recipe);
							if(input==null){
				            	if(print)
				            		System.out.println("Unknown Recipe:"+recipe);
				                continue;
				            }
							float rank=0;
							for(ItemStack itemstack:input){
								if(itemstack==null)
									continue;
								PC_ItemStack pcisi = new PC_ItemStack(itemstack);
								PC_Struct2<Float, Integer> s =ranking.get(pcisi);
								if(s != null){
									rank += s.a;
								}else{
									if(print)
										System.out.println("Can't find "+pcisi+" for "+pcis);
									rank = -1;
									break;
								}
							}
							if(rank>0){
								rank /= recipe.getRecipeOutput().stackSize;
								setRank(pcis, (int)(rank+2.5));
								anyDone = true;
								if(!allreadyDone.contains(pcis))
									allreadyDone.add(pcis);
							}
						}
					}
				}else{
					if(print)
						System.out.println("Error");
				}
			}
		}
		return anyDone;
	}
	
	private static Item getItem(String key){
		for(Item i:Item.itemsList){
			if(i!=null && key.equals(i.getItemName()))
				return i;
		}
		return null;
	}
	
	private static void load(String key, PC_Property prop){
		if(prop.hasChildren()){
			if(!key.equals("")){
				key += ".";
			}
			HashMap<String, PC_Property> porps = prop.getPropertys();
			for(Entry<String, PC_Property> e:porps.entrySet()){
				load(key + e.getKey(), e.getValue());
			}
		}else{
			Item item = getItem(key);
			List<Integer> l = GameInfo.parseIntList(prop.getString());
			int num=0;
			for(Integer i:l){
				if(i>0)
					setRank(new PC_ItemStack(item, 1, num), i);
				num++;
			}
		}
	}
	
	private static void reg(PC_Property prop, Item item, String nums){
		prop.getString(item.getItemName(), nums);
	}
	
	private static void reg(PC_Property prop, Block block, String nums){
		prop.getString(Item.itemsList[block.blockID].getItemName(), nums);
	}
	
	public static void init(){
		if(hasInit)
			return;
		
		File file = new File(GameInfo.getPowerCraftFile(), "/itemRanks.cfg");
		
		PC_Property prop;
		
		try {
			prop = PC_Property.loadFromFile(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			prop = new PC_Property();
		}
		
		reg(prop, Item.appleRed, "1");
		reg(prop, Item.diamond, "1");
		reg(prop, Item.ingotIron, "1");
		reg(prop, Item.ingotGold, "1");
		reg(prop, Item.silk, "1");
		reg(prop, Item.feather, "1");
		reg(prop, Item.reed, "1");
		reg(prop, Item.leather, "1");
		reg(prop, Item.seeds, "1");
		reg(prop, Item.flint, "1");
		reg(prop, Item.redstone, "1");
		reg(prop, Item.brick, "1");
		reg(prop, Item.carrot, "1");
		reg(prop, Item.gunpowder, "1");
		reg(prop, Item.pumpkinPie, "1");
		reg(prop, Item.pumpkinSeeds, "1");
		reg(prop, Item.melon, "1");
		reg(prop, Item.snowball, "1");
		reg(prop, Item.clay, "1");
		reg(prop, Item.coal, "1");
		reg(prop, Item.emerald, "1");
		reg(prop, Item.spiderEye, "1");
		reg(prop, Item.slimeBall, "1");
		reg(prop, Item.eyeOfEnder, "1");
		reg(prop, Item.enderPearl, "1");
		reg(prop, Item.blazePowder, "1");
		reg(prop, Item.blazeRod, "1");
		reg(prop, Item.egg, "1");
		reg(prop, Item.wheat, "1");
		
		reg(prop, Block.dirt, "1");
		reg(prop, Block.cobblestone, "1");
		reg(prop, Block.cobblestoneMossy, "1");
		reg(prop, Block.sapling, "1");
		reg(prop, Block.sand, "1");
		reg(prop, Block.gravel, "1");
		reg(prop, Block.wood, "1, 1, 1, 1");
		reg(prop, Block.stoneBrick, "1");
		reg(prop, Block.pumpkin, "1");
		reg(prop, Block.melon, "1");
		reg(prop, Block.obsidian, "1");
		reg(prop, Block.mushroomBrown, "1");
		reg(prop, Block.mushroomRed, "1");
		reg(prop, Block.plantRed, "1");
		reg(prop, Block.plantYellow, "1");
		
		load("", prop);
		
		while(makeTree(false));
		
		makeTree(true);
		
		for(Entry<PC_ItemStack, PC_Struct2<Float, Integer>> e : ranking.entrySet()){
			System.out.println(e.getKey().toString() + ", ranked:"+e.getValue().a);
		}
		
		try {
			prop.save(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		hasInit = true;
	}
	
}
