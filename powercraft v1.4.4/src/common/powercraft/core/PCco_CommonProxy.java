package powercraft.core;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class PCco_CommonProxy extends PC_Proxy {

	public void initUtils(){
		PC_Utils.setUtilsInstance(new PC_Utils());
	}

	@Override
	public void registerRenderer() {
		new PC_Renderer(true);
		new PC_Renderer(false);
	}

	@Override
	public Object[] registerGuis() {
		return new Object[]{
			"CraftingTool", PCco_ContainerCraftingTool.class	
		};
	}

	@Override
	public Object[] registerPackethandlers(){
		return new Object[]{
				"MobSpawner", new PCco_MobSpawnerSetter(),
				"DeleteAllPlayerStacks", new PCco_DeleteAllPlayerStacks()
		};
	}
	
}
