package powercraft.api;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;

public class PC_Direction implements Serializable, PC_INBT<PC_Direction> {

	public static final long serialVersionUID = 1522073818686692234L;
	
	public static final PC_Direction BACK = new PC_Direction(0), LEFT = new PC_Direction(1), 
			RIGHT = new PC_Direction(2), FRONT = new PC_Direction(3), BOTTOM = new PC_Direction(4), 
			TOP = new PC_Direction(5);
	
	private int mcDir;
	private boolean lock;
	
	public PC_Direction(){
		lock = false;
		mcDir = 0;
	}
	
	public PC_Direction(int dir){
		lock = true;
		mcDir = dir;
	}
	
	public int getMCDir(){
		return mcDir;
	}
	
	public PC_VecI getDir(){
		switch(mcDir){
		case 0:
			return new PC_VecI(0, 0, -1);
		case 1:
			return new PC_VecI(-1, 0, 0);
		case 2:
			return new PC_VecI(1, 0, 0);
		case 3:
			return new PC_VecI(0, 0, 1);
		case 4:
			return new PC_VecI(0, -1, 0);
		case 5:
			return new PC_VecI(0, 1, 0);
		}
		return null;
	}
	
	public PC_Direction rotateRight(){
		switch(mcDir){
		case 0:
			return LEFT;
		case 1:
			return FRONT;
		case 2:
			return BACK;
		case 3:
			return RIGHT;
		case 4:
			return BOTTOM;
		case 5:
			return TOP;
		}
		return null;
	}
	
	public PC_Direction rotateLeft(){
		switch(mcDir){
		case 0:
			return RIGHT;
		case 1:
			return BACK;
		case 2:
			return FRONT;
		case 3:
			return LEFT;
		case 4:
			return BOTTOM;
		case 5:
			return TOP;
		}
		return null;
	}
	
	public PC_Direction mirror(){
		switch(mcDir){
		case 0:
			return FRONT;
		case 1:
			return RIGHT;
		case 2:
			return LEFT;
		case 3:
			return BACK;
		case 4:
			return TOP;
		case 5:
			return BOTTOM;
		}
		return null;
	}
	
	public static PC_Direction getFormMCDir(int dir){
		switch(dir){
		case 0:
			return BACK;
		case 1:
			return LEFT;
		case 2:
			return RIGHT;
		case 3:
			return FRONT;
		case 4:
			return BOTTOM;
		case 5:
			return TOP;
		}
		return null;
	}

	@Override
	public PC_Direction readFromNBT(NBTTagCompound nbttag) {
		if(!lock){
			mcDir = nbttag.getInteger("dir");
			lock = true;
		}
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("dir", mcDir);
		return nbttag;
	}

	@Override
	public String toString() {
		return "Direction: "+getDirName();
	}
	
	public String getDirName(){
		switch(mcDir){
		case 0:
			return "BACK";
		case 1:
			return "LEFT";
		case 2:
			return "RIGHT";
		case 3:
			return "FRONT";
		case 4:
			return "BOTTOM";
		case 5:
			return "TOP";
		}
		return "";
	}
	
}
