package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.PC_ModuleObject;
import powercraft.management.entity.PC_EntityFanFX;
import powercraft.management.entity.PC_EntityLaserFX;
import powercraft.management.entity.PC_EntityLaserParticleFX;
import powercraft.management.hacks.PC_ClientHacks;
import powercraft.management.hacks.PC_MainMenuHacks;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.renderer.PC_ClientRenderer;
import powercraft.management.tick.PC_ClientTickHandler;
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
		PC_ClientHacks.hackClient();
	}
	
	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(true));
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(false));
		TickRegistry.registerTickHandler(new PC_MainMenuHacks(), Side.CLIENT);
		TickRegistry.registerTickHandler(new PC_ClientTickHandler(), Side.CLIENT);
		TickRegistry.registerTickHandler(new PC_ClientTickHandler(), Side.SERVER);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityFanFX.class);
		PC_ClientUtils.registerEnitiyFX("EntitySmokeFX", EntitySmokeFX.class);
		NetworkRegistry.instance().registerConnectionHandler(new PC_ConnectionHandler());
		
		PC_Logger.enterSection("Register EntityRender");
		List<PC_ModuleObject> modules = PC_ModuleRegistry.getModuleList();
		for(PC_ModuleObject module:modules){
			List<PC_Struct2<Class<? extends Entity>, Render>> list = module.registerEntityRender(new ArrayList<PC_Struct2<Class<? extends Entity>, Render>>());
			if(list!=null){
				for(PC_Struct2<Class<? extends Entity>, Render> s:list){
					RenderingRegistry.registerEntityRenderingHandler(s.a, s.b);
				}
			}
		}
		PC_Logger.exitSection();
	}
	
}
