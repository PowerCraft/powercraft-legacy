package powercraft.management;

import java.util.List;

import net.minecraft.src.Entity;
import powercraft.management.recipes.PC_IRecipe;

public interface PC_IModule {

	public String getName();

	public String getVersion();
	
	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public void initProperties(PC_Property config);
	public List<Class<? extends Entity>> initEntities(List<Class<? extends Entity>> entities);
	
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes);
	
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(List<PC_Struct2<String, PC_IDataHandler>> dataHandlers);
	
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects);
	
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers);
	
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis);
	
}
