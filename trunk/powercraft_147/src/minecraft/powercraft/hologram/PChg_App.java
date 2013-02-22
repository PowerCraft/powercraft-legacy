package powercraft.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.recipes.PC_ShapelessRecipes;

public class PChg_App implements PC_IModule {

	@PC_FieldObject(clazz=PChg_BlockHologramBlockEmpty.class)
	public static PC_Block hologramBlockEmpty;
	@PC_FieldObject(clazz=PChg_BlockHologramBlock.class)
	public static PC_Block hologramBlock;
	@PC_FieldObject(clazz=PChg_BlockHologramField.class)
	public static PC_Block hologramField;
	@PC_FieldObject(clazz=PChg_ItemArmorHologramGlasses.class)
	public static PC_ItemArmor hologramGlasses;
	private static PChg_App instance;
	
	public static PChg_App getInstance(){
		return instance;
	}
	
	public PChg_App(){
		instance = this;
	}
	
	@Override
	public String getName() {
		return "Hologram";
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
	public List<Class<? extends Entity>> initEntities(List<Class<? extends Entity>> entities) {
		return null;
	}

	@Override
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(hologramBlock), new PC_ItemStack(hologramBlockEmpty), getAllAccepptedBlocksForHologramBlock()));
		recipes.add(new PChg_HologramBackRecipe());
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(hologramBlockEmpty), 
				" p ",
				"gcg",
				"ggg",
				'g', Block.glass, 'c', Block.chest, 'p', new PC_ItemStack(ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal"), 1, -1)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(hologramField), 
				"ggg",
				"hhh",
				"ioi",
				'i', Item.ingotIron, 'g', Block.glass, 'h', new PC_ItemStack(hologramBlockEmpty), 'o', new PC_ItemStack(ModuleInfo.getPCItemByName("PCco_ItemOreSniffer"))));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(hologramGlasses), 
				"i i",
				"ghg",
				'i', Item.ingotIron, 'g', Block.thinGlass, 'h', new PC_ItemStack(hologramField)));
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		return null;
	}

	public List<PC_ItemStack> getAllAccepptedBlocksForHologramBlock(){
		List<PC_ItemStack>l = new ArrayList<PC_ItemStack>();
		for(int i=1; i<Block.blocksList.length; i++){
			if(Block.blocksList[i]!=null && Item.itemsList[i]!=null){
				if(Block.blocksList[i].renderAsNormalBlock()){
					l.add(new PC_ItemStack(Block.blocksList[i], 1, -1));
				}
			}
		}
		return l;
	}
	
	public void renderHologramField(PChg_TileEntityHologramField te, double x, double y, double z){
		
	}
	
}
