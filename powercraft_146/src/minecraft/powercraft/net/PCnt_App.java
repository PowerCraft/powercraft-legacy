package powercraft.net;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.src.ModLoader;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCnt_App implements PC_IModule {

	public static PC_Block sensor;
	public static PC_Block radio;
	public static PC_Item portableTx;

	@Override
	public String getName() {
		return "Net";
	}

	@Override
	public String getVersion() {
		return "1.0.2";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {
		ModuleLoader.regsterDataHandler("Radio", new PCnt_RadioManager());
	}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initBlocks() {
		sensor = ModuleLoader.register(this, PCnt_BlockSensor.class, PCnt_ItemBlockSensor.class, PCnt_TileEntitySensor.class);
		radio = ModuleLoader.register(this, PCnt_BlockRadio.class, PCnt_ItemBlockRadio.class, PCnt_TileEntityRadio.class);
	}

	@Override
	public void initItems() {
		portableTx = ModuleLoader.register(this, PCnt_ItemRadioRemote.class);
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
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

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}

}
