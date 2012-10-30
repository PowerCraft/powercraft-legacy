package powercraft.logic;

import powercraft.logic.PClo_CommonProxy;

public class PClo_ClientProxy extends PClo_CommonProxy {

	@Override
	public Object[] registerGuis() {
		return new Object[]{
			"Pulsar", PClo_GuiPulsar.class,
			"Delayer", PClo_GuiDelayer.class
		};
	}

	
	
}
