package powercraft.api.interfaces;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import powercraft.api.PC_BeamTracer;
import powercraft.api.utils.PC_VecI;

public interface PC_IBeamHandler {
	
	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_VecI coord);
	
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord);
	
}
