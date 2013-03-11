package powercraft.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitPacketHandlers;
import powercraft.launcher.loader.PC_Module.PC_InitProperties;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.launcher.PC_Property;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_IPacketHandler;
import powercraft.api.PC_Struct2;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.recipes.PC_ShapelessRecipes;

@PC_Module(name="Core", version="1.1.0")
public class PCco_App {

	@PC_FieldObject(clazz=PCco_BlockPowerCrystal.class)
	public static PC_Block powerCrystal;
	@PC_FieldObject(clazz=PCco_ItemPowerDust.class)
	public static PC_Item powerDust;
	@PC_FieldObject(clazz=PCco_ItemActivator.class)
	public static PC_Item activator;
	@PC_FieldObject(clazz=PCco_ItemBlockSaver.class)
	public static PC_Item blockSaver;
	@PC_FieldObject(clazz=PCco_ItemCraftingTool.class)
	public static PC_Item craftingTool;
	@PC_FieldObject(clazz=PCco_ItemOreSniffer.class)
	public static PC_Item oreSniffer;
	@PC_FieldObject(clazz=PCco_MobSpawnerSetter.class)
	public static PCco_MobSpawnerSetter spawnerSetter;
	@PC_FieldObject(clazz=PCco_CraftingToolLoader.class)
	public static PCco_CraftingToolLoader craftingToolLoader;

	@PC_InitProperties
	public void initProperties(PC_Property config) {
		PC_GlobalVariables.consts.put("recipes.recyclation", config.getBoolean("recipes.recyclation", true, "Add new recypes allowing easy material recyclation"));
		PC_GlobalVariables.consts.put("recipes.spawner", config.getBoolean("recipes.spawner", true, "Make spawners craftable of iron and mossy cobble"));
	}
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
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
		
		
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.sand, 4), new PC_ItemStack(Block.sandStone, 1, -1)));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 6), Item.doorWood));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 8), Block.chest));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 4), Block.workbench));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 2), Block.pressurePlatePlanks));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.stone, 2), Block.pressurePlateStone));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.stone, 2), Block.stoneButton));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.stick, 3), Block.fence));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.stick, 2), Block.ladder));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 6), Item.sign));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.ingotIron, 6), Item.doorSteel));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.cobblestone, 8), Block.stoneOvenIdle));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.ingotIron, 5), Item.minecartEmpty));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.ingotIron, 3), Item.bucketEmpty));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 5), Item.boat));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.stick, 3), Block.fence));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.stick, 8), Block.fenceGate));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.stick, 7), Block.ladder, Block.ladder));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.stone), new PC_ItemStack(Block.stoneBrick, 1, -1)));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Item.ingotIron, 7), new PC_ItemStack(Item.cauldron, 1, -1)));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 3), Block.trapdoor));
		recipes.add(new PC_ShapelessRecipes("recipes.recyclation", new PC_ItemStack(Block.planks, 1), Item.stick, Item.stick));
		
		recipes.add(new PC_ShapedRecipes("recipes.spawner", new PC_ItemStack(Block.mobSpawner, 1),
					"SIS", 
					"I I", 
					"SIS", 
					'I', Item.ingotIron, 'S', Block.cobblestoneMossy));
		
		return recipes;
	}
	
	@PC_InitPacketHandlers
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("DeleteAllPlayerStacks", new PCco_DeleteAllPlayerStacks()));
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("MobSpawner", spawnerSetter));
		
		return packetHandlers;
	}
	
	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("CraftingTool", PCco_ContainerCraftingTool.class));
		return guis;
	}

}
