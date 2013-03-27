package powercraft.api;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;

public interface PC_IBeamHandler {

	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block,
			PC_VecI coord);

	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity,
			PC_VecI coord);

}
