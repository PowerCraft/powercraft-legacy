package powercraft.management;

import java.util.List;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;

public class PC_TickHandler {

	private int countDown = 40;
	
	public void tick() {
		if(GameInfo.mcs()!=null && GameInfo.mcs().isServerRunning()){
			if(countDown<=0){
				List<PC_IMSG> objs = ModuleInfo.getMSGObjects();
				for (PC_IMSG obj : objs){
		        	obj.msg(PC_Utils.MSG_TICK_EVENT);
		        }
			}else{
				countDown--;
			}
		}else{
			countDown=20;
		}
	}
	
}
