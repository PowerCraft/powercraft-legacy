package powercraft.api;


public interface PC_IBeamHandlerExt extends PC_IBeamHandler {

	public boolean onEmptyBlockHit(PC_BeamTracer beamTracer, PC_VecI coord);
	
}
