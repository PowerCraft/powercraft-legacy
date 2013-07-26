package powercraft.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.api.utils.PC_VecI;
import powercraft.apiOld.PC_BeamTracer;

public interface PC_IBeamHandler {
	
	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_VecI coord);
	
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord);
	
}
