package powercraft.management;

import java.util.List;

public interface PC_IModule {

	public String getName();

	public String getVersion();
	
	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public void initProperties(PC_Property config);
	public void initBlocks();
	public void initItems();
	public void initEntities();
	
	public List<Object> initRecipes(List<Object> recipes);
	
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(List<PC_Struct2<String, PC_IDataHandler>> dataHandlers);
	
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects);
	
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers);
	
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis);
	
}
