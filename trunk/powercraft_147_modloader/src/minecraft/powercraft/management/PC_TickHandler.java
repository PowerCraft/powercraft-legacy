package powercraft.management;

import powercraft.management.registry.PC_MSGRegistry;

public class PC_TickHandler {

	public void tick() {
		if(PC_ClientUtils.mc().theWorld!=null){
			PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_TICK_EVENT);
		}
	}
	
}
