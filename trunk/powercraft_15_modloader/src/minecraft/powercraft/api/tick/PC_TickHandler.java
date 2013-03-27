package powercraft.api.tick;

import powercraft.api.PC_ClientUtils;
import powercraft.api.registry.PC_TickRegistry;

public class PC_TickHandler {
	
	public void tick() {
		if(PC_ClientUtils.mc().theWorld!=null){
			PC_TickRegistry.onTickEvent();
		}
	}

}
