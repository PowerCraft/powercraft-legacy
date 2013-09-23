package powercraft.api.registries;

import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_Utils;
import powercraft.api.security.PC_Security;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PC_Registry {

	protected static PC_SidedRegistry sidedRegistry; 
	
	@SideOnly(Side.CLIENT)
	public static void createClientRegistry() {
		if(PC_Security.allowedCaller("PC_Registry.createClientRegistry()", PC_ClientUtils.class) && sidedRegistry==null){
			sidedRegistry = new PC_SidedRegistryClient();
		}
	}
	
	public static void createServerRegistry() {
		if(PC_Security.allowedCaller("PC_Registry.createServerRegistry()", PC_Utils.class) && sidedRegistry==null){
			sidedRegistry = new PC_SidedRegistry();
		}
	}
	
}
