package powercraft.core;

public interface PC_IBeamSpecialHandling {
	
	public PC_BeamTracer.result onHitByBeamTracer(PC_BeamTracer beamTracer, PC_CoordI cnt, PC_CoordI move, PC_Color color, float strength, int distanceToMove);
	
}
