package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ModuleInfo;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class PC_ClientProxy extends PC_CommonProxy {

	@Override
	public void initUtils() {
		PC_ClientUtils.create();
	}
	
	@Override
	public void hack() {
		PC_ClientHacks.hackServer();
	}
	
	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(true));
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(false));
		TickRegistry.registerTickHandler(new PC_MainMenuHacks(), Side.CLIENT);
		TickRegistry.registerTickHandler(new PC_TickHandler(), Side.CLIENT);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityFanFX.class);
		PC_ClientUtils.registerEnitiyFX(EntitySmokeFX.class);
		NetworkRegistry.instance().registerConnectionHandler(new PC_ConnectionHandler());
		Gres.registerGres("UpdateNotification", PC_GuiUpdateNotification.class);
		
		PC_Logger.enterSection("Register EntityRender");
		List<PC_IModule> modules = ModuleInfo.getModules();
		for(PC_IModule module:modules){
			if(module instanceof PC_IClientModule){
				List<PC_Struct2<Class<? extends Entity>, Render>> list = ((PC_IClientModule) module).registerEntityRender(new ArrayList<PC_Struct2<Class<? extends Entity>, Render>>());
				if(list!=null){
					for(PC_Struct2<Class<? extends Entity>, Render> s:list){
						RenderingRegistry.registerEntityRenderingHandler(s.a, s.b);
					}
				}
			}
		}
		PC_Logger.exitSection();
	}
	
}
