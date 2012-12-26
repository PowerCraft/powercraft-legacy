package powercraft.net;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Item;
import powercraft.management.PC_Property;
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
		return "1.0.0";
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}

}
