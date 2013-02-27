package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.recipes.PC_ShapelessRecipes;
import powercraft.management.tick.PC_ITickHandler;

public class PCde_App implements PC_IModule {

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

	@Override
	public String getName() {
		return "Deco";
	}

	@Override
	public String getVersion() {
		return "1.0.3";
	}
	
    public void preInit(){}

    public void init(){}

    public void postInit(){}
	
	
	@Override
	public void initProperties(PC_Property config) {}
	
	@Override
	public List<PC_Struct2<Class<? extends Entity>, Integer>> initEntities(List<PC_Struct2<Class<? extends Entity>, Integer>> entities){
		return null;
	}
	
	@Override
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

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		return null;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		return null;
	}

}
