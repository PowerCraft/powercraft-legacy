package powercraft.api.tube;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.item.ItemStack;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_TileEntityTube extends PC_TileEntity {

	@PC_ClientServerSync
	private PC_ItemStack tube;
	@PC_ClientServerSync
	private int cable[][] = new int[6][16];
	
	public ItemStack getTube(){
		return tube==null?null:tube.toItemStack();
	}
	
	public int getCable(PC_Direction side){
		int cable=0;
		int[] a = this.cable[side.getMCDir()];
		for(int i=0; i<16; i++){
			if(a[i]!=0){
				cable |= 1<<i;
			}
		}
		return cable;
	}

	public void setTube(ItemStack tube) {
		this.tube = null;
		if(tube!=null){
			this.tube = new PC_ItemStack(tube);
		}
		if(!worldObj.isRemote)
			notifyChanges("tube");
	}

	@Override
	protected void dataRecieved() {
		PC_Utils.markBlockForUpdate(worldObj, getCoord());
	}

	public void setCable(PC_Direction dir, int cableID, boolean there) {
		if(there){
			int nw = getNigbourNetwork(dir, cableID);
			if(nw==0){
				nw = PC_CableNetworks.getNetworkID(new PC_CableNetwork(worldObj, cableID));
			}
			PC_CableNetworks.addRef(nw);
			cable[dir.getMCDir()][cableID] = nw;
			setNigboutNetwork(dir, cableID, nw, false);
			if(isCableIO(dir, cableID)){
				PC_CableNetworks.getNetwork(nw).addIO(getCoord());
				PC_CableNetworks.getNetwork(nw).setPowerValue(PC_BlockTube.tube.getRedstonePowereValueEx(worldObj, xCoord, yCoord, zCoord));
			}
		}else{
			int nw;
			PC_CableNetworks.release(nw = cable[dir.getMCDir()][cableID], getCoord());
			cable[dir.getMCDir()][cableID] = 0;
			setNigboutNetwork(dir, cableID, nw, true);
		}
		if(!worldObj.isRemote)
			notifyChanges("cable");
	}
	
	public int getNetwork(PC_Direction dir, int cableID){
		return cable[dir.getMCDir()][cableID];
	}
	
	private int getNigbourNetwork(PC_Direction dir, int cableID){
		for(int i=0; i<6; i++){
			PC_Direction dir2 = PC_Direction.getFromMCDir(i);
			if(dir==dir2||dir==dir2.mirror())
				continue;
			int nw = getNetwork(dir2, cableID);
			if(nw>0)
				return nw;
			PC_VecI offset = dir2.getOffset();
			int x = xCoord, y=yCoord, z=zCoord;
			int x1=xCoord+offset.x, y1=yCoord+offset.y, z1=zCoord+offset.z;
			PC_TileEntityTube tileEntityTubeOther;
			if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()){
				tileEntityTubeOther = PC_Utils.getTE(worldObj, x1, y1, z1);
				nw = tileEntityTubeOther.getNetwork(dir2.mirror(), cableID);
				if(nw>0)
					return nw;
				nw = tileEntityTubeOther.getNetwork(dir, cableID);
				if(nw>0)
					return nw;
			}
			offset = dir.getOffset();
			x += offset.x;
			y += offset.y;
			z += offset.z;
			if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()||PC_Utils.getBlock(worldObj, x1, y1, z1)==null){
				x1 += offset.x;
				y1 += offset.y;
				z1 += offset.z;
				if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()){
					tileEntityTubeOther = PC_Utils.getTE(worldObj, x1, y1, z1);
					nw = tileEntityTubeOther.getNetwork(dir2.mirror(), cableID);
					if(nw>0)
						return nw;
				}
			}
			if(PC_Utils.getBlock(worldObj, x, y, z)==getBlockType()){
				tileEntityTubeOther = PC_Utils.getTE(worldObj, x, y, z);
				nw = tileEntityTubeOther.getNetwork(dir2, cableID);
				if(nw>0)
					return nw;
			}
		}
		return 0;
	}

	private void setNigboutNetwork(PC_Direction dir, int cableID, int nw, boolean change){
		for(int i=0; i<6; i++){
			PC_Direction dir2 = PC_Direction.getFromMCDir(i);
			if(dir==dir2||dir==dir2.mirror())
				continue;
			if(getNetwork(dir2, cableID)!=0){
				setNetwork(dir2, cableID, nw);
				if(change){
					nw = -1;
				}
			}
			PC_VecI offset = dir2.getOffset();
			int x = xCoord, y=yCoord, z=zCoord;
			int x1=xCoord+offset.x, y1=yCoord+offset.y, z1=zCoord+offset.z;
			PC_TileEntityTube tileEntityTubeOther;
			if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()){
				tileEntityTubeOther = PC_Utils.getTE(worldObj, x1, y1, z1);
				if(tileEntityTubeOther.getNetwork(dir2.mirror(), cableID)!=0){
					tileEntityTubeOther.setNetwork(dir2.mirror(), cableID, nw);
					if(change){
						nw = -1;
					}
				}
				if(tileEntityTubeOther.getNetwork(dir, cableID)!=0){
					tileEntityTubeOther.setNetwork(dir, cableID, nw);
					if(change){
						nw = -1;
					}
				}
			}
			offset = dir.getOffset();
			x += offset.x;
			y += offset.y;
			z += offset.z;
			if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()||PC_Utils.getBlock(worldObj, x1, y1, z1)==null){
				x1 += offset.x;
				y1 += offset.y;
				z1 += offset.z;
				if(PC_Utils.getBlock(worldObj, x1, y1, z1)==getBlockType()){
					tileEntityTubeOther = PC_Utils.getTE(worldObj, x1, y1, z1);
					if(tileEntityTubeOther.getNetwork(dir2.mirror(), cableID)!=0){
						tileEntityTubeOther.setNetwork(dir2.mirror(), cableID, nw);
						if(change){
							nw = -1;
						}
					}
				}
			}
			if(PC_Utils.getBlock(worldObj, x, y, z)==getBlockType()){
				tileEntityTubeOther = PC_Utils.getTE(worldObj, x, y, z);
				if(tileEntityTubeOther.getNetwork(dir2, cableID)!=0){
					tileEntityTubeOther.setNetwork(dir2, cableID, nw);
					if(change){
						nw = -1;
					}
				}
			}
		}
	}
	
	private void setNetwork(PC_Direction dir, int cableID, int nw) {
		int oldNw = cable[dir.getMCDir()][cableID];
		if(oldNw != nw && oldNw!=0){
			if(nw==-1){
				nw = PC_CableNetworks.getNetworkID(new PC_CableNetwork(worldObj, cableID));
			}
			PC_CableNetworks.release(oldNw, getCoord());
			PC_CableNetworks.addRef(nw);
			if(isCableIO(dir, cableID)){
				PC_CableNetworks.getNetwork(nw).addIO(getCoord());
			}
			cable[dir.getMCDir()][cableID] = nw;
			setNigboutNetwork(dir, cableID, nw, false);
		}
	}

	private boolean isCableIO(PC_Direction dir, int cableID){
		if(dir!=PC_Direction.BOTTOM){
			return false;
		}
		int cable = getCable(PC_Direction.BOTTOM);
		if((cable&1<<cableID)!=cable){
			return false;
		}
		for(int i=0; i<6; i++){
			PC_Direction dir2 = PC_Direction.getFromMCDir(i);
			PC_VecI offset = dir2.getOffset();
			offset.add(getCoord());
			if(offset.y==yCoord && PC_Utils.getBlock(worldObj, offset)!=getBlockType() && BlockRedstoneWire.isPowerProviderOrWire(worldObj, offset.x, offset.y, offset.z, dir2.getMCDir())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(int id) {
		if(id==getBlockType().blockID)
			return;
		int count=0;
		int index=0;
		for(int i=0; i<16; i++){
			if(cable[PC_Direction.BOTTOM.getMCDir()][i]!=0){
				count++;
				if(count==1){
					index = i;
				}
			}
		}
		if(count==1 && isCableIO(PC_Direction.BOTTOM, index)){
			int nw = cable[PC_Direction.BOTTOM.getMCDir()][index];
			PC_CableNetwork cnw = PC_CableNetworks.getNetwork(nw);
			cnw.addIO(getCoord());
			cnw.setPowerValue(PC_BlockTube.tube.getRedstonePowereValueEx(worldObj, xCoord, yCoord, zCoord));
		}
	}

	@Override
	public int getProvidingStrongRedstonePowerValue(PC_Direction dir) {
		if(dir==PC_Direction.TOP||dir==PC_Direction.BOTTOM)
			return 0;
		int cable = getCable(PC_Direction.BOTTOM);
		int cableCount=0;
		int cableOne=0;
		for(int i=0; i<16; i++){
			if((cable&1<<i)!=0){
				cableCount++;
				if(cableCount==1){
					cableOne=i;
				}
			}
		}
		if(cableCount!=1){
			return 0;
		}
		PC_CableNetwork cnw = PC_CableNetworks.getNetwork(this.cable[PC_Direction.BOTTOM.getMCDir()][cableOne]);
		return cnw.getPowerValue();
	}
	
}
