package powercraft.checkpoints;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.api.PC_Struct2;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;

@PC_Module(name="Checkpoints", version="1.1.0")
public class PCcp_App {

	@PC_FieldObject(clazz=PCcp_BlockCheckpoint.class)
	public static PC_Block checkpoint;
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(checkpoint), 
				"b",
				"e",
				'b', Block.stoneButton, Block.woodenButton, 'e', Item.bed));
		return recipes;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Checkpoint", PCcp_ContainerCheckpoint.class));
		return guis;
	}

}
