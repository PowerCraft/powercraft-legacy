package powercraft.backpacks;

import java.util.List;

import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Item;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCbp_App implements PC_IModule {

	public static PC_Item backpack;
	
	@Override
	public String getName() {
		return "Backpacks";
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
    }

	@Override
	public void initItems() {
		backpack = ModuleLoader.register(this, PCbp_ItemBackpack.class);
	}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
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
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Backpack", PCbp_ContainerBackpack.class));
		return guis;
	}

}
