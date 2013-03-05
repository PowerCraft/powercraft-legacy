package powercraft.net;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.launcher.PC_Module;
import powercraft.launcher.PC_Module.PC_InitDataHandlers;
import powercraft.launcher.PC_Module.PC_InitRecipes;
import powercraft.launcher.PC_Property;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;

@PC_Module(name="Net", version="1.1.0")
public class PCnt_App {

	@PC_FieldObject(clazz=PCnt_BlockSensor.class)
	public static PC_Block sensor;
	@PC_FieldObject(clazz=PCnt_BlockRadio.class)
	public static PC_Block radio;
	@PC_FieldObject(clazz=PCnt_ItemRadioRemote.class)
	public static PC_Item portableTx;
	@PC_FieldObject(clazz=PCnt_RadioManager.class)
	public static PCnt_RadioManager radioManager;

	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(sensor, 1, 1),
					"R", 
					"I", 
					"S",
						'I', Item.ingotIron, 'R', Item.redstone, 'S', Block.stone ));


		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(sensor, 1, 0),
					"R", 
					"I", 
					"W",
						'I', Item.ingotIron, 'R', Item.redstone, 'W', Block.planks ));

		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(sensor, 1, 2),
					"R", 
					"I", 
					"O",
						'I', Item.ingotIron, 'R', Item.redstone, 'O', Block.obsidian ));
		
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(radio,1,0),
					" I ", 
					"RIR", 
					"SSS",
						'I', Item.ingotGold, 'R', Item.redstone, 'S', Block.stone ));

		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(radio,1,1),
					" I ", 
					"RIR", 
					"SSS",
						'I', Item.ingotIron, 'R', Item.redstone, 'S', Block.stone ));

		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(portableTx),
					"T", 
					"B",
						'B', Block.stoneButton, 'T', radio ));
		return recipes;
	}

	@PC_InitDataHandlers
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Radio", radioManager));
		return dataHandlers;
	}
	
}
