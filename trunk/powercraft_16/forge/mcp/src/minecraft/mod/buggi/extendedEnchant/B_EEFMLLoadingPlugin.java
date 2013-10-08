package mod.buggi.extendedEnchant;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.6.2")
public class B_EEFMLLoadingPlugin implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		//This will return the name of the class "mod.buggi.extendedEnchant.B_EEClassTransformer"
		return new String[]{B_EEClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		//This is the name of our dummy container "mod.buggi.extendedEnchant.B_EEDummyContainer"
		return B_EEDummyContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

}
