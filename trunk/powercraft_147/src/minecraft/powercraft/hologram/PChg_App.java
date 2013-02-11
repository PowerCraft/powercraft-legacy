package powercraft.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.recipes.PC_ShapelessRecipes;

public class PChg_App implements PC_IModule {

	public static PC_Block hologramBlockEmpty;
	public static PC_Block hologramBlock;
	public static PC_Block hologramField;
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
		return "1.0.0";
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
	public void initBlocks() {
		hologramBlockEmpty = ModuleLoader.register(this, PChg_BlockHologramBlockEmpty.class, PChg_ItemBlockHologramBlockEmpty.class);
		hologramBlock = ModuleLoader.register(this, PChg_BlockHologramBlock.class, PChg_ItemBlockHologramBlock.class, PChg_TileEntityHologramBlock.class);
		hologramField = ModuleLoader.register(this, PChg_BlockHologramField.class, PChg_TileEntityHologramField.class);
	}

	@Override
	public void initItems() {
		hologramGlasses = ModuleLoader.register(this, PChg_ItemArmorHologramGlasses.class);
	}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(hologramBlock), new PC_ItemStack(hologramBlockEmpty), getAllAccepptedBlocksForHologramBlock()));
		recipes.add(new PChg_HologramBackRecipe());
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
		for(int i=0; i<Block.blocksList.length; i++){
			if(Block.blocksList[i]!=null){
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
