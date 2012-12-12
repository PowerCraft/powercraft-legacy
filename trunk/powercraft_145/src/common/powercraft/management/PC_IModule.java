package powercraft.management;

import java.util.List;

import net.minecraft.src.IRecipe;

public interface PC_IModule {

	public String getName();

	public String getVersion();
	
	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public void initProperties(PC_Property config);
	public void initBlocks();
	public void initItems();
	public List<IRecipe> initRecipes(List<IRecipe> recipes);
	
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis);
	
}
