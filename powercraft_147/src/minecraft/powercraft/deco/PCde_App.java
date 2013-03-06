package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.PC_Module;
import powercraft.launcher.PC_Module.PC_InitRecipes;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.recipes.PC_ShapelessRecipes;

@PC_Module(name="Deco", version="1.1.0")
public class PCde_App {

	@PC_FieldObject(clazz=PCde_BlockRedstoneStorage.class)
	public static PC_Block redstoneStorage;
	@PC_FieldObject(clazz=PCde_BlockIronFrame.class)
	public static PC_Block ironFrame;
	@PC_FieldObject(clazz=PCde_BlockChimney.class)
	public static PC_Block chimney;
	@PC_FieldObject(clazz=PCde_BlockPlatform.class)
	public static PC_Block platform;
	@PC_FieldObject(clazz=PCde_BlockStairs.class)
	public static PC_Block stairs;
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(ironFrame, 32, 0),
					"XXX", 
					"X X", 
					"XXX",
						'X', Item.ingotIron));	
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(redstoneStorage, 1),
					"XXX", 
					"XXX", 
					"XXX",
						'X', Item.redstone));
		
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(Item.redstone, 9, 0),
				new PC_ItemStack(redstoneStorage)));
		
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(platform, 15),
					"X  ", 
					"X  ", 
					"XXX",
						'X', Item.ingotIron));	
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(stairs, 15),
					"X  ", 
					"XX ", 
					" XX",
						'X', Item.ingotIron));			
		
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(Item.ingotIron),
				new PC_ItemStack(platform, 1), new PC_ItemStack(platform, 1), new PC_ItemStack(platform, 1)));
		
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(Item.ingotIron),
				new PC_ItemStack(stairs, 1),new PC_ItemStack(stairs, 1),new PC_ItemStack(stairs, 1)));
		
		// chimneys
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,0),
				"X X", 
				"X X", 
				"X X", 
					'X', Block.cobblestone));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,1),
				"X X", 
				"X X", 
				"X X", 
					'X', Block.brick));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,2),
				"X X", 
				"X X", 
				"X X", 
					'X', Block.stoneBrick));
		return recipes;
	}

}
