package powercraft.management;

import net.minecraft.client.particle.EntitySmokeFX;
import powercraft.management.PC_Utils.Gres;
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
		PC_ClientUtils.registerEnitiyFX(EntitySmokeFX.class);
		NetworkRegistry.instance().registerConnectionHandler(new PC_ConnectionHandler());
		Gres.registerGres("UpdateNotification", PC_GuiUpdateNotification.class);
	}
	
}
