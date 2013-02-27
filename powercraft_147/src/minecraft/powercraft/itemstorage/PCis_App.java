package powercraft.itemstorage;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_I3DRecipeHandler;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.tick.PC_ITickHandler;

public class PCis_App implements PC_IModule {

	@PC_FieldObject(clazz=PCis_BlockBigChest.class)
	public static PC_Block bigChest;
	@PC_FieldObject(clazz=PCis_ItemCompressor.class)
	public static PC_Item compressor;
	
	@Override
	public String getName() {
		return "Itemstorage";
	}

	@Override
	public String getVersion() {
		return "1.0.1";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Integer>> initEntities(List<PC_Struct2<Class<? extends Entity>, Integer>> entities){
		return null;
	}

	@Override
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
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.HIGHT),
				"lcl",
				"lcl",
				"lcl",
				'c', Block.chest, 'l', Block.lever));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(compressor, 1, PCis_ItemCompressor.HIGHT),
				"c",
				"c",
				"c",
				'c', new PC_ItemStack(compressor, 1, PCis_ItemCompressor.NORMAL)));
		
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("ItemCompressor", (PC_IPacketHandler)compressor));
		return packetHandlers;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Compressor", PCis_ContainerCompressor.class));
		return guis;
	}

}
