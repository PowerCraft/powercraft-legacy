package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PC_TileEntity extends TileEntity {

	/**
	 * @return tile entity coordinate
	 */
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}

	/**
	 * Forge method - can update?
	 * 
	 * @return can update
	 */
	public abstract boolean canUpdate();

	/**
	 * Called right before the block is picked up and it's tile entity
	 * destroyed.
	 */
	public void onBlockPickup() {}

	public abstract void set(Object[] o);
	
	public abstract Object[] get();
	
	@Override
	public Packet getAuxillaryInfoPacket() {
		Object[] o = get();
		if(o==null)
			return null;
		ByteArrayOutputStream data = new ByteArrayOutputStream();
    	ObjectOutputStream sendData;
		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeObject("TileEntity");
	        sendData.writeInt(xCoord);
	        sendData.writeInt(yCoord);
	        sendData.writeInt(zCoord);
	        sendData.writeInt(o.length);
	        for(int i=0; i<o.length; i++)
	        	sendData.writeObject(o[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Packet250CustomPayload("PowerCraft", data.toByteArray());
	}
	
}
