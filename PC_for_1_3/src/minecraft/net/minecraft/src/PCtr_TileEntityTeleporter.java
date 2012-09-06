package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;



/**
 * Teleporter TE
 * 
 * @author MightyPork
 */
public class PCtr_TileEntityTeleporter extends PC_TileEntity {

	public static List<PCtr_TileEntityTeleporter> teleporter = new ArrayList<PCtr_TileEntityTeleporter>();
	
	public final PCtr_TeleporterData td;
	
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
	public PCtr_TileEntityTeleporter() {
		super();
		PCtr_TeleporterData ntd = PCtr_TeleporterHelper.getTeleporterDataAt(xCoord, yCoord, zCoord);
		if(ntd==null)
			ntd = new PCtr_TeleporterData();
		td = ntd;
		PCtr_TeleporterHelper.teleporter.add(td);
		teleporter.add(this);
		
		//td.dimension = worldObj.worldInfo.getDimension();
	}

	@Override
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean canUpdate() {
		return true;
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
		td.direction = direction;
	}

	public boolean isActive() {

		boolean active = false;

		/*if (type == SENDER) {
			active = (!targetName.equals("") && PCtr_TeleporterHelper.targetExists(targetName));
		} else if (type == RECEIVER) {
			active = (!identifierName.equals("") && PCtr_TeleporterHelper.targetExists(identifierName));
		}*/

		
		
		if (active != td.lastActiveState) {
			worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
			td.lastActiveState = active;
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

		if (entity == null) {
			return false;
		}
		
		if (entity instanceof EntityDiggingFX) {
			return false;
		}

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
		
		teleporter.remove(this);
		
	}

	public int getDimension() {
		return td.dimension;
	}
	
	
	@Override
	public void set(Object[] o) {
		
	}

	@Override
	public Object[] get() {
		return null;
	}
}
