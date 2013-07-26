package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.recipes.PC_ShapelessRecipes;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_Instance;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_Module(name="Deco", version="1.1.1")
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
	@PC_Instance
	public static PC_ModuleObject instance;
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(ironFrame, 32, 0),
					"XXX", 
					"X X", 
					"XXX",
						'X', Item.ingotIron));	
		
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
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,3),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.stoneBrick, 1, 2)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,4),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.stoneBrick, 1, 3)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,5),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.sandStone, 1, 0)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,6),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.sandStone, 1, 1)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,7),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.sandStone, 1, 2)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,8),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.netherBrick, 1, 0)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,9),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.blockNetherQuartz, 1, 0)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,10),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.blockNetherQuartz, 1, 1)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,11),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.blockNetherQuartz, 1, 2)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,12),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.cobblestoneMossy, 1, 0)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,13),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.stoneBrick, 1, 1)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,14),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.blockClay, 1, 0)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(chimney,6,15),
				"X X", 
				"X X", 
				"X X", 
					'X', new PC_ItemStack(Block.blockIron, 1, 0)));
		
		return recipes;
	}

}
