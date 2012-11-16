package powercraft.light;

import powercraft.core.PC_ClientUtils;
import powercraft.logic.PClo_GuiDelayer;
import powercraft.logic.PClo_GuiPulsar;

public class PCli_ClientProxy extends PCli_CommonProxy {

	
	@Override
	public Object[] registerGuis() {
		return new Object[]{
			"Light", PCli_GuiLight.class
		};
	}

	@Override
	public void registerTileEntitySpecialRenderers() {
		PC_ClientUtils.bindTileEntitySpecialRenderer(PCli_TileEntityLight.class, new PCli_TileEntityLightRenderer());
	}

	
	
}
