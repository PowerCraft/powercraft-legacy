package powercraft.management;

import java.util.HashMap;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PC_ShapedRecipes implements IRecipe, PC_IRecipeInputInfo {

	private int recipeWidth;
	private int recipeHeight;
	private PC_ItemStack[] recipeItems;
	private PC_ItemStack recipeOutput;
	private String op;
	
	public PC_ShapedRecipes(PC_ItemStack recipeOutput, int recipeWidth, int recipeHeight, PC_ItemStack[] recipeItems) {
		this.recipeOutput = recipeOutput;
		this.recipeWidth = recipeWidth;
		this.recipeHeight = recipeHeight;
		this.recipeItems = recipeItems;
	}
	
	public PC_ShapedRecipes(PC_ItemStack itemStack, Object... recipe) {
		this(null, itemStack, recipe);
	}
	
	public PC_ShapedRecipes(String op, PC_ItemStack itemStack, Object... recipe) {
		this.op = op;
		String recs = "";
		int i = 0;
		recipeOutput = itemStack;
		recipeWidth = 0;
		recipeHeight = 0;
		int length;

		while (recipe[i] instanceof String) {
			String s = (String) recipe[i++];
			recipeHeight++;
			recipeWidth = s.length();
			recs = recs + s;
		}

		HashMap<Character, PC_ItemStack> hm;

		for (hm = new HashMap<Character, PC_ItemStack>(); i < recipe.length; i += 2) {
			Character c = (Character) recipe[i];
			PC_ItemStack is = null;

			if (recipe[i + 1] instanceof Item) {
				is = new PC_ItemStack(recipe[i + 1]);
			} else if (recipe[i + 1] instanceof Block) {
				is = new PC_ItemStack(recipe[i + 1], 1, -1);
			} else if (recipe[i + 1] instanceof PC_ItemStack) {
				is = (PC_ItemStack) recipe[i + 1];
			}

			hm.put(c, is);
		}

		recipeItems = new PC_ItemStack[recipeWidth
				* recipeHeight];

		for (length = 0; length < recipeWidth * recipeHeight; ++length) {
			char c = recs.charAt(length);

			if (hm.containsKey(c)) {
				recipeItems[length] = hm.get(c).copy();
			} else {
				recipeItems[length] = null;
			}
		}
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
	
	public ItemStack getRecipeOutput() {
		if(!canBeCrafted())
			return null;
		return recipeOutput.toItemStack();
	}

	public boolean matches(InventoryCrafting inventoryCrafting, World world) {
		if(!canBeCrafted())
			return false;
		for (int j = 0; j <= 3 - this.recipeWidth; j++) {
			for (int i = 0; i <= 3 - this.recipeHeight; i++) {
				if (this.checkMatch(inventoryCrafting, j, i, true)) {
					return true;
				}

				if (this.checkMatch(inventoryCrafting, j, i, false)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkMatch(InventoryCrafting inventoryCrafting,
			int x, int y, boolean par4) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int xp = i - x;
				int yp = j - y;
				PC_ItemStack is = null;

				if (xp >= 0 && yp >= 0 && xp < this.recipeWidth
						&& yp < this.recipeHeight) {
					if (par4) {
						is = this.recipeItems[this.recipeWidth - xp - 1
								+ yp * this.recipeWidth];
					} else {
						is = this.recipeItems[xp + yp * this.recipeWidth];
					}
				}

				ItemStack var10 = inventoryCrafting.getStackInRowAndColumn(
						i, j);

				if (var10 != null || is != null) {
					if (var10 == null && is != null || var10 != null
							&& is == null) {
						return false;
					}

					if (!is.equals(var10)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		if(!canBeCrafted())
			return null;
		ItemStack itemStack = getRecipeOutput().copy();
		if (itemStack.getItem() instanceof PC_Item) {
			((PC_Item) itemStack.getItem()).doCrafting(itemStack,
					inventoryCrafting);
		}
		return itemStack;
	}

	public int getRecipeSize() {
		if(!canBeCrafted())
			return 0;
		return this.recipeWidth * this.recipeHeight;
	}

	@Override
	public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks) {
		if(!canBeCrafted())
			return null;
		for (PC_ItemStack itemStack : recipeItems) {
			if (itemStack != null) {
				itemStacks.add(itemStack.toItemStack());
			}
		}
		return itemStacks;
	}

}
