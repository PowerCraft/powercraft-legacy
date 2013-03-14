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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import powercraft.launcher.PC_Property;
import powercraft.api.PC_Struct3;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_ItemRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_RecipeRegistry;

public class PCma_ItemRanking {
	
	private static List<PC_Struct3<PC_ItemStack, Float, Integer>> ranking = new ArrayList<PC_Struct3<PC_ItemStack, Float, Integer>>();
	private static List<PC_ItemStack> alreadyDone = new ArrayList<PC_ItemStack>();
	private static boolean hasInit = false;
	
	private static void setRank(PC_ItemStack itemstack, int rank){
		PC_Struct3<PC_ItemStack, Float, Integer> s = get(itemstack);
		if(s==null){
			ranking.add(s = new PC_Struct3<PC_ItemStack, Float, Integer>(itemstack, 0.0f, 0));
		}
		s.b *= s.c;
		s.b += rank;
		s.c++;
		s.b /= s.c;
	}
	
	private static PC_Struct3<PC_ItemStack, Float, Integer> get(PC_ItemStack itemstack){
		for(PC_Struct3<PC_ItemStack, Float, Integer> s:ranking){
			if(s.a.equals(itemstack)){
				return s;
			}
		}
		return null;
	}
	
	public static float getRank(PC_ItemStack itemstack){
		PC_Struct3<PC_ItemStack, Float, Integer> s = get(itemstack);
		if(s!=null){
			return s.b;
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
                    if (b != null){
	                    if (b instanceof PC_IItemInfo){
	                        l = ((PC_IItemInfo)b).getItemStacks(new ArrayList<ItemStack>());
	                    }else{
	                    	if (item.getHasSubtypes()){
	                        	l = new ArrayList<ItemStack>();
	                        	boolean bo = false;
	                            for (int j = 0; true; j++){
	                                ItemStack is = new ItemStack(b, 1, j);
	                                if (PC_RecipeRegistry.getRecipesForProduct(is).size() > 0 || PC_RecipeRegistry.getFeedstock(is).size() > 0) {
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
                        if (PC_RecipeRegistry.getRecipesForProduct(is).size() > 0 || PC_RecipeRegistry.getFeedstock(is).size() > 0) {
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
					for(ItemStack is:l){
						PC_ItemStack pcis = new PC_ItemStack(is);
						if(alreadyDone.contains(pcis))
							continue;
						List<IRecipe> recipes = PC_RecipeRegistry.getRecipesForProduct(is);
						List<ItemStack> feedstocks = PC_RecipeRegistry.getFeedstock(is);
						if(recipes.size()==0&&feedstocks.size()==0){
							if(get(pcis)==null){
								setRank(pcis, 10000);
								anyDone = true;
							}
							if(!alreadyDone.contains(pcis))
								alreadyDone.add(pcis);
						}
						for(ItemStack itemstack:feedstocks){
							PC_ItemStack pcisi = new PC_ItemStack(itemstack);
							PC_Struct3<PC_ItemStack, Float, Integer> s = get(pcisi);
							if(s != null){
								setRank(pcis, (int)(Math.min(s.b+100, s.b*2)));
								anyDone = true;
								if(!alreadyDone.contains(pcis))
									alreadyDone.add(pcis);
							}
						}
						for(IRecipe recipe:recipes){
							List<PC_ItemStack>[][] input = PC_RecipeRegistry.getExpectedInput(recipe, -1, -1);
							if(input==null){
				                continue;
				            }
							float rank=0;
							for(int x=0; x<input.length; x++){
								for(int y=0; y<input[x].length; y++){
									List<PC_ItemStack> list = input[x][y];
									if(list==null || list.size()<=0)
										continue;
									float bestRank = -1;
									for(PC_ItemStack pcisi:list){
										PC_Struct3<PC_ItemStack, Float, Integer>s = get(pcisi);
										if(s!=null) {
											if(bestRank == -1 || bestRank>s.b)
												bestRank = s.b;
										}
									}
									if(bestRank == -1){
										rank = -1;
										break;
									}else{
										rank += bestRank;
									}
								}
							}
							if(rank>0){
								rank /= recipe.getRecipeOutput().stackSize;
								setRank(pcis, (int)(rank+2.5));
								anyDone = true;
								if(!alreadyDone.contains(pcis))
									alreadyDone.add(pcis);
							}
						}
					}
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
			Item item;
			if(key.startsWith("id.")){
				item = Item.itemsList[Integer.parseInt(key.substring(3))];
			}else{
				item = getItem(key);
			}
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
		prop.getString("id."+item.itemID, nums, item.getItemName());
	}
	
	private static void reg(PC_Property prop, Block block, String nums){
		prop.getString("id."+block.blockID, nums, Item.itemsList[block.blockID].getItemName());
	}
	
	public static void init(){
		if(hasInit)
			return;
		
		File file = new File(GameInfo.getPowerCraftFile(), "/itemRanks.cfg");
		
		PC_Property prop;
		
		try {
			prop = PC_Property.loadFromFile(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			prop = new PC_Property();
		}
		
		reg(prop, Block.bedrock, "0");
		reg(prop, Block.grass, "1");
		reg(prop, Block.dirt, "1");
		reg(prop, Block.cobblestone, "1");
		reg(prop, Block.stoneBrick, "1");
		reg(prop, Block.sand, "1");
		reg(prop, Block.gravel, "1");
		reg(prop, Block.oreGold, "1000");
		reg(prop, Block.oreIron, "500");
		reg(prop, Block.oreCoal, "250");
		reg(prop, Block.wood, "1, 1, 1, 1");
		reg(prop, Block.sponge, "1");
		reg(prop, Block.oreLapis, "500");
		reg(prop, Block.web, "1");
		reg(prop, Block.cobblestoneMossy, "500");
		reg(prop, Block.obsidian, "2000");
		reg(prop, Block.fire, "1");
		reg(prop, Block.mobSpawner, "4000");
		reg(prop, Block.oreDiamond, "2000");
		reg(prop, Block.crops, "1");
		reg(prop, Block.oreRedstone, "250");
		reg(prop, Block.snow, "1");
		reg(prop, Block.ice, "1");
		reg(prop, Block.cactus, "20");
		reg(prop, Block.pumpkin, "100");
		reg(prop, Block.netherrack, "1");
		reg(prop, Block.slowSand, "10");
		reg(prop, Block.vine, "1");
		reg(prop, Block.mycelium, "1");
		reg(prop, Block.waterlily, "1000");
		reg(prop, Block.netherBrick, "10");
		reg(prop, Block.netherStalk, "10");
		reg(prop, Block.cauldron, "1");
		reg(prop, Block.whiteStone, "100");
		reg(prop, Block.dragonEgg, "100000");
		reg(prop, Block.oreEmerald, "4000");
		reg(prop, Block.tripWire, "100");
		reg(prop, Block.commandBlock, "1000");
		reg(prop, Block.flowerPot, "10");
		reg(prop, Block.skull, "1");
		reg(prop, Item.appleRed, "10");
		reg(prop, Item.silk, "10");
		reg(prop, Item.feather, "1");
		reg(prop, Item.gunpowder, "100");
		reg(prop, Item.seeds, "10");
		reg(prop, Item.wheat, "10");
		reg(prop, Item.flint, "10");
		reg(prop, Item.porkRaw, "1");
		reg(prop, Item.bucketWater, "1000");
		reg(prop, Item.bucketLava, "10000");
		reg(prop, Item.saddle, "1000");
		reg(prop, Item.snowball, "10");
		reg(prop, Item.leather, "1");
		reg(prop, Item.bucketMilk, "1000");
		reg(prop, Item.clay, "1");
		reg(prop, Item.reed, "1");
		reg(prop, Item.slimeBall, "100");
		reg(prop, Item.egg, "10");
		reg(prop, Item.lightStoneDust, "100");
		reg(prop, Item.fishRaw, "100");
		reg(prop, Item.bone, "10");
		reg(prop, Item.melon, "10");
		reg(prop, Item.beefRaw, "10");
		reg(prop, Item.chickenRaw, "10");
		reg(prop, Item.rottenFlesh, "1");
		reg(prop, Item.enderPearl, "1000");
		reg(prop, Item.blazeRod, "10000");
		reg(prop, Item.ghastTear, "5000");
		reg(prop, Item.netherStalkSeeds, "1");
		reg(prop, Item.spiderEye, "10");
		reg(prop, Item.expBottle, "1000");
		reg(prop, Item.writtenBook, "1000");
		reg(prop, Item.carrot, "10");
		reg(prop, Item.potato, "10");
		reg(prop, Item.poisonousPotato, "1");
		reg(prop, Item.netherStar, "100000");
		reg(prop, Item.record13, "10000");
		reg(prop, Item.recordCat, "10000");
		reg(prop, Item.recordBlocks, "10000");
		reg(prop, Item.recordChirp, "10000");
		reg(prop, Item.recordFar, "10000");
		reg(prop, Item.recordMall, "10000");
		reg(prop, Item.recordMellohi, "10000");
		reg(prop, Item.recordStal, "10000");
		reg(prop, Item.recordStrad, "10000");
		reg(prop, Item.recordWard, "10000");
		reg(prop, Item.record11, "10000");
		reg(prop, Item.recordWait, "10000");
		
		TreeMap<String, PC_Block> blocks = PC_BlockRegistry.getPCBlocks();
		TreeMap<String, PC_Item> items = PC_ItemRegistry.getPCItems();
		TreeMap<String, PC_ItemArmor> itemArmors = PC_ItemRegistry.getPCItemArmors();
		
		for(PC_Block block:blocks.values()){
			Object o = block.msg(PC_MSGRegistry.MSG_RATING);
			if(o instanceof List){
				List<Integer> rating = (List<Integer>)o;
				String s = "";
				for(int i=0; i<rating.size(); i++){
					s += rating.get(i);
					if(i<rating.size()-1)
						s+=", ";
				}
				reg(prop, block, s);
			}
		}
		
		for(PC_Item item:items.values()){
			Object o = item.msg(PC_MSGRegistry.MSG_RATING);
			if(o instanceof List){
				List<Integer> rating = (List<Integer>)o;
				String s = "";
				for(int i=0; i<rating.size(); i++){
					s += rating.get(i);
					if(i<rating.size()-1)
						s+=", ";
				}
				reg(prop, item, s);
			}
		}
		
		for(PC_ItemArmor itemArmor:itemArmors.values()){
			Object o = itemArmor.msg(PC_MSGRegistry.MSG_RATING);
			if(o instanceof List){
				List<Integer> rating = (List<Integer>)o;
				String s = "";
				for(int i=0; i<rating.size(); i++){
					s += rating.get(i);
					if(i<rating.size()-1)
						s+=", ";
				}
				reg(prop, itemArmor, s);
			}
		}
		
		load("", prop);
		
		while(makeTree());
		
		try {
			prop.save(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		hasInit = true;
	}
	
}
