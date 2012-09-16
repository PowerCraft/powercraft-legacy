package net.minecraft.src;


/**
 * Laser tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_TileEntityLaser extends PC_TileEntity implements PC_IBeamHandler {

	private PCma_BeamTracer beamTracer;

	/** status flag */
	public boolean active = false;
	private boolean loadedLegacyLaser = false;

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		active = nbttagcompound.getBoolean("on");
		type = nbttagcompound.getInteger("ltype");
		receiverCooldownTimer = nbttagcompound.getInteger("lrecflag");

		if (type == 0) {
			loadedLegacyLaser = true;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("on", active);
		nbttagcompound.setInteger("ltype", type);
		nbttagcompound.setInteger("lrecflag", receiverCooldownTimer);
	}

	/**
	 * Forge method
	 * 
	 * @return should this TE get update ticks?
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {

		// create the tracer if not created yet
		if (beamTracer == null) {

			int l = getDirection();

			PC_CoordI move = new PC_CoordI(Facing.offsetsXForSide[l], 0, Facing.offsetsZForSide[l]);

			PC_CoordI cnt = new PC_CoordI(xCoord, yCoord, zCoord);
			beamTracer = new PCma_BeamTracer(worldObj, this);

			beamTracer.setStartCoord(cnt);
			beamTracer.setStartMove(move);
			beamTracer.setCanChangeColor(true);
			beamTracer.setReflectedByMirror(true);
			beamTracer.setReflectedByPrism(true);
			beamTracer.setTotalLengthLimit(10000);
			beamTracer.setMaxLengthAfterCrystal(8000);
			beamTracer.setStartLength(180);
			beamTracer.setCrystalAddedLength(100);

		}

		// code for migration from old laser type
		if (type == 0 && loadedLegacyLaser) {

			if (worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == mod_PCmachines.roaster.blockID) {
				type = PCma_LaserType.KILLER;
			} else {
				type = PCma_LaserType.SENSOR;
			}

			loadedLegacyLaser = false;

		}

		// if type is not decided
		if (type == 0) {
			return;
		}

		// if it's killer and roaster is not burning
		if (isKiller() && !isKillerActive()) {
			return;
		}

		// if it's a receiver
		if (isRsReceiver()) {

			if (receiverCooldownTimer > 0) {
				notifyChange(true);
				if (receiverCooldownTimer > 1) {
					receiverCooldownTimer = 1;
				} else {
					receiverCooldownTimer = 0;
				}
			} else {
				notifyChange(false);
			}

			return;
		}

		// if it's a transmitter and is unpowered
		if (isRsTransmitter()) {
			if (!isRedstonePowered()) {
				return;
			}
		}

		// prepare color for type
		PC_Color color = new PC_Color();

		if (isKiller()) {
			color.setTo(1.0D, 0.001D, 0.001D);
			color.setMeta(1);
		} else if (isSensor()) {
			color.setTo(1.0D, 0.001D, 0.5D);
			color.setMeta(5);
		} else if (isRsTransmitter()) {
			color.setTo(0.001D, 1.0D, 0.001D);
			color.setMeta(2);
		} else {
			color.setMeta(-1);
		}

		hitObjectThisShot = false;

		beamTracer.setColor(color).setDetectEntities(isKiller() || isSensor()).setParticlesBidirectional(isSensor());
		beamTracer.flash();
		
		// send redstone if sender and hit something.
		if (isSensor()) {
			notifyChange(hitObjectThisShot);
		}

	}

	/** flag for sensor */
	private boolean hitObjectThisShot = false;

	/** type of the laser device */
	public int type;

	private void notifyChange(boolean state) {
		if (state) {
			if (!active) {
				active = true;
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
				worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
			}
		} else {
			if (active) {
				active = false;
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
				worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);

			}
		}
	}

	private int getDirection() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	/**
	 * @return is this device killer?
	 */
	public boolean isKiller() {
		return type == PCma_LaserType.KILLER;
	}

	/**
	 * @return is this device sensor?
	 */
	public boolean isSensor() {
		return type == PCma_LaserType.SENSOR;
	}

	/**
	 * @return is this device rs sender?
	 */
	public boolean isRsTransmitter() {
		return type == PCma_LaserType.RS_SEND;
	}

	/**
	 * @return is this device rs receiver?
	 */
	public boolean isRsReceiver() {
		return type == PCma_LaserType.RS_RECEIVE;
	}

	private boolean isKillerActive() {
		if (isKiller()) {
			return PCma_BlockRoaster.isBurning(worldObj, xCoord, yCoord - 1, zCoord);
		}
		return false;
	}

	private boolean isRedstonePowered() {
		if (worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)) {
			return true;
		}

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			return true;
		}

		if (worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)) {
			return true;
		}

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			return true;
		}
		return false;
	}

	/**
	 * Set laser type
	 * 
	 * @param type laser type; Uses constants from PCma_LaserType.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * if receiver is hit by sender's beam, this is set to some number.
	 */
	public int receiverCooldownTimer = 0;

	@Override
	public boolean onBlockHit(World world, PC_CoordI coord, PC_CoordI startCoord) {

		int id = coord.getId(world);

		if (Block.blocksList[id] == null) {
			return false;
		}

		if (id == Block.tnt.blockID && (isKiller() || isRsTransmitter())) {

			Block.tnt.onBlockDestroyedByPlayer(worldObj, coord.x, coord.y, coord.z, 1);
			coord.setBlock(world, 0, 0);

			return false;

		}

		Material mat = Block.blocksList[id].blockMaterial;

		Block block = Block.blocksList[id];

		// translucent blocks...
		if (mat == Material.glass || mat == Material.snow || mat == Material.plants || mat == Material.circuits || mat == Material.vine
				|| id == Block.signPost.blockID || id == Block.signWall.blockID || block instanceof BlockPane
				|| PC_BlockUtils.hasFlag(world, coord, "TRANSLUCENT")) {

			return false;

		} else {

			// laser
			if (id == mod_PCmachines.laser.blockID) {
				if (isKiller() && coord.equals(startCoord)) {
					worldObj.createExplosion(null, coord.x, coord.y, coord.z, 3);

				} else if (isRsTransmitter()) {
					PCma_TileEntityLaser tel = ((PCma_TileEntityLaser) coord.getTileEntity(world));

					if (tel.isRsReceiver()) {
						tel.receiverCooldownTimer = 2;
						tel.notifyChange(true);
					}
				}

				return true;
			}

			// PC block, yet not translucent
			if (PC_BlockUtils.hasFlag(world, coord, "POWERCRAFT")) {
				return true;
			}

			if (Block.blocksList[id].isOpaqueCube()) {
				return true;
			}

		}

		return false;
	}

	@Override
	public boolean onEntityHit(World world, Entity[] array, PC_CoordI startCoord) {

		if (isSensor()) {

			int cnte = 0;
			for (Entity entity : array) {
				if ((entity instanceof EntityFX) || (entity instanceof EntityArrow) || (entity instanceof EntityEnderEye)
						|| (entity instanceof EntityEnderPearl) || (entity instanceof EntityFishHook) || (entity instanceof EntityPainting)
						|| (entity instanceof EntityWeatherEffect) || (entity instanceof EntityItem) || (entity instanceof EntityXPOrb)) {
					continue;
				}

				cnte++;
				break;
			}
			if (cnte < 1) {
				return false;
			}

			hitObjectThisShot = true;

			return true;

		} else if (isKiller()) {

			killLoop:
			for (Entity entity : array) {

				if (!(entity instanceof EntityLiving)) {
					continue killLoop;
				}

				if (!entity.isEntityAlive()) {
					continue killLoop;
				}
				((EntityLiving) entity).experienceValue /= 2;

				if (entity instanceof EntityPlayerSP) {

					EntityPlayerSP player = (EntityPlayerSP) entity;
					int armourValue = PC_InvUtils.getPlayerArmourValue(player);
					if (worldObj.rand.nextBoolean()) {
						player.attackEntityFrom(new PCma_LaserDamageSource(), MathHelper.clamp_int(Math.round(5 * (0.7F - (armourValue / 8))), 0, 8));
					}

				} else {

					entity.attackEntityFrom(new PCma_LaserDamageSource(), 200);

				}

			}
		}
		return false;
	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("active"))
				active = (Boolean)o[p++];
			else if(var.equals("loadedLegacyLaser"))
				loadedLegacyLaser = (Boolean)o[p++];
			else if(var.equals("type"))
				type = (Integer)o[p++];
			else if(var.equals("hitObjectThisShot"))
				hitObjectThisShot = (Boolean)o[p++];
			else if(var.equals("receiverCooldownTimer"))
				receiverCooldownTimer = (Integer)o[p++];
		}
	}

	@Override
	public Object[] get() {
		return new Object[]{
				"active", active,
				"loadedLegacyLaser", loadedLegacyLaser,
				"type", type,
				"hitObjectThisShot", hitObjectThisShot,
				"receiverCooldownTimer", receiverCooldownTimer
		};
	}
}
