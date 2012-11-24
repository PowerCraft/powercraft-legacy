package powercraft.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;

public class PCco_ClientProxy extends PCco_CommonProxy {

	
	
	public PCco_ClientProxy(){
		
	}
	
	@Override
	public void initUtils(){
		PC_Utils.setUtilsInstance(new PC_ClientUtils());
	}
	
	@Override
	public void registerRenderer(){
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(true));
		RenderingRegistry.registerBlockHandler(new PC_ClientRenderer(false));
	}
	
	@Override
	public Object[] registerGuis(){
		return new Object[]{
			"CraftingTool", PCco_GuiCraftingTool.class,
			"OreSnifferResultScreen", PCco_GuiOreSnifferResultScreen.class,
			"SpawnerEditor", PCco_GuiSpawnerEditor.class,
			"UpdateNotification", PCco_GuiUpdateNotification.class
		};
	}
	
	@Override
	public Object[] registerPackethandlers(){
		return new Object[]{
				"MobSpawner", new PCco_ClientMobSpawnerSetter(),
				"DeleteAllPlayerStacks", new PCco_DeleteAllPlayerStacks()
		};
	}
	
	@Override
	public void init(){
		TickRegistry.registerTickHandler(new PCco_MainMenuHacks(), Side.CLIENT);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
	}
	
}
