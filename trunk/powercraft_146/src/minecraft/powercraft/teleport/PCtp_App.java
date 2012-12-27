package powercraft.teleport;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;

public class PCtp_App implements PC_IModule {

	@Override
	public String getName() {
		return "Teleport";
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
