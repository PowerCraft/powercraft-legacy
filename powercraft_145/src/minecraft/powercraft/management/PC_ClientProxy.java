package powercraft.management;

import net.minecraft.src.EntitySmokeFX;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;

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
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
		PC_ClientUtils.registerEnitiyFX(EntitySmokeFX.class);
	}
	
}
