package powercraft.api.registries;

import net.minecraft.util.Icon;
import powercraft.core.PC_Core;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class PC_SidedRegistryClient extends PC_SidedRegistry {
	
	protected PC_SidedRegistryClient(){}

	@Override
	protected void registerLanguage(String key, String value) {
		LanguageRegistry.instance().addStringLocalization(key, value);
	}

	@Override
	protected Icon registerIcon(String icon, String objectName) {

		if (icon.equals("DefaultMaschineTexture")) {
			return PC_TextureRegistry.iconRegistry.registerIcon(PC_Core.instance.getMetadata().modId + ":maschineDefault/DefaultMaschineTexture");
		}
		return PC_TextureRegistry.iconRegistry.registerIcon(PC_ModuleRegistry.getActiveModule().getMetadata().modId + ":" + objectName + "/" + icon);
	}
	
}
