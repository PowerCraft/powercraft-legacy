package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;




/**
 * Teleporter TE
 * 
 * @author MightyPork
 */
public class PCnt_TileEntityTeleporter extends PC_TileEntity {
	
	/*public String defaultTarget, name;

	private boolean lastActiveState = false;

	public boolean items = true;
	public boolean animals = true;
	public boolean monsters = true;
	public boolean players = true;
	public boolean sneakTrigger = false;
	public String direction = "N";
	public boolean hideLabel = false;*/

	public int dimension;
	
	/**
	 * 
	 */
	
	
	public PCnt_TileEntityTeleporter() {
		super();
		System.out.println("created:"+this);
		//td = new PCtr_TeleporterData();
		//PCtr_TeleporterManager.add(this);
	}
	
	@Override
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}

	/*public boolean isSender() {
		return type == SENDER;
	}

	public boolean isReceiver() {
		return type == RECEIVER;
	}

	public void setIsSender() {
		type = SENDER;
	}

	public void setIsReceiver() {
		type = RECEIVER;
	}*/

	public void setPrimaryOutputDirection(String direction) {
		//td.direction = direction;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		/*defaultTarget = nbttagcompound.getString("defaultTarget");
		name = nbttagcompound.getString("name");

		items = nbttagcompound.getBoolean("tpaitems");
		monsters = nbttagcompound.getBoolean("tpamonsters");
		animals = nbttagcompound.getBoolean("tpaanimals");
		players = nbttagcompound.getBoolean("tpaplayers");
		direction = nbttagcompound.getString("tpdir");
		hideLabel = nbttagcompound.getBoolean("hideLabel");

		sneakTrigger = nbttagcompound.getBoolean("tpsneak");

		if (!nbttagcompound.getBoolean("tp_flag_28")) {
			items = true;
			monsters = true;
			animals = true;
			players = true;
		}*/
		
		dimension = nbttagcompound.getInteger("dimension");
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		/*nbttagcompound.setString("defaultTarget", defaultTarget);
		nbttagcompound.setString("name", name);

		nbttagcompound.setBoolean("tpaitems", items);
		nbttagcompound.setBoolean("tpamonsters", monsters);
		nbttagcompound.setBoolean("tpaanimals", animals);
		nbttagcompound.setBoolean("tpaplayers", players);
		nbttagcompound.setBoolean("hideLabel", hideLabel);

		nbttagcompound.setBoolean("tpsneak", sneakTrigger);
		nbttagcompound.setString("tpdir", direction);
		nbttagcompound.setBoolean("tp_flag_28", true);*/
		
		nbttagcompound.setInteger("dimension", dimension);
		
	}

	@Override
	public void onBlockPickup(){
		PCnt_TeleporterManager.remove(PCnt_TeleporterManager.getTeleporterDataAt(dimension, xCoord, yCoord, zCoord));
		System.out.println("remove:"+this);
		
	}
	
	@Override
	public void set(Object[] o) {
		boolean add=false;
		PCnt_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(dimension, xCoord, yCoord, zCoord);
		if(td==null){
			td = new PCnt_TeleporterData();
			add=true;
		}
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("name")){
				td.setName((String)o[p++]);
			}else if(var.equals("defaultTarget")){
				td.defaultTarget = (String)o[p++];
			}else if(var.equals("dimension")){
				td.dimension = (Integer)o[p++];
			}else if(var.equals("tdimension")){
				dimension = (Integer)o[p++];
				add=false;
				td = PCnt_TeleporterManager.getTeleporterDataAt(dimension, xCoord, yCoord, zCoord);
				if(td==null){
					td = new PCnt_TeleporterData();
					add=true;
				}
			}
		}
		if(add)
			PCnt_TeleporterManager.add(td);
	}

	@Override
	public Object[] get() {
		PCnt_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(dimension, xCoord, yCoord, zCoord);
		if(td==null){
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
			return null;
		}
		Object[] o = {
				"tdimension",
				dimension,
				"name",
				td.getName(),
				"defaultTarget",
				td.defaultTarget,
				"dimension",
				td.dimension
		};
		return o;
	}
}
