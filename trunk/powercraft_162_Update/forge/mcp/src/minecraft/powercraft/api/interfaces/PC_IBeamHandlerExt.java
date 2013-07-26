package powercraft.api.interfaces;

import powercraft.api.utils.PC_VecI;
import powercraft.apiOld.PC_BeamTracer;

public interface PC_IBeamHandlerExt extends PC_IBeamHandler {
	
	public boolean onEmptyBlockHit(PC_BeamTracer beamTracer, PC_VecI coord);
	
}
