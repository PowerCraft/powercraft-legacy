package powercraft.management.tick;

import powercraft.management.PC_ClientUtils;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.registry.PC_TickRegistry;

public class PC_TickHandler {

	public void tick() {
		if(PC_ClientUtils.mc().theWorld!=null){
			PC_TickRegistry.onTickEvent();
		}
	}
	
}
