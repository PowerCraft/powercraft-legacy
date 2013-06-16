package powercraft.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderBlocks;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.network.PC_ClientPacketHandler;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_OverlayRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_ClientRenderer;
import powercraft.api.renderer.PC_IOverlayRenderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_Struct2;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_ClientModule
public class PC_APIClientModule extends PC_APIModule {
	
	private PC_ClientRenderer cr1;
	
	@Override
	protected void initVars() {
		PC_ClientUtils.create();
		packetHandler = new PC_ClientPacketHandler();
	}
	
	@Override
	protected void clientPreInit(List<PC_ModuleObject> modules) {
		PC_Logger.enterSection("Module Language Init");
		new PC_ThreadLangUpdates();
		for (PC_ModuleObject module : modules) {
			List<LangEntry> l = module.initLanguage(new ArrayList<LangEntry>());
			if (l != null) {
				PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
			}
		}
		ModLoader.addLocalization("pc.gui.mods", "en_US", "Mods");
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Texture Init");
		for (PC_ModuleObject module : modules) {
			List<String> l = module.loadTextureFiles(new ArrayList<String>());
			if (l != null) {
				for (String file : l) {
					PC_TextureRegistry.registerTexture(PC_TextureRegistry.getPowerCraftImageDir() + PC_TextureRegistry.getTextureName(module, file));
				}
			}
		}
		PC_TextureRegistry.registerTexture(PC_TextureRegistry.getGresImgDir() + "button.png");
		PC_TextureRegistry.registerTexture(PC_TextureRegistry.getGresImgDir() + "dialog.png");
		PC_TextureRegistry.registerTexture(PC_TextureRegistry.getGresImgDir() + "frame.png");
		PC_TextureRegistry.registerTexture(PC_TextureRegistry.getGresImgDir() + "scrollbar_handle.png");
		PC_TextureRegistry.registerTexture(PC_TextureRegistry.getGresImgDir() + "widgets.png");
		PC_Logger.exitSection();
	}
	
	@Override
	protected ModuleFieldInit getModuleFieldInit(PC_ModuleObject module) {
		return new ClientModuleFieldInit(module);
	}
	
	@Override
	protected void clientInit(List<PC_ModuleObject> modules) {
		cr1 = new PC_ClientRenderer();
		PC_Logger.enterSection("Module Gui Init");
		for (PC_ModuleObject module : modules) {
			List<PC_Struct2<String, Class<PC_IGresClient>>> l = module.registerGuis(new ArrayList<PC_Struct2<String, Class<PC_IGresClient>>>());
			if (l != null) {
				for (PC_Struct2<String, Class<PC_IGresClient>> g : l) {
					PC_GresRegistry.registerGresGui(g.a, g.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Splashes Init");
		for (PC_ModuleObject module : modules) {
			List<String> l = module.addSplashes(new ArrayList<String>());
			if (l != null) {
				PC_GlobalVariables.splashes.addAll(l);
			}
		}
		
		PC_GlobalVariables.splashes.add("GRES");
		
		for (int i = 0; i < 10; i++) {
			PC_GlobalVariables.splashes.add("Modded by MightyPork!");
		}
		
		for (int i = 0; i < 6; i++) {
			PC_GlobalVariables.splashes.add("Modded by XOR!");
		}
		
		for (int i = 0; i < 5; i++) {
			PC_GlobalVariables.splashes.add("Modded by Rapus95!");
		}
		
		for (int i = 0; i < 4; i++) {
			PC_GlobalVariables.splashes.add("Reviewed by RxD");
		}
		
		PC_GlobalVariables.splashes.add("Modded by masters!");
		
		for (int i = 0; i < 3; i++) {
			PC_GlobalVariables.splashes.add("PowerCraft " + PC_LauncherUtils.getPowerCraftVersion());
		}
		
		PC_GlobalVariables.splashes.add("Null Pointers included!");
		PC_GlobalVariables.splashes.add("ArrayIndexOutOfBoundsException");
		PC_GlobalVariables.splashes.add("Null Pointer loves you!");
		PC_GlobalVariables.splashes.add("Unstable!");
		PC_GlobalVariables.splashes.add("Buggy code!");
		PC_GlobalVariables.splashes.add("Break it down!");
		PC_GlobalVariables.splashes.add("Addictive!");
		PC_GlobalVariables.splashes.add("Earth is flat!");
		PC_GlobalVariables.splashes.add("Faster than Atari!");
		PC_GlobalVariables.splashes.add("DAFUQ??");
		PC_GlobalVariables.splashes.add("LWJGL");
		PC_GlobalVariables.splashes.add("Don't press the button!");
		PC_GlobalVariables.splashes.add("Press the button!");
		PC_GlobalVariables.splashes.add("Ssssssssssssssss!");
		PC_GlobalVariables.splashes.add("C'mon!");
		PC_GlobalVariables.splashes.add("Redstone Wizzard!");
		PC_GlobalVariables.splashes.add("Keep your mods up-to-date!");
		PC_GlobalVariables.splashes.add("Read the changelog!");
		PC_GlobalVariables.splashes.add("Read the log files!");
		PC_GlobalVariables.splashes.add("Discoworld!");
		PC_GlobalVariables.splashes.add("Also try ICE AGE mod!");
		PC_GlobalVariables.splashes.add("Also try Backpack mod!");
		
		PC_Logger.exitSection();
	}
	
	@Override
	protected void clientPostInit(List<PC_ModuleObject> modules) {
		PC_Logger.enterSection("Module Language Saving");
		for (PC_ModuleObject module : modules) {
			PC_LangRegistry.saveLanguage(module);
		}
		PC_Logger.exitSection();
	}
	
	@Override
	public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId) {
		if (modelId == PC_ClientRenderer.getRendererID()) {
			return cr1.renderWorldBlock(blockAccess, x, y, z, block, modelId, renderer);
		}
		return false;
	}
	
	@Override
	public void renderInvBlock(RenderBlocks renderer, Block block, int metadata, int modelId) {
		if (modelId == PC_ClientRenderer.getRendererID()) {
			cr1.renderInventoryBlock(block, metadata, modelId, renderer);
		} 
	}
	
	private static class ClientModuleFieldInit extends ModuleFieldInit {
		
		public ClientModuleFieldInit(PC_ModuleObject module) {
			super(module);
		}
		
		@Override
		protected void registerObject(Object object) {
			super.registerObject(object);
			if (object instanceof PC_IOverlayRenderer) {
				PC_OverlayRegistry.register((PC_IOverlayRenderer) object);
			}
		}
		
	}
	
}
