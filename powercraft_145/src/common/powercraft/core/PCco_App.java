package powercraft.core;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_Configuration;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_ShapelessRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;

public class PCco_App implements PC_IModule {

	public static PC_Block powerCrystal;
	public static PC_Item powerDust;
	public static PC_Item activator;
	public static PC_Item blockSaver;
	public static PC_Item craftingTool;
	public static PC_Item oreSniffer;
	
	@Override
	public String getName() {
		return "Core";
	}

	@Override
	public String getVersion() {
		return "1.0AlphaA";
	}
	
	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Configuration config) {}

	@Override
	public void initBlocks() {
		powerCrystal = PC_Utils.register(this, PCco_BlockPowerCrystal.class, PCco_ItemBlockPowerCrystal.class);
	}

	@Override
	public void initItems() {
		powerDust = PC_Utils.register(this, PCco_ItemPowerDust.class);
		activator = PC_Utils.register(this, PCco_ItemActivator.class);
		blockSaver = PC_Utils.register(this, PCco_ItemBlockSaver.class);
		craftingTool = PC_Utils.register(this, PCco_ItemCraftingTool.class);
		oreSniffer = PC_Utils.register(this, PCco_ItemOreSniffer.class);
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(craftingTool), 
				" r ",
                "rIr",
                " r ",
                'r', Item.redstone, 'I', Block.blockSteel));
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(powerDust, 24, 0), new PC_ItemStack(powerCrystal, 1, -1) ));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(oreSniffer),
                    " G ",
                    "GCG",
                    " G ",
                    'C', new PC_ItemStack(powerCrystal, 1, -1), 'G', Item.ingotGold));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(activator, 1, 0),
                    "C",
                    "I",
                    'C', new PC_ItemStack(powerCrystal, 1, -1), 'I', Item.ingotIron));
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("CraftingTool", PCco_ContainerCraftingTool.class));
		return guis;
	}

}
