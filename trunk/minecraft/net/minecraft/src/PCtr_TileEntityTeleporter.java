package net.minecraft.src;



/**
 * Teleporter TE
 * 
 * @author MightyPork
 */
public class PCtr_TileEntityTeleporter extends PC_TileEntity {
	private static final int SENDER = 1, RECEIVER = 2, INVALID = 0;
	/** TP type */
	public int type;

	public String targetName, identifierName;

	private boolean lastActiveState = false;

	public boolean items = true;
	public boolean animals = true;
	public boolean monsters = true;
	public boolean players = true;
	public boolean sneakTrigger = false;
	public String direction = "N";
	public boolean hideLabel = false;

	/**
	 * 
	 */
	public PCtr_TileEntityTeleporter() {
		type = INVALID;
		targetName = "";
		identifierName = "";
	}

	@Override
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	public boolean isSender() {
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
	}

	public void setPrimaryOutputDirection(String direction) {
		this.direction = direction;
	}

	public boolean isActive() {

		boolean active = false;

		if (type == SENDER) {
			active = (!targetName.equals("") && PCtr_TeleporterHelper.targetExists(targetName));
		} else if (type == RECEIVER) {
			active = (!identifierName.equals("") && PCtr_TeleporterHelper.targetExists(identifierName));
		}

		if (active != lastActiveState) {
			worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
			worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
			lastActiveState = active;
		}

		return active;

	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		targetName = nbttagcompound.getString("tptarget");
		identifierName = nbttagcompound.getString("tpidentifier");
		type = nbttagcompound.getInteger("tptype");

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
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setString("tpidentifier", identifierName);
		nbttagcompound.setString("tptarget", targetName);
		nbttagcompound.setInteger("tptype", type);

		nbttagcompound.setBoolean("tpaitems", items);
		nbttagcompound.setBoolean("tpamonsters", monsters);
		nbttagcompound.setBoolean("tpaanimals", animals);
		nbttagcompound.setBoolean("tpaplayers", players);
		nbttagcompound.setBoolean("hideLabel", hideLabel);

		nbttagcompound.setBoolean("tpsneak", sneakTrigger);
		nbttagcompound.setString("tpdir", direction);
		nbttagcompound.setBoolean("tp_flag_28", true);
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

		if ((entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntitySlime) && !animals) {
			return false;
		}

		if ((entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntityDragon || entity instanceof EntityGolem)
				&& !monsters) {
			return false;
		}

		if ((entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) && !items) {
			return false;
		}

		if ((entity instanceof EntityPlayer) && !players) {
			return false;
		}

		if ((entity instanceof EntityPlayer) && !entity.isSneaking() && sneakTrigger) {
			return false;
		}

		return true;

	}
}
