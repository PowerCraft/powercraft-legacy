package powercraft.mobile;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCmo_AppClient extends PCmo_App implements PC_IClientModule {

	@Override
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.miner.disconnected", "Miner %s disconnected from keyboard."));
		lang.add(new LangEntry("pc.miner.connected", "Miner %s connected to keyboard."));
		lang.add(new LangEntry("pc.miner.bridgeOn", "Bridge building enabled."));
		lang.add(new LangEntry("pc.miner.bridgeOff", "Bridge building disabled."));
		lang.add(new LangEntry("pc.miner.launchedAll", "Miners launched and disconnected."));
		lang.add(new LangEntry("pc.miner.miningOn", "Mining enabled."));
		lang.add(new LangEntry("pc.miner.miningOff", "Mining disabled."));
		lang.add(new LangEntry("pc.miner.operationsCancelled", "All operations cancelled."));
		lang.add(new LangEntry("pc.miner.build.errInvalidStructure", "Not a valid Miner's structure!"));
		lang.add(new LangEntry("pc.miner.build.errMissingCrystals", "Put some Power Crystals into the chest!"));
		
	
		lang.add(new LangEntry("pc.gui.miner.program", "Program"));
		lang.add(new LangEntry("pc.gui.miner.settings", "Settings"));
		lang.add(new LangEntry("pc.gui.miner.cargo", "Cargo"));
		lang.add(new LangEntry("pc.gui.miner.terminal", "Terminal"));
		
		lang.add(new LangEntry("pc.gui.miner.opt.mining", "Mining enabled"));
		lang.add(new LangEntry("pc.gui.miner.opt.bridge", "Bridge building"));
		lang.add(new LangEntry("pc.gui.miner.opt.lavaFill", "Lava filling"));
		lang.add(new LangEntry("pc.gui.miner.opt.waterFill", "Water filling"));
		lang.add(new LangEntry("pc.gui.miner.opt.airFill", "Air filling - tunnel mode"));
		lang.add(new LangEntry("pc.gui.miner.opt.keepFuel", "Keep all fuel"));
		lang.add(new LangEntry("pc.gui.miner.opt.torchPlacing", "Place torches"));
		lang.add(new LangEntry("pc.gui.miner.opt.torchesOnlyOnFloor", "Torches only on floor"));
		lang.add(new LangEntry("pc.gui.miner.opt.compress", "Compress items"));
		lang.add(new LangEntry("pc.gui.miner.opt.makeCobble", "Make cobble (water & lava)"));

		lang.add(new LangEntry("pc.gui.miner.program", "Program"));
		lang.add(new LangEntry("pc.gui.miner.noError", "no error"));
		lang.add(new LangEntry("pc.gui.miner.running", "running"));
		lang.add(new LangEntry("pc.gui.miner.crashed", "crashed"));
		lang.add(new LangEntry("pc.gui.miner.stoped", "stoped"));
		lang.add(new LangEntry("pc.gui.miner.launchProgram", "Launch"));
		lang.add(new LangEntry("pc.gui.miner.restartProgram", "Restart"));
		lang.add(new LangEntry("pc.gui.miner.stopProgram", "Stop"));
		
		lang.add(new LangEntry("pc.gui.miner.title", "Miner"));
		
		lang.add(new LangEntry("pc.miner.chestName", "Miner Cargo Storage"));
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTextureDirectory(this) + "miner_base.png");
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		if(PC_ModuleRegistry.getModule("Weasel")!=null){
			guis.add(new PC_Struct2<String, Class>("Miner", PCmo_GuiWeaselMiner.class));
		}else{
			guis.add(new PC_Struct2<String, Class>("Miner", PCmo_GuiMiner.class));
		}
		return guis;
	}
	
	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		list.add(new PC_Struct2<Class<? extends Entity>, Render>(PCmo_EntityMiner.class, new PCmo_RenderMiner()));
		return list;
	}

}
