package powercraft.management;

import java.util.List;

import net.minecraft.src.IRecipe;

public interface PC_IModule {

	public String getName();

	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public void initProperties(PC_Configuration config);
	public void initBlocks();
	public void initItems();
	public List<IRecipe> initRecipes(List<IRecipe> recipes);
	
}
