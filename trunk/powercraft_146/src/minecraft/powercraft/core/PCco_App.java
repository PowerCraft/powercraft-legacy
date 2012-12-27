package powercraft.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_ShapelessRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;

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
		return "1.0.1";
	}
	
	@Override
	public void preInit() {}

	@Override
	public void init() {
		PC_PacketHandler.registerPackethandler("DeleteAllPlayerStacks", new PCco_DeleteAllPlayerStacks());
		PCco_MobSpawnerSetter spawnerSetter = new PCco_MobSpawnerSetter();
		PC_PacketHandler.registerPackethandler("MobSpawner", spawnerSetter);
		ModuleInfo.registerMSGObject(spawnerSetter);
	}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		PC_GlobalVariables.consts.put("recipes.recyclation", config.getBoolean("recipes.recyclation", true, "Add new recypes allowing easy material recyclation"));
		PC_GlobalVariables.consts.put("recipes.spawner", config.getBoolean("recipes.spawner", true, "Make spawners craftable of iron and mossy cobble"));
	}

	@Override
	public void initBlocks() {
		powerCrystal = ModuleLoader.register(this, PCco_BlockPowerCrystal.class, PCco_ItemBlockPowerCrystal.class);
	}

	@Override
	public void initItems() {
		powerDust = ModuleLoader.register(this, PCco_ItemPowerDust.class);
		activator = ModuleLoader.register(this, PCco_ItemActivator.class);
		blockSaver = ModuleLoader.register(this, PCco_ItemBlockSaver.class);
		craftingTool = ModuleLoader.register(this, PCco_ItemCraftingTool.class);
		oreSniffer = ModuleLoader.register(this, PCco_ItemOreSniffer.class);
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

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("CraftingTool", PCco_ContainerCraftingTool.class));
		return guis;
	}

}
