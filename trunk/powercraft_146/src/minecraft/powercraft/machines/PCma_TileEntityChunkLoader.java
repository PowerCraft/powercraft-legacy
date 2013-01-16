package powercraft.machines;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.management.PC_BeamTracer;
import powercraft.management.PC_Color;
import powercraft.management.PC_IBeamHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_VecI;

public class PCma_TileEntityChunkLoader extends PC_TileEntity implements PC_IBeamHandler {

	private PC_BeamTracer beamTracer;
	private int tick=0;
	
	@Override
	public void updateEntity() {
		if(beamTracer==null){
			beamTracer = new PC_BeamTracer(worldObj, this);
			beamTracer.setCanChangeColor(false);
			beamTracer.setTotalLengthLimit(256);
			beamTracer.setMaxLengthAfterCrystal(256);
			beamTracer.setStartLength(256);
			beamTracer.setColor(new PC_Color(0.001f, 0.001f, 1.000f));
			beamTracer.setBlockHandels(false);
			beamTracer.setStartMove(new PC_VecI(0, 1, 0));
			beamTracer.setTotalLengthLimit(256);
		}
		if(tick%40<10){
			beamTracer.setStartCoord(getCoord().offset(1, 0, 1));
		}else if(tick%40<20){
			beamTracer.setStartCoord(getCoord().offset(-1, 0, 1));
		}else if(tick%40<30){
			beamTracer.setStartCoord(getCoord().offset(-1, 0, -1));
		}else{
			beamTracer.setStartCoord(getCoord().offset(1, 0, -1));
		}
		beamTracer.flash();
		
		tick++;
		
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_VecI coord) {
		return false;
	}

	@Override
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord) {
		return false;
	}

}
