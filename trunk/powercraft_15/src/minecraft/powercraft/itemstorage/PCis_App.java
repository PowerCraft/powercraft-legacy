package powercraft.itemstorage;

import java.util.List;

import net.minecraft.block.Block;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.network.PC_IPacketHandler;
import powercraft.api.recipes.PC_3DRecipe;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.utils.PC_Struct2;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitPacketHandlers;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_RegisterContainers;

@PC_Module(name="ItemStorage", version="1.1.0")
public class PCis_App {

	@PC_FieldObject(clazz=PCis_BlockBigChest.class)
	public static PC_Block bigChest;
	@PC_FieldObject(clazz=PCis_ItemCompressor.class)
	public static PC_Item compressor;
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_3DRecipe((PC_I3DRecipeHandler)bigChest, 
				new String[]{
				"g  g",
				"    ",
				"    ",
				"g  g"},
				new String[]{
				"f  f",
				"    ",
				"    ",
				"f  f"},
				new String[]{
				"f  f",
				"    ",
				"    ",
				"f  f"},
				new String[]{
				"g  g",
				"    ",
				"    ",
				"g  g"},
				'g', Block.glass, 'f', Block.fence, ' ', null));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.NORMAL),
				" l ",
				"lcl",
				" l ",
				'c', Block.chest, 'l', Block.lever));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.ENDERACCESS),
				" l ",
				"lel",
				" l ",
				'e', Block.enderChest, 'l', Block.lever));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.BIG),
				"lll",
				"ccc",
				"lll",
				'c', Block.chest, 'l', Block.lever));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.BIG),
				"ccc",
				'c', new PC_ItemStack(compressor, 1, PCis_ItemCompressor.NORMAL)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.HEIGHT),
				"lcl",
				"lcl",
				"lcl",
				'c', Block.chest, 'l', Block.lever));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.HEIGHT),
				"c",
				"c",
				"c",
				'c', new PC_ItemStack(compressor, 1, PCis_ItemCompressor.NORMAL)));
		
		return recipes;
	}

	@PC_InitPacketHandlers
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("ItemCompressor", (PC_IPacketHandler)compressor));
		return packetHandlers;
	}

	@PC_RegisterContainers
	public List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> registerContainers(List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>("Compressor", PCis_ContainerCompressor.class));
		return guis;
	}

}
