package powercraft.transport;

import powercraft.core.PC_Proxy;

public class PCtr_CommonProxy extends PC_Proxy {

	@Override
	public Object[] registerGuis() {
		return new Object[]{
				"SeperationBelt", PCtr_ContainerSeparationBelt.class
		};
	}

}
