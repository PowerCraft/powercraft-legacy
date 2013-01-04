package powercraft.mobile;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_IEntityRender;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCmo_AppClient extends PCmo_App implements PC_IClientModule, PC_IEntityRender {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Mobile")) + "miner_base.png");
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Miner", PCmo_GuiMiner.class));
		return guis;
	}
	
	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		list.add(new PC_Struct2<Class<? extends Entity>, Render>(PCmo_EntityMiner.class, new PCmo_RenderMiner()));
		return list;
	}

}
