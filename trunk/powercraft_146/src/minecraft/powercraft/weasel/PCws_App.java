package powercraft.weasel;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCws_App implements PC_IModule {

	@Override
	public String getName() {
		return "Weasel";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {
		ModuleLoader.regsterDataHandler("Weasel", new PCws_WeaselManager());
	}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		
	}

	@Override
	public void initBlocks() {
		
	}

	@Override
	public void initItems() {
		
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		return null;
	}

}
