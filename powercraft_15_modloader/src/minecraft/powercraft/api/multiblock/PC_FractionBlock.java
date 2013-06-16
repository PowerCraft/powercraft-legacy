package powercraft.api.multiblock;

import java.io.IOException;
import java.io.Serializable;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import powercraft.api.interfaces.PC_INBT;
import powercraft.api.utils.PC_VecI;

public abstract class PC_FractionBlock implements PC_INBT<PC_FractionBlock>, Serializable {

	protected float thick=2.0f/16.0f;
	
	protected PC_FractionBlock(){
		
	}
	
	public abstract boolean canPlaceOnPosition(PC_IMultiblock multiblock, PC_FractionSide side);
	
	public AxisAlignedBB getCollisionBox(PC_IMultiblock multiblock, PC_FractionSide side, Entity entity){
		switch(side){
		case BACK:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, thick);
		case BACKBOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, thick, thick);
		case BACKLEFT:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 0, 1, 1, thick);
		case BACKLEFTBOTTOM:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 0, 1, thick, thick);
		case BACKLEFTTOP:
			return AxisAlignedBB.getBoundingBox(1-thick, 1-thick, 0, 1, 1, thick);
		case BACKRIGHT:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, thick, 1, thick);
		case BACKRIGHTBOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, thick, thick, thick);
		case BACKRIGHTTOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 0, thick, 1, thick);
		case BACKTOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 0, 1, 1, thick);
		case BOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, thick, 1);
		case FRONT:
			return AxisAlignedBB.getBoundingBox(0, 0, 1-thick, 1, 1, 1);
		case FRONTBOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 1-thick, 1, thick, 1);
		case FRONTLEFT:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 1-thick, 1, 1, 1);
		case FRONTLEFTBOTTOM:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 1-thick, 1, thick, 1);
		case FRONTLEFTTOP:
			return AxisAlignedBB.getBoundingBox(1-thick, 1-thick, 1-thick, 1, 1, 1);
		case FRONTRIGHT:
			return AxisAlignedBB.getBoundingBox(0, 0, 1-thick, thick, 1, 1);
		case FRONTRIGHTBOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 1-thick, thick, thick, 1);
		case FRONTRIGHTOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 1-thick, thick, 1, 1);
		case FRONTTOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 1-thick, 1, 1, 1);
		case LEFT:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 0, 1, 1, 1);
		case LEFTBOTTOM:
			return AxisAlignedBB.getBoundingBox(1-thick, 0, 0, 1, thick, 1);
		case LEFTTOP:
			return AxisAlignedBB.getBoundingBox(1-thick, 1-thick, 0, 1, 1, 1);
		case MIDDLE:
			return AxisAlignedBB.getBoundingBox(0.5-thick, 0.5-thick, 0.5-thick, 0.5+thick, 0.5+thick, 0.5+thick);
		case RIGHT:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, thick, 1, 1);
		case RIGHTBOTTOM:
			return AxisAlignedBB.getBoundingBox(0, 0, 0, thick, thick, 1);
		case RIGHTTOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 0, thick, 1, 1);
		case TOP:
			return AxisAlignedBB.getBoundingBox(0, 1-thick, 0, 1, 1, 1);
		default:
			return null;
		}
	}

	public abstract void renderFraction(PC_IMultiblock multiblock, PC_FractionSide side, Object renderer);

	public PC_FractionBlock mixWithFraction(PC_FractionBlock otherFraction){
		return null;
	}
	
	public boolean canMixWithFraction(PC_FractionBlock otherFraction){
		return false;
	}

	public void onNeighborBlockChange(PC_IMultiblock multiblock, PC_FractionSide fromIndex, int id) {}
	
	protected abstract void loadFromNBT(NBTTagCompound nbttag);
	
	@Override
	public PC_FractionBlock readFromNBT(NBTTagCompound nbttag) {
		thick = nbttag.getFloat("thick");
		loadFromNBT(nbttag);
		return this;
	}

	protected abstract void saveToNBT(NBTTagCompound nbttag);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setFloat("thick", thick);
		saveToNBT(nbttag);
		return nbttag;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException{
		NBTTagCompound nbttag = new NBTTagCompound("Object");
		nbttag = writeToNBT(nbttag);
		if(nbttag==null){
			out.writeObject(new byte[0]);
		}else{
			out.writeObject(CompressedStreamTools.compress(nbttag));
		}
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
		byte[] bytes = (byte[])in.readObject();
		if(bytes.length>0){
			readFromNBT(CompressedStreamTools.decompress(bytes));
		}
	}
	
}
