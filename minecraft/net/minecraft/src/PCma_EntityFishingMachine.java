package net.minecraft.src;

import java.util.Random;


/**
 * Fishing Machine entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_EntityFishingMachine extends Entity {
	/**
	 * @param world the world
	 */
	public PCma_EntityFishingMachine(World world) {
		super(world);
		preventEntitySpawning = true;
		setSize(1F, 6F);
		yOffset = 4F;

		entityCollisionReduction = 1.0F;
		stepHeight = 0.0F;
		isImmuneToFire = true;
	}

	/**
	 * @param world the world
	 * @param d pos x
	 * @param d1 pos y
	 * @param d2 pos z
	 */
	public PCma_EntityFishingMachine(World world, double d, double d1, double d2) {
		this(world);
		setPosition(d, d1 + yOffset, d2);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = d;
		prevPosY = d1;
		prevPosZ = d2;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(1));
		dataWatcher.addObject(19, new Integer(0));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return 0D;
	}

	// returns true if in water.
	@Override
	public boolean handleWaterMovement() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		if (damagesource != DamageSource.outOfWorld
				&& (worldObj.isRemote || isDead || (damagesource.getSourceOfDamage() == null && damagesource != DamageSource.explosion))) { return true; }
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() + i * 7);
		setBeenAttacked();
		if (getDamageTaken() > 40) {
			turnIntoBlocks();
		}
		return true;
	}

	@Override
	public void performHurtAnimation() {
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() * 11);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void setVelocity(double d, double d1, double d2) {
		motionX = d;
		motionY = d1;
		motionZ = d2;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	private Random rand = new Random();

	private int fishTimer = 100;
	private int waterCheckTimer = 200;
	private int bodyCheckTimer = 100;
	private int burningFuel = 0;

	/** Coordinate where the structure starts (X- Z- corner block). */
	private PC_CoordI buildPos;



	// *** Building ***

	/**
	 * Try to build fisher st this coordinate
	 * 
	 * @param stack of ActivationCrystal, used to click
	 * @param player player who clicked
	 * @param world the world
	 * @param pos clicked block position
	 * @return true on success
	 */
	public boolean tryToBuildFishingMachine(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos) {
		if (checkForFishingMachineNearby(pos)) {
			spawnFishingMachine(world);
			stack.damageItem(1, player);
			return true;
		}
		return false;
	}

	/**
	 * Check if fishing machine can be built at this position.
	 * If it fails, user is informed using in-game chat messages.
	 * 
	 * @param pos coordinate of one fence
	 * @return true if it's a valid structure
	 */
	private boolean checkForFishingMachineNearby(PC_CoordI pos) {
		if (pos.getId(worldObj) == 85) {
			int fish = 0;
			boolean errorWater = false;

			// depends on which fence was clicked, we must check all four.

			// 0 = valid, 1 = invalid, 2 = not enough water

			fish = checkForFishingMachineAt(pos.offset(-2, 0, 0));
			switch (fish) {
				case 0:
					// remove iron cube
					pos.offset(-2, 0, 0).offset(1, 0, 1).setBlock(worldObj, 0, 0);
					return true;
				case 2:
					errorWater = true;
			}


			fish = checkForFishingMachineAt(pos.offset(0, 0, -2));
			switch (fish) {
				case 0:
					// remove iron cube
					pos.offset(0, 0, -2).offset(1, 0, 1).setBlock(worldObj, 0, 0);
					return true;
				case 2:
					errorWater = true;
			}


			fish = checkForFishingMachineAt(pos.offset(-2, 0, -2));
			switch (fish) {
				case 0:
					// remove iron cube
					pos.offset(-2, 0, -2).offset(1, 0, 1).setBlock(worldObj, 0, 0);
					return true;
				case 2:
					errorWater = true;
			}


			fish = checkForFishingMachineAt(pos.offset(0, 0, 0));
			switch (fish) {
				case 0:
					// remove iron cube
					pos.offset(0, 0, 0).offset(1, 0, 1).setBlock(worldObj, 0, 0);
					return true;
				case 2:
					errorWater = true;
			}


			if (errorWater) {
				PC_Utils.chatMsg(PC_Lang.tr("pc.fisher.errWater"), false);
			} else {
				PC_Utils.chatMsg(PC_Lang.tr("pc.fisher.errStructure"), false);
			}

		} else {
			if (pos.getId(worldObj) == 5) {
				PC_Utils.chatMsg(PC_Lang.tr("pc.fisher.errClickedPlanks"), false);
			}
		}
		return false;
	}

	/** Fish pattern array, int[3][3] with block IDs; X,Z */
	private int[][] fishPattern = { { 85, 5, 85 }, { 5, 42, 5 }, { 85, 5, 85 } };


	/**
	 * Check if the structure at this coord is complete
	 * 
	 * @param pos starting position
	 * @return
	 *         <ul>
	 *         <li>0 - valid structure
	 *         <li>1 - invalid structure
	 *         <li>2 - not enough water
	 *         </ul>
	 */
	private int checkForFishingMachineAt(PC_CoordI pos) {

		buildPos = pos.copy();

		if (!isBodyComplete(true)) { return 1; }

		// count water blocks under it - depth of 10.
		if (!hasEnoughWater()) { return 2; }

		setLocationAndAngles(pos.x + 1.5D, pos.y - yOffset, pos.z + 1.5D, 0.0F, 0.0F);

		return 0;
	}

	/**
	 * Spawn itself in the world
	 * 
	 * @param world the world
	 */
	private void spawnFishingMachine(World world) {
		world.spawnEntityInWorld(this);
	}

	// *** STRUCTURE CHECKS ***

	/**
	 * Check if there's enough water under the machine.<br>
	 * Water must be at least 8 blocks deep.
	 * 
	 * @return has enough water
	 */
	private boolean hasEnoughWater() {
		int nonWaters = 0;
		int critical = 0;

		for (int x = buildPos.x; x <= buildPos.x + 2; x++) {
			for (int z = buildPos.z; z <= buildPos.z + 2; z++) {
				for (int y = buildPos.y - 1; y > buildPos.y - 8; y--) {

					int id = new PC_CoordI(x, y, z).getId(worldObj);
					if (id != 8 && id != 9) {
						nonWaters++;
						if ((x == buildPos.x + 1 && z == buildPos.z + 1) || y >= buildPos.y - 3) {
							critical++;
						}
					}

				}
			}
		}
		if (nonWaters > 20 || critical > 2) { return false; }

		return true;
	}

	/**
	 * Check if body is complete.
	 * 
	 * @param building building new structure - check iron cube
	 * @return is complete
	 */
	private boolean isBodyComplete(boolean building) {
		for (int x = buildPos.x; x <= buildPos.x + 2; x++) {
			for (int z = buildPos.z; z <= buildPos.z + 2; z++) {

				if (worldObj.getBlockId(x, buildPos.y, z) != fishPattern[x - buildPos.x][z - buildPos.z]) {

					// skip iron cube check if not building - it was replaced by air when the fisher was built
					if (!building && x == buildPos.x + 1 && z == buildPos.z + 1) {
						continue;
					}
					return false;
				}

			}
		}

		return buildPos.offset(1, 1, 1).getId(worldObj) == 54;
	}

	/**
	 * Turn into blocks if not enough water
	 */
	private void checkWaterState() {
		if (!hasEnoughWater()) {
			turnIntoBlocks();
		}
	}

	/**
	 * Turn into blocks if the body is corrupted
	 */
	private void checkBodyState() {
		if (!isBodyComplete(false)) {
			turnIntoBlocks();
		}
	}


	// *** FISH CATCHING ***

	/**
	 * Catch and eject a fish to nearby covneyor or air block.
	 */
	private void catchFish() {

		PC_CoordI[] outputs = new PC_CoordI[4];

		outputs[0] = buildPos.offset(-1, 0, 1);
		outputs[1] = buildPos.offset(3, 0, 1);
		outputs[2] = buildPos.offset(1, 0, -1);
		outputs[3] = buildPos.offset(1, 0, 3);


		for (int i = 0; i <= 3; i++) {
			if (PC_BlockUtils.isConveyorOrElevator(worldObj, outputs[i])) {
				ejectFish_do(outputs[i], false);
				return;
			}
		}

		for (int i = 0; i <= 3; i++) {
			if (outputs[i].getId(worldObj) == 0) {
				ejectFish_do(outputs[i], true);
				return;
			}
		}

	}

	/**
	 * Create and eject fish or ink item
	 * 
	 * @param out pos of the output block
	 * @param fast set false if there's a conveyor.
	 */
	private void ejectFish_do(PC_CoordI out, boolean fast) {
		ItemStack caught = new ItemStack(rand.nextInt(6) == 0 ? Item.dyePowder : Item.fishRaw, 1, 0);
		EntityItem entityitem = new EntityItem(worldObj, out.x + 0.5D, out.y + 0.5D, out.z + 0.5D, caught);

		if (!fast) {
			entityitem.motionX = 0;
			entityitem.motionY = 0;
			entityitem.motionZ = 0;
		}

		entityitem.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityitem);

		if (mod_PCcore.soundsEnabled) {
			worldObj.playSoundAtEntity(entityitem, "random.splash", 0.2F, 0.5F + rand.nextFloat() * 0.3F);
		}
	}




	// === UPDATE TICK ===

	@Override
	public void onUpdate() {
		super.onUpdate();

		// breaking animations.
		if (getTimeSinceHit() > 0) {
			setTimeSinceHit(getTimeSinceHit() - 1);
		}
		if (getDamageTaken() > 0) {
			setDamageTaken(getDamageTaken() - 1);
		}

		boolean running = checkFuel();

		if (running) {

			// bubbles
			for (int i = 0; i < 2; i++) {
				double buX = posX - 0.3D + rand.nextFloat() * 0.6F;
				double buY = posY + yOffset - 4.8D + rand.nextFloat() * 0.6F;
				double buZ = posZ - 0.3D + rand.nextFloat() * 0.6F;
				worldObj.spawnParticle("bubble", buX, buY, buZ, 0, 0.01F, 0);
			}

			// splash sound
			if (rand.nextInt(20) == 0 && mod_PCcore.soundsEnabled) {
				worldObj.playSoundAtEntity(this, "random.splash", 0.08F, 0.5F + rand.nextFloat() * 0.3F);
			}

			// smoke from chimney
			if (rand.nextInt(2) == 0) {
				double chimX = posX - 0.1F + rand.nextFloat() * 0.2F;
				double chimY = posY + 2.4F;
				double chimZ = posZ - 0.8F + rand.nextFloat() * 0.2F;
				worldObj.spawnParticle("largesmoke", chimX, chimY, chimZ, 0, 0, 0);
			}

			rotationYaw -= 15;
			if (rotationYaw <= 0) {
				rotationYaw += 360;
				prevRotationYaw = rotationYaw + 15F;
			}
		}

		if (--waterCheckTimer == 0) {
			waterCheckTimer += 300;
			checkWaterState();
		}

		if (--bodyCheckTimer == 0) {
			bodyCheckTimer += 80;
			checkBodyState();
		}

		if (running) {
			if (--fishTimer == 0) {
				fishTimer = 250 + rand.nextInt(350);
				catchFish();
			}
		}

	}


	// NBT TAGs

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {

		tag.setInteger("buildX", buildPos.x);
		tag.setInteger("buildY", buildPos.y);
		tag.setInteger("buildZ", buildPos.z);
		tag.setInteger("fishTimer", fishTimer);
		tag.setInteger("burningFuel", burningFuel);
		tag.setInteger("waterCheckTimer", waterCheckTimer);
		tag.setInteger("bodyCheckTimer", bodyCheckTimer);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		buildPos = new PC_CoordI(tag.getInteger("buildX"), tag.getInteger("buildY"), tag.getInteger("buildZ"));
		fishTimer = tag.getInteger("fishTimer");
		burningFuel = tag.getInteger("burningFuel");
		waterCheckTimer = tag.getInteger("waterCheckTimer");
		bodyCheckTimer = tag.getInteger("bodyCheckTimer");
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		return Block.chest.blockActivated(worldObj, buildPos.x + 1, buildPos.y + 1, buildPos.z + 1, entityplayer);
	}

	// === WATCHER ===

	public void setDamageTaken(int i) {
		dataWatcher.updateObject(19, Integer.valueOf(i));
	}

	public int getDamageTaken() {
		return dataWatcher.getWatchableObjectInt(19);
	}

	public void setTimeSinceHit(int i) {
		dataWatcher.updateObject(17, Integer.valueOf(i));
	}

	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	public void setForwardDirection(int i) {
		dataWatcher.updateObject(18, Integer.valueOf(i));
	}

	public int getForwardDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	// === TURNING INTO BLOCKS ===

	private void turnIntoBlocks() {

		buildPos.offset(1, 0, 1).setBlock(worldObj, 42, 0);

		setDead();
	}

	private IInventory getChestInventory() {
		TileEntity te = buildPos.offset(1, 1, 1).getTileEntity(worldObj);

		if (te instanceof IInventory) { return (IInventory) te; }
		return null;
	}

	/**
	 * Check fuel contained in chest.
	 * 
	 * @return true if enough fuel consumed for next fish
	 */
	private boolean checkFuel() {

		if (burningFuel > 11) {
			burningFuel -= 3;
			return true;
		} else {

			IInventory inv = getChestInventory();
			if (inv == null) {
				turnIntoBlocks();
				return false;
			}

			for (int s = 0; s < inv.getSizeInventory(); s++) {
				ItemStack stack = inv.getStackInSlot(s);
				int cost = PC_InvUtils.getFuelValue(stack, 1.0D);
				if (cost > 0) {
					burningFuel += cost;
					if (stack.getItem().hasContainerItem()) {
						inv.setInventorySlotContents(s, new ItemStack(stack.getItem().getContainerItem(), 1, 0));
					} else {
						inv.decrStackSize(s, 1);
					}

					return true;
				}
			}

			return false;
		}
	}

}
