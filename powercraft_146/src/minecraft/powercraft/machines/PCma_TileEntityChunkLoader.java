package powercraft.machines;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.management.PC_BeamTracer;
import powercraft.management.PC_Color;
import powercraft.management.PC_IBeamHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PCma_TileEntityChunkLoader extends PC_TileEntity implements PC_IBeamHandler {

	private int tick=0;
	
	@Override
	public void updateEntity() {
		
		PC_VecI pos = getCoord().copy();
		
		for(int i=-1; i<=1; i++){
			for(int j=-1; j<=1; j++){
				if((i==j || -i==j) && i!=0){
					if(GameInfo.getBID(worldObj, pos.offset(i, 0, j))!=Block.glass.blockID){
						System.out.println("no glass "+i+":"+j);
						ValueWriting.setBID(worldObj, pos, Block.glass.blockID, 0);
						return;
					}
				}else if(i!=j){
					if(GameInfo.getBID(worldObj, pos.offset(i, 0, j))!=Block.obsidian.blockID){
						System.out.println("no obs "+i+":"+j);
						ValueWriting.setBID(worldObj, pos, Block.glass.blockID, 0);
						return;
					}
				}
				if(!worldObj.canBlockSeeTheSky(pos.x+i, pos.y+1, pos.z+j)){
					System.out.println("no "+i+":"+j);
					ValueWriting.setBID(worldObj, pos, Block.glass.blockID, 0);
					return;
				}
			}
		}
		
		if(tick%40<10){
			pos.add(1, 0, 1);
		}else if(tick%40<20){
			pos.add(-1, 0, 1);
		}else if(tick%40<30){
			pos.add(-1, 0, -1);
		}else{
			pos.add(1, 0, -1);
		}
		
		if(worldObj.isRemote)
			ValueWriting.spawnParticle("PC_EntityLaserFX", worldObj, pos, new PC_VecI(0, 256, 0), 0.5f, new PC_Color(1.0f, 0.001f, 0.2f));
		
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
