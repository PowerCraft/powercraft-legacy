package powercraft.api;


import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_IBlock;
import powercraft.api.blocks.PC_TileEntity;
import powercraft.api.items.PC_Item;
import powercraft.api.items.PC_ItemInfo;
import cpw.mods.fml.common.registry.GameRegistry;


public class PC_Registry {

	private static PC_Module activeModule;


	public static Block registerBlock(PC_Module module, Class<? extends Block> clazz) {

		activeModule = module;
		Configuration config = module.getConfig();
		PC_BlockInfo blockInfo = clazz.getAnnotation(PC_BlockInfo.class);
		int blockID = config.getBlock(blockInfo.blockid(), blockInfo.defaultid()).getInt();
		if (blockID == -1) {
			PC_Logger.info("Block %s disabled", blockInfo.name());
			return null;
		}
		try {
			Block block = clazz.getConstructor(int.class).newInstance(blockID);
			GameRegistry.registerBlock(block, blockInfo.itemBlock(), blockInfo.blockid());
			Class<? extends PC_TileEntity> tileEntity = blockInfo.tileEntity();
			if (tileEntity != PC_TileEntity.class) {
				GameRegistry.registerTileEntity(tileEntity, tileEntity.getName());
			}
			activeModule = null;
			return block;
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to generate block %s", blockInfo.name());
		}
		activeModule = null;
		return null;
	}


	public static Item registerItem(PC_Module module, Class<? extends Item> clazz) {

		activeModule = module;
		Configuration config = module.getConfig();
		PC_ItemInfo itemInfo = clazz.getAnnotation(PC_ItemInfo.class);
		int itemID = config.getItem(itemInfo.itemid(), itemInfo.defaultid()).getInt();
		if (itemID == -1) {
			PC_Logger.info("Item %s disabled", itemInfo.name());
			return null;
		}
		try {
			Item item = clazz.getConstructor(int.class).newInstance(itemID);
			GameRegistry.registerItem(item, itemInfo.itemid());
			activeModule = null;
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to generate item %s", itemInfo.name());
		}
		activeModule = null;
		return null;
	}


	public static void registerRecipes(PC_Module module, Object object) {

		activeModule = module;
		if (object instanceof PC_IBlock) {
			((PC_IBlock) object).registerRecipes();
		} else if (object instanceof PC_Item) {
			((PC_Item) object).registerRecipes();
		}
		activeModule = null;
	}


	public static PC_Module getActiveModule() {

		return activeModule;
	}

	public static enum PC_RecipeTypes {
		SHAPED, SHAPELESS, RECIPE3D, SMELTING;
	}


	public static void addRecipe(PC_RecipeTypes recipeType, Object... obj) {

		switch (recipeType) {
			case RECIPE3D:
				break;
			case SHAPED:
				addShapedRecipe(obj);
				break;
			case SHAPELESS:
				addShapelessRecipe(obj);
				break;
			case SMELTING:
				addSmelting(obj);
				break;
			default:
				PC_Logger.severe("Unknown recipe type %s", recipeType);
				break;
		}
	}


	private static void addShapedRecipe(Object... obj) {

		ItemStack output = PC_Utils.getItemStack(obj);
		CraftingManager.getInstance().addRecipe(output, Arrays.copyOfRange(obj, 1, obj.length));
	}


	private static void addShapelessRecipe(Object... obj) {

		ItemStack output = PC_Utils.getItemStack(obj);
		CraftingManager.getInstance().addShapelessRecipe(output, Arrays.copyOfRange(obj, 1, obj.length));
	}


	private static void addSmelting(Object... obj) {

		ItemStack output = PC_Utils.getItemStack(obj);
		int i = 1;
		while (i < obj.length) {
			ItemStack input = PC_Utils.getItemStack(obj[i]);
			float experience = 0;
			i++;
			if (i < obj.length && obj[i] instanceof Float) {
				experience = (Float) obj[i];
				i++;
			}
			FurnaceRecipes.smelting().addSmelting(input.itemID, input.getItemDamage(), output, experience);
		}
	}

}
