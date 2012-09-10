package net.minecraft.src.PCnt;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_CoordI;
import net.minecraft.src.PC_TileEntity;
import net.minecraft.src.PC_Utils;



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
	public boolean hideLabel = false;

	public int dimension;*/
	
	/**
	 * 
	 */
	
	
	public PCnt_TileEntityTeleporter() {
		super();
		System.out.println("created:"+this);
		//td = new PCtr_TeleporterData();
		//PCtr_TeleporterManager.add(this);
	}

	public void createData(){
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		if(td==null){
			PC_Utils.sendToPacketHandler(PC_Utils.mc().thePlayer, "TeleporterNetHandler", 0, xCoord, yCoord, zCoord, "", "", worldObj.worldInfo.getDimension());
		}
	}
	
	@Override
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void updateEntity(){
		createData();
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

	public boolean isActive() {

		boolean active = false;

		/*if (type == SENDER) {
			active = (!targetName.equals("") && PCtr_TeleporterHelper.targetExists(targetName));
		} else if (type == RECEIVER) {
			active = (!identifierName.equals("") && PCtr_TeleporterHelper.targetExists(identifierName));
		}*/
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		
		if(td!=null){
			if (active != td.lastActiveState) {
				worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
				td.lastActiveState = active;
			}
		}
		return active;

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
	}

	/**
	 * Does sender accept type of entity
	 * 
	 * @param entity
	 * @return accepted
	 */
	public boolean acceptsEntity(Entity entity) {

		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		
		if(td == null)
			return false;
		
		if (entity == null) {
			return false;
		}
		
		if(!(entity instanceof EntityLiving || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow))
			return false;
		
		
		if ((entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntitySlime) && !td.animals) {
			return false;
		}

		if ((entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntityDragon || entity instanceof EntityGolem)
				&& !td.monsters) {
			return false;
		}

		if ((entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) && !td.items) {
			return false;
		}

		if ((entity instanceof EntityPlayer) && !td.players) {
			return false;
		}

		if ((entity instanceof EntityPlayer) && !entity.isSneaking() && td.sneakTrigger) {
			return false;
		}
		
		return true;

	}

	@Override
	public void onBlockPickup(){
		PCnt_TeleporterManager.remove(PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord));
		System.out.println("remove:"+this);
		
	}

	public int getDimension() {
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		return td.dimension;
	}
	
	
	@Override
	public void set(Object[] o) {
		boolean add=false;
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		if(td==null){
			td = new PC_TeleporterData();
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
			}
		}
		if(add)
			PCnt_TeleporterManager.add(td);
	}

	@Override
	public Object[] get() {
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(xCoord, yCoord, zCoord);
		if(td==null){
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
			return null;
		}
		Object[] o = {
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
