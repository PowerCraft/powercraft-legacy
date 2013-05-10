package mods.betterworld.CB;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class BWCB_ClientProxy extends BWCB_CommonProxy {

	@Override
	public void registerRendering() {
		RenderingRegistry.registerBlockHandler(BWCB_Render.getRender());
	}

	@Override
	public int addArmour(String path) {
		return 0;
	}
}
