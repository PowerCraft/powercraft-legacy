package net.minecraft.src;


import java.util.List;
import java.util.Random;

import net.minecraft.src.weasel.IWeaselHardware;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.lang.InstructionAssignRetval;
import net.minecraft.src.weasel.lang.InstructionCall;
import net.minecraft.src.weasel.lang.InstructionEnd;
import net.minecraft.src.weasel.lang.InstructionFunction;
import net.minecraft.src.weasel.lang.InstructionGoto;
import net.minecraft.src.weasel.lang.InstructionIf;
import net.minecraft.src.weasel.lang.InstructionLabel;
import net.minecraft.src.weasel.lang.InstructionReturn;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCmo_EntityMiner extends Entity implements IInventory {


	/**
	 * Try to spawn Miner where player clicked with activator.
	 * 
	 * @param itemstack stack with activator
	 * @param entityplayer the player who clicked
	 * @param world the world
	 * @param position clicked position
	 * @return success
	 */
	public boolean tryToSpawnMinerAt(ItemStack itemstack, EntityPlayer entityplayer, World world, PC_CoordI position) {

		int steel = Block.blockSteel.blockID;
		int chest = Block.chest.blockID;

		String eMinerStructure = PC_Lang.tr("pc.miner.build.errInvalidStructure");
		String eMinerCrystals = PC_Lang.tr("pc.miner.build.errMissingCrystals");


		miner_build_loop:
		for (int yy = position.y; yy >= position.y - 1; yy--) {
			for (int xx = position.x - 1; xx <= position.x + 1; xx++) {
				for (int zz = position.z - 1; zz <= position.z + 1; zz++) {

					PC_CoordI pos = new PC_CoordI(xx, yy, zz);

					// is lower layer?
					if (pos.getId(world) == steel && pos.offset(1, 0, 0).getId(world) == steel && pos.offset(1, 0, 1).getId(world) == steel && pos.offset(0, 0, 1).getId(world) == steel) {

						System.out.println("x-,z-,y- coord orig 4STEEL = " + pos);
						String upper = "";

						int bl;

						bl = pos.offset(0, 1, 0).getId(world);
						upper += (bl == steel ? "S" : (bl == chest ? "C" : "?"));
						bl = pos.offset(1, 1, 0).getId(world);
						upper += (bl == steel ? "S" : (bl == chest ? "C" : "?"));
						bl = pos.offset(1, 1, 1).getId(world);
						upper += (bl == steel ? "S" : (bl == chest ? "C" : "?"));
						bl = pos.offset(0, 1, 1).getId(world);
						upper += (bl == steel ? "S" : (bl == chest ? "C" : "?"));

						// valid bottom layer
						// find direction.
						if (upper.equals("SCCS")) {
							if (spawnMinerAt(world, pos, 0)) {
								itemstack.damageItem(1, entityplayer);
							} else {
								PC_Utils.chatMsg(eMinerCrystals, false);
							}
							return true;
						} else if (upper.equals("CCSS")) {
							if (spawnMinerAt(world, pos, 3)) {
								itemstack.damageItem(1, entityplayer);
							} else {
								PC_Utils.chatMsg(eMinerCrystals, false);
							}
							return true;
						} else if (upper.equals("CSSC")) {
							if (spawnMinerAt(world, pos, 2)) {
								itemstack.damageItem(1, entityplayer);
							} else {
								PC_Utils.chatMsg(eMinerCrystals, false);
							}
							return true;
						} else if (upper.equals("SSCC")) {
							if (spawnMinerAt(world, pos, 1)) {
								itemstack.damageItem(1, entityplayer);
							} else {
								PC_Utils.chatMsg(eMinerCrystals, false);
							}
							return true;
						}

						PC_Utils.chatMsg(eMinerStructure, false);

						break miner_build_loop;
					}
				}
			}
		}

		return false;
	}


	/**
	 * Remove the Miner's blocks before spawning.<br>
	 * Coordinates given is the x- z- block.
	 * 
	 * @param world the world
	 * @param pos miner's X- Y- Z- block position
	 */
	private void removeSpawnStructure(World world, PC_CoordI pos) {
		for (int x = 0; x <= 1; x++) {
			for (int z = 0; z <= 1; z++) {
				for (int y = 0; y <= 1; y++) {
					pos.offset(x, y, z).setBlock(world, 0, 0);
				}
			}
		}
	}


	/**
	 * Spawn miner at given position.<br>
	 * Position = X- Z- block of the build.
	 * 
	 * @param world the world
	 * @param pos X- Y- Z- position
	 * @param rot miner rotation
	 * @return success
	 */
	private boolean spawnMinerAt(World world, PC_CoordI pos, int rot) {
		minerBeingCreated = true; // disable crystal counting.

		System.out.println("x-,z-,y- coord = " + pos);

		IInventory inv = null;

		find_chest_loop:
		for (int x = pos.x; x <= pos.x + 1; x++) {
			for (int z = pos.z; z <= pos.z + 1; z++) {
				inv = PC_InvUtils.getCompositeInventoryAt(world, new PC_CoordI(x, pos.y + 1, z));
				if (inv != null) {
					break find_chest_loop;
				}
			}
		}

		if (inv == null) {
			System.out.println("no chest");
			return false;
		}

		int cnt = PC_InvUtils.countPowerCrystals(inv);

		if (cnt == 0) {
			return false;
		}

		// move contents.
		PC_InvUtils.moveStacks(inv, this);

		// remove blocks.
		removeSpawnStructure(world, pos);

		// update level.
		minerBeingCreated = false;
		closeChest();

		setLocationAndAngles((double) pos.x + 1, pos.y, (double) pos.z + 1, (rot * 90F), 0.0F);
		target = new PC_CoordI(pos.x + 1, pos.y, pos.z + 1);
		world.spawnEntityInWorld(this);
		return true;
	}


	private void updateLevel() {
		if (!minerBeingCreated) {
			int cnt = PC_InvUtils.countPowerCrystals(this);
			if (cnt == 0) {
				turnIntoBlocks();
				return;
			}

			level = Math.min(cnt, 8);

			bridgeEnabled &= (level >= 3);
			waterFillingEnabled &= (level >= 6);
			lavaFillingEnabled &= (level >= 4);
		}
	}



	/**
	 * Despawn the miner, recreate build structure at it's position; Called when
	 * miner is killed or "to blocks" key is pressed
	 */
	private void turnIntoBlocks() {
		minerBeingCreated = true;
		int xh = (int) Math.round(posX);
		int y = (int) Math.floor(posY + 0.0001F);
		int zh = (int) Math.round(posZ);
		int yaw = (rotationYaw < 45 || rotationYaw > 315) ? 0 : (rotationYaw < 135 ? 1 : (rotationYaw < 215 ? 2 : (rotationYaw < 315 ? 3 : 0)));

		int xl = xh - 1, zl = zh - 1;

		// building chests
		for (int x = xl; x <= xh; x++) {
			for (int z = zl; z <= zh; z++) {
				worldObj.setBlockWithNotify(x, y, z, Block.blockSteel.blockID);
				if ((yaw == 0 && x == xh) || (yaw == 1 && z == zh) || (yaw == 2 && x == xl) || (yaw == 3 && z == zl)) {
					worldObj.setBlockWithNotify(x, y + 1, z, Block.chest.blockID);
				} else {
					worldObj.setBlockWithNotify(x, y + 1, z, Block.blockSteel.blockID);
				}
			}
		}

		IInventory inv = null;

		test:
		for (int x = xl; x <= xh; x++) {
			for (int k = zl; k <= zh; k++) {
				inv = PC_InvUtils.getCompositeInventoryAt(worldObj, new PC_CoordI(x, y + 1, k));
				if (inv != null) {
					break test;
				}
			}
		}

		if (inv != null) {
			PC_InvUtils.moveStacks(this, inv);
		} else {
			PC_Logger.warning("Despawning miner - the chest blocks weren't found.");
		}

		setDead();

		// replace opened gui with chest.
		if (openedGui) {
			ModLoader.getMinecraftInstance().thePlayer.closeScreen();
			ModLoader.openGUI(ModLoader.getMinecraftInstance().thePlayer, new GuiChest(ModLoader.getMinecraftInstance().thePlayer.inventory, inv));
		}

	}

	/** Fuel strength multiplier. It's also affected by level. */
	private static final double FUEL_STRENGTH = 0.9D;

	// these are probably useless
	private double velocityX;
	private double velocityY;
	private double velocityZ;


	/**
	 * Create miner in world.
	 * 
	 * @param world the world
	 */
	public PCmo_EntityMiner(World world) {
		super(world);
		preventEntitySpawning = true;
		setSize(1.3F, 1.4F);
		yOffset = 0F;
		fakePlayer = new PC_FakePlayer(world);
		entityCollisionReduction = 1.0F;
		stepHeight = 0.6F;
		isImmuneToFire = true;
	}

	/**
	 * Create miner in world, at given position
	 * 
	 * @param world the world
	 * @param dx pos X
	 * @param dy pos Y
	 * @param dz pos Z
	 */
	public PCmo_EntityMiner(World world, double dx, double dy, double dz) {
		this(world);
		setPosition(dx, dy + yOffset, dz);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = dx;
		prevPosY = dy;
		prevPosZ = dz;

		target.x = (int) dx;
		target.y = (int) dy;
		target.z = (int) dz;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	// this is used for hit timer and breaking animations.
	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(1));
		dataWatcher.addObject(19, new Integer(0));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		// if (entity instanceof EntityItem || entity instanceof EntityXPOrb) { return null; }
		return entity.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	// useless as miner can't be mounted
	@Override
	public double getMountedYOffset() {
		return 1D;
	}

	// returns true if in water.
	@Override
	public boolean handleWaterMovement() {
		return worldObj.isMaterialInBB(boundingBox.expand(-0.10000000149011612D, -0.40000000596046448D, -0.10000000149011612D), Material.water);
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		// all but void and explosion is ignored.
		if (damagesource != DamageSource.outOfWorld && (worldObj.isRemote || isDead || (damagesource.getSourceOfDamage() == null && damagesource != DamageSource.explosion))) {
			return true;
		}
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() + i * 7);
		setBeenAttacked();
		if (getDamageTaken() > 40) {
			if (riddenByEntity != null) {
				riddenByEntity.mountEntity(this); // unmount
			}

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
		return !isDead;
	}

	// god knows what is this for
	@Override
	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i) {
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	@Override
	public void setVelocity(double d, double d1, double d2) {
		motionX = d;
		motionY = d1;
		motionZ = d2;
	}

	@Override
	public float getShadowSize() {
		return 1.0F;
	}



	// === PROGRAMMING UTILS ===

	private Random rand = new Random();

	/** Fake player instance used for block mining */
	private EntityPlayer fakePlayer;



	// STATUS VARIABLES and FLAGS

	/** List of commands waiting for execution */
	public String commandList = "";
	/** Command currently being processed */
	public int currentCommand = -1;
	/**
	 * The real command executed.<br>
	 * For example when executing "U" command and is already on half step,<br>
	 * "F" command is executed instead.
	 */
	private int realCommand = -1;
	/**
	 * Steps remaining to complete current command<br>
	 * Used for commands like "100", instead of inserting 100 times "F"
	 */
	private int stepCounter = 0;
	/** Command list saved when the program is paused. */
	public String commandListSaved = "";
	/** Flag: Is program execution paused? */
	public boolean paused = false;
	/** Flag: Is connected to keyboard? */
	public boolean keyboardControlled = false;
	/** Flag: Is the programming GUI open? -> Ignore keyboard control */
	public boolean programmingGuiOpen = false;

	/** Flag: the half step was already laid for this move (up) */
	private boolean upStepLaid = false;
	/** Flag: bridge building was already finished for thsi move */
	private boolean bridgeDone = false;

	/** Target position to check if command is finished */
	private PC_CoordI target = new PC_CoordI();
	/** Rotation in degrees remaining to complete current rotation command */
	private int rotationRemaining = 0;

	/**
	 * The miner's level.<br>
	 * Calculated from count of PowerCrystals.
	 */
	public int level = 1;

	/** Speed based on level. */
	private static final double[] MOTION_SPEED = { 0.04D, 0.05D, 0.06D, 0.07D, 0.08D, 0.09D, 0.11D, 0.12D };
	/**
	 * Flag that this entity is being created and inventory should not be
	 * checked for Power Crystals.
	 */
	public boolean minerBeingCreated = false;



	// CONFIGURATION FROM CONSOLE

	/** <b>cfg</b> program in the upper edit box of console screen */
	public String program = "";
	/** <b>cfg</b> Flags used to destroy certain types of blocks. */
	public byte DESTROY = 0;
	public static final byte COBBLE = 1, DIRT = 1 << 1, GRAVEL = 1 << 2;
	/** <b>cfg</b> Should keep all fuel when depositing? */
	public boolean keepAllFuel = false;
	/** <b>cfg</b> Place torches only on floor - good for large rooms. */
	public boolean torchesOnlyOnFloor = false;
	/** <b>cfg</b> Compress collected blocks to storage blocks if possible */
	public boolean compressBlocks = false;
	/** <b>cfg</b> Mining enabled */
	public boolean miningEnabled = true;
	/** <b>cfg</b> Bridge enabled. */
	public boolean bridgeEnabled = false;
	/** <b>cfg</b> Lava filling enabled */
	public boolean lavaFillingEnabled = true;
	/** <b>cfg</b> Water filling enabled */
	public boolean waterFillingEnabled = false;




	/** Fuel consumed from items and waiting for usage */
	private int fuelBuffer = 0;
	/**
	 * Fuel allocated for current operation.<br>
	 * This fuel is already in the buffer, but won't be consumed until the
	 * operation is really finished.<br>
	 * that prevents fuel consumption when miner hits something it can't mine.
	 */
	private int fuelAllocated = 0;
	/**
	 * Fuel needed to execute current command - wonẗ move until some fuel is
	 * added
	 */
	private int waitingForFuel = 0;

	/**
	 * Mining progress counter.<br>
	 * <ul>
	 * <li>0,1,2,3 - blocks in front of miner
	 * <li>4,5 - blocks in front of and below miner - for "Down" command
	 * <li>6,7,8,9,10,11 - blocks mined during "Up" command
	 * </ul>
	 * <br>
	 * Values:
	 * <ul>
	 * <li>-1 - mining scheduled but not started yet
	 * <li>0 - mining finished
	 * <li>>0 - ticks remaining
	 * </ul>
	 */
	private int[] mineCounter = { -1, -1, -1, -1, /* under */0, 0, /* above */0, 0, /* top */0, 0, 0, 0 };

	/**
	 * Mining sound counter.<br>
	 * Sound is played when reaches zero, to prevent insane noise.
	 */
	private int miningTickCounter = 0;

	/**
	 * Should this item stack be destroyed?<br>
	 * (from console screen)
	 * 
	 * @param stack the stack collected
	 * @return destroy it
	 */
	private boolean shouldDestroyStack(ItemStack stack) {
		if (stack == null) {
			return true;
		}
		if (stack.itemID == Block.cobblestone.blockID) {
			return (DESTROY & COBBLE) != 0;
		}
		if (stack.itemID == Block.dirt.blockID) {
			return (DESTROY & DIRT) != 0;
		}
		if (stack.itemID == Block.gravel.blockID) {
			return (DESTROY & GRAVEL) != 0;
		}
		return false;
	}

	/**
	 * Set flags for block destruction.<br>
	 * Used by console screen.
	 * 
	 * @param flags the flags
	 */
	public void setDestroyFlags(byte flags) {
		DESTROY = flags;
	}

	/**
	 * Get flags for block destruction.<br>
	 * Used by console screen.
	 * 
	 * @return the flags
	 */
	public byte getDestroyFlags() {
		return DESTROY;
	}

	/**
	 * Reset:
	 * <ul>
	 * <li>motion
	 * <li>commands list
	 * <li>current command
	 * <li>mine counter
	 * <li>align to blocks and get ready for next keyboard command.
	 * </ul>
	 * Typically called after "DELETE" key is pressed.
	 */
	public void resetEverything() {
		motionX = motionZ = 0;
		commandList = "";
		commandListSaved = "";
		currentCommand = -1;
		alignToBlocks();
		paused = false;
		resetStatus();
	}

	/**
	 * Pause program execution when the GUI is opened for running Miner.
	 */
	public void pauseProgram() {
		if (paused) {
			return;
		}

		// pack last instruction into saved codebuffer
		commandListSaved = new String(commandList);
		String instruction = Character.toString(PCmo_Command.getCharFromInt(currentCommand));
		if (stepCounter > 0 && PCmo_Command.isCommandMove(currentCommand)) {
			instruction = (currentCommand == PCmo_Command.FORWARD ? "" : "-") + Integer.toString(stepCounter);
		} else if (instruction.equals("?")) {
			instruction = "";
		}

		commandListSaved = instruction + " " + commandListSaved;
		commandList = "";

		resetStatus();
		paused = true;
	}

	/**
	 * Resume program after GUI is closed.<br>
	 * If not paused, do nothing.
	 */
	public void resumeProgram() {
		if (!paused) {
			return;
		}

		resetStatus();
		commandList = new String(commandListSaved.trim());
		commandListSaved = "";

		paused = false;
	}

	/**
	 * Disconnect from keyboard, parse program, reset status and start program
	 * execution.
	 * 
	 * @throws PCmo_CommandException when the program contains errors.
	 */
	public void runNewProgram() throws PCmo_CommandException {
		String parsed = PCmo_Command.parseCode(program);

		keyboardControlled = false;
		paused = false;
		PCmo_MinerControlHandler.disconnectMinerFromKeyboardControl(this, true);

		alignToBlocks();
		resetStatus();

		commandList = parsed;
		commandListSaved = "";
	}


	/**
	 * Reset various status flags and counters, but keep fuel buffer.
	 */
	private void resetStatus() {
		currentCommand = -1;
		roundRotation();
		target.x = (int) posX;
		target.z = (int) posZ;
		resetMineCounter();
		stepCounter = 0;
		waitingForFuel = 0;
		fuelAllocated = 0;
		bridgeDone = false;
		upStepLaid = false;
	}

	/**
	 * Set keyboard control status
	 * 
	 * @param yes keyboard control enabled
	 */
	public void setKeyboardControl(boolean yes) {
		keyboardControlled = yes;
		if (yes) {
			pauseProgram();
			PCmo_MinerControlHandler.setMinerForKeyboardControl(this, false); // not silent
		} else {
			PCmo_MinerControlHandler.disconnectMinerFromKeyboardControl(this, false); // not silent
			resumeProgram();
		}
	}

	/**
	 * @return is miner ready for keyboard command
	 */
	public boolean canReceiveKeyboardCommand() {
		if (openedGui) {
			return false;
		}
		commandList = commandList.trim();
		return true;
	}

	/**
	 * Keyboard command sent to this miner
	 * 
	 * @param i command index
	 */
	public void receiveKeyboardCommand(int i) {
		if (i == PCmo_Command.RUN_PROGRAM) {
			resetEverything();
			setKeyboardControl(false);
			try {
				runNewProgram();
			} catch (PCmo_CommandException e) {}
		}
		if (i == PCmo_Command.RESET) {
			resetEverything();
		} else {
			Character chr = PCmo_Command.getCharFromInt(i);
			if (chr.equals('?')) {
				return;
			}
			commandList += chr.toString();
		}
	}

	/**
	 * Append given code to the command slist.<br>
	 * Used for "turn around" command, which sends RR.
	 * 
	 * @param code code to append
	 * @throws PCmo_CommandException if code is invalid
	 */
	public void appendCode(String code) throws PCmo_CommandException {
		commandList = commandList + " " + PCmo_Command.parseCode(code);
	}

	/**
	 * Put code to the commands list.
	 * 
	 * @param code the code to parse and start
	 * @throws PCmo_CommandException if code is invalid
	 */
	public void setCode(String code) throws PCmo_CommandException {
		commandList = PCmo_Command.parseCode(code);
	}

	/**
	 * Get next command from buffer or step cound, prepare for execution and
	 * start it.
	 * 
	 * @return the command, or -1 if buffer is empty
	 */
	private int getNextCommand() {
		if (commandList.length() > 0) {
			Character first = commandList.charAt(0);

			int cmd = PCmo_Command.getIntFromChar(first);
			if (cmd != -1) {
				commandList = commandList.substring(1);
				if (cmd == PCmo_Command.FORWARD || cmd == PCmo_Command.BACKWARD || cmd == PCmo_Command.UP) {
					stepCounter = 1;
				}
				return cmd;
			} else if (Character.isDigit(first) || first.equals('-')) {
				String numbuff = Character.toString(first);
				commandList = commandList.substring(1);

				while (commandList.length() > 0) {
					first = Character.valueOf(commandList.charAt(0));

					if (Character.isDigit(first)) {
						numbuff += first.toString();
					} else {
						break;
					}
					commandList = commandList.substring(1);
				}

				try {
					stepCounter = Integer.valueOf(numbuff);
					cmd = stepCounter > 0 ? PCmo_Command.FORWARD : PCmo_Command.BACKWARD;

					stepCounter = Math.abs(stepCounter);
					if (stepCounter == 0) {
						return -1;
					}

					return cmd;

				} catch (NumberFormatException nfe) {
					return -1;
				}
			} else {
				commandList = commandList.substring(1);
				return getNextCommand();
			}
		}
		return -1;
	}

	// === EXECUTION AND STATUS UTILS ===

	/**
	 * Check if miner has at the target coordinates, which indicates that MOVE
	 * command was finished.
	 * 
	 * @return true if miner is at target pos
	 */
	private boolean isMinerAtTargetPos() {
		if (currentCommand == PCmo_Command.FORWARD || currentCommand == PCmo_Command.UP) {
			if (rotationYaw == 0) {
				return posX <= target.x;
			}
			if (rotationYaw == 90) {
				return posZ <= target.z;
			}
			if (rotationYaw == 180) {
				return posX >= target.x;
			}
			if (rotationYaw == 270) {
				return posZ >= target.z;
			}
		} else if (currentCommand == PCmo_Command.BACKWARD) {
			if (rotationYaw == 0) {
				return posX >= target.x;
			}
			if (rotationYaw == 90) {
				return posZ >= target.z;
			}
			if (rotationYaw == 180) {
				return posX <= target.x;
			}
			if (rotationYaw == 270) {
				return posZ <= target.z;
			}
		}
		return true;
	}

	/**
	 * Get Miner's absolute distance to the target X coordinate
	 * 
	 * @return distance
	 */
	private double getTargetDistanceX() {
		return Math.abs(posX - target.x);
	}

	/**
	 * Get Miner's absolute distance to the target Z coordinate
	 * 
	 * @return distance
	 */
	private double getTargetDistanceZ() {
		return Math.abs(posZ - target.z);
	}

	/**
	 * Round rotation to 0, 90, 180 or 270 degrees.
	 */
	private void roundRotation() {

		if (rotationYaw < 0) {
			rotationYaw = prevRotationYaw = 360F - rotationYaw;
		}
		if (rotationYaw > 360F) {
			rotationYaw = prevRotationYaw = rotationYaw - 360F;
		}

		if (rotationYaw >= 315 || rotationYaw < 45) {
			prevRotationYaw = rotationYaw = 0;
		}
		if (rotationYaw >= 45 && rotationYaw < 135) {
			prevRotationYaw = rotationYaw = 90;
		}
		if (rotationYaw >= 135 && rotationYaw < 215) {
			prevRotationYaw = rotationYaw = 180;
		}
		if (rotationYaw >= 215 && rotationYaw < 315) {
			prevRotationYaw = rotationYaw = 270;
		}
		rotationRemaining = 0;
	}

	/**
	 * Move to rounded position (round X and Z)
	 */
	private void alignToBlocks() {
		setPosition(Math.round(posX), posY, Math.round(posZ));
	}

	/**
	 * Get ready for command's execution, and if possible, execute it right now.
	 */
	private void prepareForCommandExecution() {
		if (currentCommand > -1) {
			realCommand = currentCommand;
			prevPosX = posX = (int) Math.round(posX);
			prevPosX = posZ = (int) Math.round(posZ);
			int x = (int) Math.round(posX);
			int z = (int) Math.round(posZ);
			int y = (int) Math.floor(posY + 0.0001F);

			roundRotation();

			if (currentCommand == PCmo_Command.DEPOSIT) {
				depositToNearbyChest();
				currentCommand = -1;

			} else if (currentCommand == PCmo_Command.DISASSEMBLY) {
				turnIntoBlocks();
				return;
			} else if (currentCommand == PCmo_Command.BRIDGE_ENABLE) {

				bridgeEnabled = true;
				currentCommand = -1;
			} else if (currentCommand == PCmo_Command.BRIDGE_DISABLE) {
				bridgeEnabled = false;
				currentCommand = -1;

			} else if (currentCommand == PCmo_Command.MINING_ENABLE) {
				miningEnabled = true;
				currentCommand = -1;
			} else if (currentCommand == PCmo_Command.MINING_DISABLE) {
				miningEnabled = false;
				currentCommand = -1;

			} else if (currentCommand == PCmo_Command.LAVA_ENABLE) {
				lavaFillingEnabled = true;
				currentCommand = -1;
			} else if (currentCommand == PCmo_Command.LAVA_DISABLE) {
				lavaFillingEnabled = false;
				currentCommand = -1;

			} else if (currentCommand == PCmo_Command.WATER_ENABLE) {
				waterFillingEnabled = true;
				currentCommand = -1;
			} else if (currentCommand == PCmo_Command.WATER_DISABLE) {
				waterFillingEnabled = false;
				currentCommand = -1;

			} else if (currentCommand == PCmo_Command.DOWN) {
				if (!miningEnabled) {
					currentCommand = -1;
				} else {
					resetMineCounter();
					mineCounter[4] = -1;
					mineCounter[5] = -1;
				}

			} else if (currentCommand == PCmo_Command.UP) {
				if (!miningEnabled) {
					currentCommand = -1;
				} else {
					if (addFuelForCost(getStepCost())) {

						resetMineCounter();
						if (rotationYaw == 0) {
							target = new PC_CoordI(x - 1, y, z);
						}
						if (rotationYaw == 90) {
							target = new PC_CoordI(x, y, z - 1);
						}
						if (rotationYaw == 180) {
							target = new PC_CoordI(x + 1, y, z);
						}
						if (rotationYaw == 270) {
							target = new PC_CoordI(x, y, z + 1);
						}

						if (!isOnHalfStep()) {
							mineCounter[6] = -1;
							mineCounter[7] = -1;
							mineCounter[8] = -1;
							mineCounter[9] = -1;
							mineCounter[10] = -1;
							mineCounter[11] = -1;

							upStepLaid = false;
						} else {
							currentCommand = PCmo_Command.FORWARD;

							// lay stepblock.
							switch ((int) Math.floor(rotationYaw)) {
								case 0:
									layHalfStep(x - 2, y, z - 1, false);
									layHalfStep(x - 2, y, z, false);
									break;

								case 90:
									layHalfStep(x - 1, y, z - 2, false);
									layHalfStep(x, y, z - 2, false);
									break;

								case 180:
									layHalfStep(x + 1, y, z - 1, false);
									layHalfStep(x + 1, y, z, false);
									break;

								case 270:
									layHalfStep(x - 1, y, z + 1, false);
									layHalfStep(x, y, z + 1, false);
									break;
							}

						}

					}
				}

			} else if (currentCommand == PCmo_Command.FORWARD) {
				if (addFuelForCost(getStepCost())) {
					resetMineCounter();
					bridgeDone = false;
					if (rotationYaw == 0) {
						target.x = x - 1;
						target.z = z;
					}
					if (rotationYaw == 90) {
						target.z = z - 1;
						target.x = x;
					}
					if (rotationYaw == 180) {
						target.x = x + 1;
						target.z = z;
					}
					if (rotationYaw == 270) {
						target.z = z + 1;
						target.x = x;
					}
				}

			} else if (currentCommand == PCmo_Command.BACKWARD) {
				if (addFuelForCost(getStepCost())) {
					bridgeDone = false;
					if (rotationYaw == 0) {
						target.x = x + 1;
						target.z = z;
					}
					if (rotationYaw == 90) {
						target.z = z + 1;
						target.x = x;
					}
					if (rotationYaw == 180) {
						target.x = x - 1;
						target.z = z;
					}
					if (rotationYaw == 270) {
						target.z = z - 1;
						target.x = x;
					}
				}

			} else if (currentCommand == PCmo_Command.LEFT) {

				rotationRemaining = -90;

			} else if (currentCommand == PCmo_Command.RIGHT) {

				rotationRemaining = 90;

			} else if (currentCommand == PCmo_Command.NORTH) {

				if (rotationYaw == 0) {
					currentCommand = PCmo_Command.RIGHT;
					rotationRemaining = 90;
				}
				if (rotationYaw == 180) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = -90;
				}
				if (rotationYaw == 90) {
					currentCommand = -1;
				}
				if (rotationYaw == 270) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}

			} else if (currentCommand == PCmo_Command.SOUTH) {

				if (rotationYaw == 0) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = -90;
				}
				if (rotationYaw == 180) {
					currentCommand = PCmo_Command.RIGHT;
					rotationRemaining = 90;
				}
				if (rotationYaw == 90) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 270) {
					currentCommand = -1;
				}

			} else if (currentCommand == PCmo_Command.EAST) {

				if (rotationYaw == 0) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 180) {
					currentCommand = -1;
				}
				if (rotationYaw == 90) {
					currentCommand = PCmo_Command.RIGHT;
					rotationRemaining = 90;
				}
				if (rotationYaw == 270) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = -90;
				}

			} else if (currentCommand == PCmo_Command.WEST) {

				if (rotationYaw == 0) {
					currentCommand = -1;
				}
				if (rotationYaw == 180) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 90) {
					currentCommand = PCmo_Command.LEFT;
					rotationRemaining = -90;
				}
				if (rotationYaw == 270) {
					currentCommand = PCmo_Command.RIGHT;
					rotationRemaining = 90;
				}

			} else {
				currentCommand = -1;
			}
		}
	}

	/**
	 * Get price (in fuel points) for one step.<br>
	 * It's equal to half of current level.
	 * 
	 * @return step cost
	 */
	private int getStepCost() {
		return MathHelper.clamp_int(level / 2, 1, 4);
	}

	/**
	 * Look if there is any player who may appreciate the awesome effects and
	 * sounds.
	 * 
	 * @return
	 */
	private boolean shouldMakeEffects() {
		return worldObj.getClosestPlayerToEntity(this, 17D) != null && mod_PCcore.soundsEnabled;
	}

	/**
	 * Play the "ticking" sound made by miner's tracks.
	 */
	private void playMotionEffect() {
		if (!shouldMakeEffects()) {
			return;
		}
		worldObj.playSoundAtEntity(this, "random.click", 0.02F, 0.8F);
	}

	/**
	 * Spawn breaking particles for blockparticles
	 * 
	 * @param x block x
	 * @param y block y
	 * @param z block z
	 * @param block_index index of mined block, equal to it's index in
	 *            MineCounter.
	 */
	private void playMiningEffect(PC_CoordI pos, int block_index) {
		miningTickCounter++;

		if (!shouldMakeEffects()) {
			return;
		}
		int id = pos.getId(worldObj);

		Block block = Block.blocksList[id];
		if (miningTickCounter % 8 == 0 && block != null) {
			ModLoader.getMinecraftInstance().sndManager.playSound(block.stepSound.getBreakSound(), pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
		}

		if (block != null) {
			ModLoader.getMinecraftInstance().effectRenderer.addBlockHitEffects(pos.x, pos.y, pos.z, block_index < 4 ? getSideFromYaw() : (block_index < 6 ? 1 : 0));
		}
	}

	/**
	 * Convert "rotation yaw" angle to block side index.
	 * 
	 * @return
	 */
	private int getSideFromYaw() {
		if (rotationYaw == 0) {
			return 5;
		}
		if (rotationYaw == 90) {
			return 3;
		}
		if (rotationYaw == 180) {
			return 4;
		}
		if (rotationYaw == 270) {
			return 2;
		}
		return 1;
	}

	/**
	 * Perform block harvesting, drop the item, remove block and play sound.
	 * 
	 * @param pos
	 */
	private void harvestBlock_do(PC_CoordI pos) {
		int id = pos.getId(worldObj);
		int meta = pos.getMeta(worldObj);

		if (!shouldIgnoreBlockForHarvesting(pos, id)) {

			// block implementing ICropBlock
			if (Block.blocksList[id] instanceof PC_ICropBlock) {
				if (!((PC_ICropBlock) Block.blocksList[id]).isMature(worldObj, pos)) {
					return;
				} else {
					// play breaking sound and animation
					if (shouldMakeEffects()) {
						worldObj.playAuxSFX(2001, pos.x, pos.y, pos.z, id + (meta << 12));
					}
					for (ItemStack stack : ((PC_ICropBlock) Block.blocksList[id]).machineHarvest(worldObj, pos)) {
						Block.blocksList[id].dropBlockAsItem_do(worldObj, pos.x, pos.y, pos.z, stack);
					}
				}

			} else if (PC_CropHarvestingManager.isBlockRegisteredCrop(id)) {
				if (PC_CropHarvestingManager.canHarvestBlock(id, meta)) {

					ItemStack[] harvested = PC_CropHarvestingManager.getHarvestedStacks(id, meta);

					for (ItemStack stack : harvested) {

						// play breaking sound and animation
						if (shouldMakeEffects()) {
							worldObj.playAuxSFX(2001, pos.x, pos.y, pos.z, id + (meta << 12));
						}

						Block.blocksList[id].dropBlockAsItem_do(worldObj, pos.x, pos.y, pos.z, stack);

					}

					int newMeta = PC_CropHarvestingManager.getReplantMeta(id);

					if (newMeta == -1) {
						worldObj.setBlockWithNotify(pos.x, pos.y, pos.z, 0);
					} else {
						worldObj.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, newMeta);
					}

					return;

				}

			} else {

				Block.blocksList[id].harvestBlock(worldObj, fakePlayer, pos.x, pos.y, pos.z, meta);
				pos.setBlock(worldObj, 0, 0);
				if (shouldMakeEffects()) {
					worldObj.playAuxSFX(2001, pos.x, pos.y, pos.z, id + (meta << 12));
				}

			}
		}
	}

	/**
	 * Perform mining update of given block.
	 * 
	 * @param pos miner coordinate
	 * @param loc block position index.
	 */
	private void performMiningUpdate(PC_CoordI pos, int loc) {
		int id = pos.getId(worldObj);

		if (loc == 4 || loc == 5) {
			bridgeBuilding_do(pos.offset(0, -1, 0));
		}

		if (mineCounter[loc] <= 0) {

			if (shouldIgnoreBlockForHarvesting(pos, id)) {
				if (mineCounter[loc] < 0) {
					mineCounter[loc] = 0;
				}
				return;
			}

			if (Block.blocksList[id] != null) {
				int cost = getBlockMiningCost(pos, id);
				if (id == 7 && level == 8 && pos.y == 0) {
					cost = -1;
				}

				if (cost > 0) {
					if (addFuelForCost(cost)) {
						mineCounter[loc] = cost;
					}
				}
			}

		}

		if (waitingForFuel == 0 && mineCounter[loc] > 0) {
			int step = level;
			if (mineCounter[loc] < step) {
				step = mineCounter[loc];
				mineCounter[loc] = 0;
			} else {
				mineCounter[loc] -= step;
			}

			consumeAllocatedFuel(step);

			if (mineCounter[loc] == 0) {
				harvestBlock_do(pos);
			}
		}

		if (mineCounter[loc] != 0 && Block.blocksList[id] != null) {
			playMiningEffect(pos, loc);
		}

	}

	/**
	 * @return is mining in progress
	 */
	private boolean isMiningInProgress() {
		for (int counter : mineCounter) {
			if (counter != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return is mining finished
	 */
	private boolean isMiningDone() {
		return !isMiningInProgress();
	}

	/**
	 * Reset mining counters
	 */
	private void resetMineCounter() {
		for (int element : mineCounter) {
			fuelAllocated -= element;
			if (fuelAllocated <= 0) {
				fuelAllocated = 0;
				break;
			}
		}

		for (int i = 0; i < 4; i++) {
			mineCounter[i] = -1;
		}
		for (int i = 4; i < mineCounter.length; i++) {
			mineCounter[i] = 0;
		}
	}

	/**
	 * Check if block is unharvestable
	 * 
	 * @param pos
	 * @param id block id
	 * @return is not harvested
	 */
	private boolean shouldIgnoreBlockForHarvesting(PC_CoordI pos, int id) {

		if (id == 0 || Block.blocksList[id] == null || Block.blocksList[id] instanceof BlockTorch || id == Block.fire.blockID || id == Block.portal.blockID || id == Block.endPortal.blockID || Block.blocksList[id] instanceof BlockFluid || id == 55
				|| id == 70 || id == 72 || PC_BlockUtils.hasFlag(worldObj, pos, "LIFT") || PC_BlockUtils.hasFlag(worldObj, pos, "BELT")) {
			return true;
		}

		boolean flag = false;
		if (Block.blocksList[id] instanceof PC_ICropBlock) {
			flag = !((PC_ICropBlock) Block.blocksList[id]).isMature(worldObj, pos);
		}
		if (PC_CropHarvestingManager.isBlockRegisteredCrop(id)) {
			flag = !PC_CropHarvestingManager.canHarvestBlock(id, pos.getMeta(worldObj));
		}

		if (flag && Block.blocksList[id].getCollisionBoundingBoxFromPool(worldObj, pos.x, pos.y, pos.z) == null) {
			return true;
		}

		return false;

	}

	/**
	 * Check if miner stops at this block.
	 * 
	 * @param id block ID
	 * @return is unbreakable
	 */
	private boolean isBlockUnbreakable(int id) {
		return id == 7;
	}

	/**
	 * Check if miner is able to break given block.
	 * 
	 * @param pos
	 * @param id block id
	 * @return can break
	 */
	private boolean canHarvestBlockWithCurrentLevel(PC_CoordI pos, int id) {
		// exception - miner 8 can mine bedrock.
		if (id == 7 && level == 8) {
			return true;
		}
		if (isBlockUnbreakable(id) || PC_BlockUtils.hasFlag(worldObj, pos, "HARVEST_STOP")) {
			return false;
		}
		switch (level) {
			case 1: // all but rocks and iron
				return Block.blocksList[id].blockMaterial != Material.rock && Block.blocksList[id].blockMaterial != Material.iron && id != mod_PCcore.powerCrystal.blockID;
			case 2: // everything but precious ores (cobble, coal, iron)
				return id != 49 && id != 14 && id != 21 && id != 22 && id != 41 && id != 56 && id != 57 && id != 73 && id != 74 && id != mod_PCcore.powerCrystal.blockID;
			case 3: // all but diamonds + obsidian + power crystals
				return id != 49 && id != 56 && id != 57 && id != mod_PCcore.powerCrystal.blockID;
			case 4: // all but obsidian
				return id != 49;
			case 5:
			case 6:
			case 7:
			case 8:
				return true;
		}
		return false;
	}

	/**
	 * Get block mining price.
	 * 
	 * @param pos position
	 * @param id block ID
	 * @return block's mining cost in fuel points.
	 */
	private int getBlockMiningCost(PC_CoordI pos, int id) {
		if (!canHarvestBlockWithCurrentLevel(pos, id)) {
			return -1;
		}
		if (shouldIgnoreBlockForHarvesting(pos, id)) {
			return 0;
		}

		// dirt, gravel, sand, non-rocks.
		if (Block.blocksList[id].blockMaterial != Material.rock && Block.blocksList[id].blockMaterial != Material.iron) {
			return 10;
		}
		if (id == 73 || id == 74 || id == 21 || id == 14) {
			return 100;// redstone,lapis,gold
		}
		if (id == 16 || id == 15 || id == 42 || id == 98) {
			return 30;// coal,iron,stonebrick
		}
		if (id == 56 || id == 57 || id == 14) {
			return 150; // diamond
		}
		if (id == 49) {
			return 600;
		}
		if (id == 7) {
			return 2000;
		}
		if (id == mod_PCcore.powerCrystal.blockID) {
			return 100;
		}

		return 20;
	}

	/**
	 * Check if given location is empty.<br>
	 * Coord is X+ Z+ Y- corner of miner
	 * 
	 * @param pos
	 * @return is empty
	 */
	private boolean isLocationEmpty(PC_CoordI pos) {
		boolean notempty = false;
		notempty |= !checkIfAir(pos.offset(0, 0, 0), true);
		notempty |= !checkIfAir(pos.offset(-1, 0, 0), true);
		notempty |= !checkIfAir(pos.offset(0, 0, -1), true);
		notempty |= !checkIfAir(pos.offset(-1, 0, -1), true);
		notempty |= !checkIfAir(pos.offset(0, 1, 0), false);
		notempty |= !checkIfAir(pos.offset(-1, 1, 0), false);
		notempty |= !checkIfAir(pos.offset(0, 1, -1), false);
		notempty |= !checkIfAir(pos.offset(-1, 1, -1), false);


		return !notempty;

	}

	/**
	 * Check if block at given position is air, laid half step or non-colliding
	 * block.
	 * 
	 * @param pos position in world
	 * @param lower is it the lower block of miner's body
	 * @return is free to move
	 */
	private boolean checkIfAir(PC_CoordI pos, boolean lower) {
		int id = pos.getId(worldObj);

		if (lower && id == Block.stairSingle.blockID) {
			return true;
		}

		Block block = Block.blocksList[id];
		return block == null || block.getCollisionBoundingBoxFromPool(worldObj, pos.x, pos.y, pos.z) == null;
	}

	/**
	 * @return is miner standing on halfstep
	 */
	private boolean isOnHalfStep() {
		return (posY - Math.floor(posY + 0.0001)) >= 0.4D;
	}

	/**
	 * Place bridge blocks at target pos; target is X+ Z+.
	 * 
	 * @return false if it run out of material
	 */
	private boolean performBridgeBuilding() {
		if (!bridgeEnabled) {
			return true;
		}

		int y = (int) Math.floor(posY - 0.9999F);
		if (isOnHalfStep()) {
			return true;
		}
		if (!bridgeBuilding_do(target.offset(0, -1, 0))) {
			return false;
		}
		if (!bridgeBuilding_do(target.offset(-1, -1, 0))) {
			return false;
		}
		if (!bridgeBuilding_do(target.offset(0, -1, -1))) {
			return false;
		}
		if (!bridgeBuilding_do(target.offset(-1, -1, -1))) {
			return false;
		}

		return true;
	}

	/**
	 * Place bridge block at this exact position.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return success
	 */
	private boolean bridgeBuilding_do(PC_CoordI pos) {
		if (checkIfAir(pos, false)) {
			if (level < 3) {
				currentCommand = -1;
				return false;
			}
			ItemStack fill = getBlockForBuilding();
			if (fill == null) {
				return false;
			}

			int id = fill.itemID;
			int meta = fill.getItemDamage();
			pos.setBlock(worldObj, id, meta);

			if (shouldMakeEffects()) {
				worldObj.playSoundEffect(pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, Block.blocksList[id].stepSound.getStepSound(), (Block.blocksList[id].stepSound.getVolume() + 1.0F) / 2.0F, Block.blocksList[id].stepSound.getPitch() * 0.8F);
			}
		}
		return true;
	}


	/**
	 * Lay half step.<br>
	 * If already on step, check the block in front.<br>
	 * Smartly prevents falling into caves.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param step is already on half step.
	 */
	private void layHalfStep(int x, int y, int z, boolean step) {

		if (step) {
			if (worldObj.getBlockId(x, y, z) == 0) {
				int dmg = getDamageForHalfStep();
				if (dmg != -1) {
					worldObj.setBlockAndMetadataWithNotify(x, y, z, Block.stairSingle.blockID, dmg);
				}
			}
		}

		int id = worldObj.getBlockId(x, y + (step ? -1 : 0), z);
		if (id == 0 || id == 8 || id == 9 || id == 10 || id == 11 || Block.blocksList[id].getCollisionBoundingBoxFromPool(worldObj, x, y, z) == null) {
			ItemStack fill = getBlockForBuilding();
			if (fill == null) {
				return;
			}

			id = fill.itemID;
			int meta = fill.getItemDamage();
			worldObj.setBlockAndMetadataWithNotify(x, y + (step ? -1 : 0), z, id, meta);
			if (shouldMakeEffects()) {
				worldObj.playSoundEffect(x + 0.5F, (float) y + (step ? -1 : 0) + 0.5F, z + 0.5F, Block.blocksList[id].stepSound.getStepSound(), (Block.blocksList[id].stepSound.getVolume() + 1.0F) / 2.0F,
						Block.blocksList[id].stepSound.getPitch() * 0.8F);
			}
		}
	}

	/**
	 * Find block for halfstep and get it's id; TODO this method ignores
	 * metadata and newly you get two halfsteps from signel block.
	 * 
	 * @return metadata
	 */
	private int getDamageForHalfStep() {
		for (int pass = 0; pass < 3; pass++) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (isItemGoodForHalfStep(getStackInSlot(i), pass)) {
					ItemStack returned = decrStackSize(i, 1);

					if (returned.itemID == Block.stairSingle.blockID) {
						return returned.getItemDamage();
					}
					return getDamageFromMaterialForHalfStep(returned.itemID);
				}
			}
		}
		return -1;
	}

	/**
	 * Get meta of halfstep crafted from given block.
	 * 
	 * @param id block id
	 * @return meta
	 */
	private int getDamageFromMaterialForHalfStep(int id) {
		switch (id) {
			case 1:
				return 0;
			case 4:
				return 3;
			case 5:
				return 2;
			case 24:
				return 1;
			case 45:
				return 4;
			case 98:
				return 5;
		}
		return 3;
	}

	/**
	 * Check if stack can be crafted to halfstep.
	 * 
	 * @param is stack
	 * @param pass pass; 0 = stone, 1 = planks+smoothstone, 2 =
	 *            sandstone+stonebrick+brick.
	 * @return
	 */
	private boolean isItemGoodForHalfStep(ItemStack is, int pass) {
		if (is == null) {
			return false;
		}
		int id = is.itemID;

		if (pass == 0) {
			return id == Block.cobblestone.blockID || id == Block.stairSingle.blockID;
		}

		if (pass == 1) {
			return id == Block.planks.blockID || id == Block.stone.blockID;
		}

		if (pass == 2) {
			return id == Block.sandStone.blockID || id == Block.stoneBrick.blockID || id == Block.brick.blockID;
		}

		return false;
	}

	/**
	 * Get block from inventory good for building.
	 * 
	 * @return stack or null.
	 */
	private ItemStack getBlockForBuilding() {
		for (int pass = 0; pass < 3; pass++) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (isBlockGoodForBuilding(getStackInSlot(i), pass)) {
					return decrStackSize(i, 1);
				}
			}
		}
		return null;
	}

	/**
	 * Check if block is good for building.
	 * 
	 * @param is stack
	 * @param pass pass; 0 = cheap, 1 = better
	 * @return is good
	 */
	private boolean isBlockGoodForBuilding(ItemStack is, int pass) {
		if (is == null) {
			return false;
		}

		if (!(is.getItem() instanceof ItemBlock)) {
			return false;
		}

		int id = is.itemID;

		if (PC_BlockUtils.hasFlag(is, "NO_BUILD")) {
			return false;
		}

		if (id == Block.sand.blockID || id == Block.gravel.blockID) {
			return false;
		}

		if (pass == 0) {
			return id == 2 || id == 3 || id == 4;
		}

		if (pass == 1) {
			return id == 5 || id == 1 || id == 24 || id == 87;
		}

		if (id == 15 || id == 14 || id == 56) {
			return false;
		}


		return id != mod_PCcore.powerCrystal.blockID && Block.blocksList[is.itemID] != null && Block.blocksList[is.itemID].blockMaterial.isSolid();
	}

	/**
	 * Compress blocks in inventory to storage blocks.<br>
	 * <b>Time expensive, do only if needed!</b>
	 */
	private void compressInv() {
		if (level < 5) {
			return;
		}
		if (!compressBlocks) {
			return;
		}

		int sand = 0;
		int diamond = 0;
		int lapis = 0;
		int glowstone = 0;
		int snowball = 0;

		for (int pass = 0; pass < 3; pass++) {
			for (int i = 0; i < getSizeInventory(); i++) {
				if (getStackInSlot(i) != null) {
					if (getStackInSlot(i).itemID == Block.sand.blockID) {
						sand += getStackInSlot(i).stackSize;
						setInventorySlotContents(i, null);
						continue;
					}

					if (getStackInSlot(i).itemID == Item.diamond.shiftedIndex) {
						diamond += getStackInSlot(i).stackSize;
						setInventorySlotContents(i, null);
						continue;
					}

					if (getStackInSlot(i).itemID == Item.snowball.shiftedIndex) {
						snowball += getStackInSlot(i).stackSize;
						setInventorySlotContents(i, null);
						continue;
					}

					if (getStackInSlot(i).itemID == Item.lightStoneDust.shiftedIndex) {
						glowstone += getStackInSlot(i).stackSize;
						setInventorySlotContents(i, null);
						continue;
					}

					if (getStackInSlot(i).itemID == Item.dyePowder.shiftedIndex && getStackInSlot(i).getItemDamage() == 4) {
						lapis += getStackInSlot(i).stackSize;
						setInventorySlotContents(i, null);
						continue;
					}
				}
			}
		}

		if (sand > 0) {
			while (sand >= 4) {
				if (PC_InvUtils.addItemStackToInventory(this, new ItemStack(Block.sandStone.blockID, 1, 0))) {
					sand -= 4;
				} else {
					break;
				}
			}

			if (sand > 0) {
				ItemStack remaining = new ItemStack(Block.sand.blockID, sand, 0);
				if (!PC_InvUtils.addItemStackToInventory(this, remaining)) {
					entityDropItem(remaining, 1);
				}
			}
		}

		if (snowball > 0) {
			while (snowball >= 4) {
				if (PC_InvUtils.addItemStackToInventory(this, (new ItemStack(Block.blockSnow.blockID, 1, 0)))) {
					snowball -= 4;
				} else {
					break;
				}
			}

			if (snowball > 0) {
				ItemStack remaining = new ItemStack(Item.snowball.shiftedIndex, snowball, 0);
				if (!PC_InvUtils.addItemStackToInventory(this, remaining)) {
					entityDropItem(remaining, 1);
				}
			}
		}

		if (glowstone > 0) {
			while (glowstone >= 4) {
				if (PC_InvUtils.addItemStackToInventory(this, new ItemStack(Block.glowStone.blockID, 1, 0))) {
					glowstone -= 4;
				} else {
					break;
				}
			}

			if (glowstone > 0) {
				ItemStack remaining = new ItemStack(Item.lightStoneDust.shiftedIndex, glowstone, 0);
				if (!PC_InvUtils.addItemStackToInventory(this, remaining)) {
					entityDropItem(remaining, 1);
				}
			}
		}

		if (diamond > 0) {
			while (diamond >= 9) {
				if (PC_InvUtils.addItemStackToInventory(this, new ItemStack(Block.blockDiamond.blockID, 1, 0))) {
					diamond -= 9;
				} else {
					break;
				}
			}

			if (diamond > 0) {
				ItemStack remaining = new ItemStack(Item.diamond.shiftedIndex, diamond, 0);
				if (!PC_InvUtils.addItemStackToInventory(this, remaining)) {
					entityDropItem(remaining, 1);
				}
			}
		}

		if (lapis > 0) {
			while (lapis >= 9) {
				if (PC_InvUtils.addItemStackToInventory(this, new ItemStack(Block.blockLapis.blockID, 1, 0))) {
					lapis -= 9;
				} else {
					break;
				}
			}

			if (lapis > 0) {
				ItemStack remaining = new ItemStack(Item.dyePowder.shiftedIndex, lapis, 4);
				if (!PC_InvUtils.addItemStackToInventory(this, remaining)) {
					entityDropItem(remaining, 1);
				}
			}
		}

		return;
	}

	/**
	 * Fill nearby lava with stones from inventory, get lava into bucket.
	 */
	private void fillLavaNearby() {
		if (level < 4 || !lavaFillingEnabled) {
			return;
		}

		int y1 = (int) Math.floor(posY + 0.0001F);
		int x1 = (int) Math.round(posX);
		int z1 = (int) Math.round(posZ);

		boolean replace = true;
		for (int x = x1 - 2; x <= x1 + 1; x++) {
			for (int y = y1 - 1; y <= y1 + 2; y++) {
				for (int z = z1 - 2; z <= z1 + 1; z++) {
					replace = !((y == y1 || y == y1 + 1) && (x == x1 || x == x1 - 1) && (z == z1 || z == z1 - 1));

					if (x == x1 - 2 && y == y1 - 1) {
						continue;
					}
					if (x == x1 - 2 && y == y1 + 2) {
						continue;
					}
					if (x == x1 + 1 && y == y1 - 1) {
						continue;
					}
					if (x == x1 + 1 && y == y1 + 2) {
						continue;
					}

					if (z == z1 - 2 && y == y1 - 1) {
						continue;
					}
					if (z == z1 - 2 && y == y1 + 2) {
						continue;
					}
					if (z == z1 + 1 && y == y1 - 1) {
						continue;
					}
					if (z == z1 + 1 && y == y1 + 2) {
						continue;
					}

					if (x == x1 - 2 && z == z1 - 2) {
						continue;
					}
					if (x == x1 - 2 && z == z1 + 1) {
						continue;
					}
					if (x == x1 + 1 && z == z1 - 2) {
						continue;
					}
					if (x == x1 + 1 && z == z1 + 1) {
						continue;
					}

					switch ((int) Math.floor(rotationYaw)) {
						case 180:
							if (x == x1 - 2) {
								replace = false;
							}
							break;
						case 270:
							if (z == z1 - 2) {
								replace = false;
							}
							break;
						case 0:
							if (x == x1 + 1) {
								replace = false;
							}
							break;
						case 90:
							if (z == z1 + 1) {
								replace = false;
							}
							break;
					}

					int id = worldObj.getBlockId(x, y, z);
					if (id == 10 || id == 11) {
						lavaFillBucket();
						int fillId = 0;
						int fillMeta = 0;
						if (replace) {
							ItemStack fill = getBlockForBuilding();
							if (fill != null) {
								fillId = fill.itemID;
								fillMeta = fill.getItemDamage();
							}
						}
						worldObj.setBlockAndMetadataWithNotify(x, y, z, fillId, fillMeta);
						if (Block.blocksList[fillId] != null) {
							if (shouldMakeEffects()) {
								worldObj.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.blocksList[fillId].stepSound.getStepSound(), (Block.blocksList[fillId].stepSound.getVolume() + 1.0F) / 2.0F,
										Block.blocksList[fillId].stepSound.getPitch() * 0.8F);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Fill nearby water with stones from inventory.
	 */
	private void fillWaterNearby() {
		if (level < 6 || !waterFillingEnabled) {
			return;
		}

		int y1 = (int) Math.floor(posY + 0.000F);
		int x1 = (int) Math.round(posX);
		int z1 = (int) Math.round(posZ);

		boolean replace = true;
		for (int x = x1 - 2; x <= x1 + 1; x++) {
			for (int y = y1 - 1; y <= y1 + 2; y++) {
				for (int z = z1 - 2; z <= z1 + 1; z++) {
					replace = !((y == y1 || y == y1 + 1) && (x == x1 || x == x1 - 1) && (z == z1 || z == z1 - 1));

					if (x == x1 - 2 && y == y1 - 1) {
						continue;
					}
					if (x == x1 - 2 && y == y1 + 2) {
						continue;
					}
					if (x == x1 + 1 && y == y1 - 1) {
						continue;
					}
					if (x == x1 + 1 && y == y1 + 2) {
						continue;
					}

					if (z == z1 - 2 && y == y1 - 1) {
						continue;
					}
					if (z == z1 - 2 && y == y1 + 2) {
						continue;
					}
					if (z == z1 + 1 && y == y1 - 1) {
						continue;
					}
					if (z == z1 + 1 && y == y1 + 2) {
						continue;
					}

					if (x == x1 - 2 && z == z1 - 2) {
						continue;
					}
					if (x == x1 - 2 && z == z1 + 1) {
						continue;
					}
					if (x == x1 + 1 && z == z1 - 2) {
						continue;
					}
					if (x == x1 + 1 && z == z1 + 1) {
						continue;
					}

					switch ((int) Math.floor(rotationYaw)) {
						case 180:
							if (x == x1 - 2) {
								replace = false;
							}
							break;
						case 270:
							if (z == z1 - 2) {
								replace = false;
							}
							break;
						case 0:
							if (x == x1 + 1) {
								replace = false;
							}
							break;
						case 90:
							if (z == z1 + 1) {
								replace = false;
							}
							break;
					}

					int id = worldObj.getBlockId(x, y, z);
					if (id == 8 || id == 9) {
						int fillId = 0;
						int fillMeta = 0;
						if (replace) {
							ItemStack fill = getBlockForBuilding();
							if (fill != null) {
								fillId = fill.itemID;
								fillMeta = fill.getItemDamage();
							}
						}
						worldObj.setBlockAndMetadataWithNotify(x, y, z, fillId, fillMeta);
						if (Block.blocksList[fillId] != null) {
							if (shouldMakeEffects()) {
								worldObj.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.blocksList[fillId].stepSound.getStepSound(), (Block.blocksList[fillId].stepSound.getVolume() + 1.0F) / 2.0F,
										Block.blocksList[fillId].stepSound.getPitch() * 0.8F);
							}
						}
					}
				}
			}
		}
	}


	/**
	 * Try to unburry itself (if cobble was spawned at miner's position, sand
	 * fell on it or whatever.
	 * 
	 * @param targetPos do this for target position; current pos otherwise
	 */
	private void burriedFix(boolean targetPos) {

		int y1 = (int) Math.floor(posY + 0.0001F);

		if (isOnHalfStep()) {
			y1++;
		}

		int x1 = targetPos ? target.x : (int) Math.round(posX);
		int z1 = targetPos ? target.z : (int) Math.round(posZ);

		for (int x = x1 - 1; x <= x1; x++) {
			for (int y = y1; y <= y1 + 1; y++) {
				for (int z = z1 - 1; z <= z1; z++) {
					int id = worldObj.getBlockId(x, y, z);

					// get entry for new blocks.
					if (id != 0 && (Block.blocksList[id] instanceof BlockSand || id == Block.cobblestone.blockID || id == Block.dirt.blockID)) {
						harvestBlock_do(new PC_CoordI(x, y, z));
					}
				}
			}
		}

	}

	/**
	 * Deposit depositable blocks to nearby chest.
	 */
	private void depositToNearbyChest() {

		int y1 = (int) Math.floor(posY + 0.0001F);
		int x1 = (int) Math.round(posX);
		int z1 = (int) Math.round(posZ);

		for (int x = x1 - 2; x <= x1 + 1; x++) {
			for (int y = y1; y <= y1 + 1; y++) {
				for (int z = z1 - 2; z <= z1 + 1; z++) {
					IInventory chest = PC_InvUtils.getCompositeInventoryAt(worldObj, new PC_CoordI(x, y, z));
					if (chest != null) {
						// cycle through and deposit.
						for (int i = 0; i < getSizeInventory(); i++) {
							boolean stored = false;
							boolean crystal = false;
							ItemStack stack = getStackInSlot(i);
							if (stack != null && stack.itemID != mod_PCcore.powerDust.shiftedIndex && stack.itemID != Block.torchWood.blockID && stack.itemID != Item.bucketEmpty.shiftedIndex && stack.itemID != Item.bucketLava.shiftedIndex
									&& (!keepAllFuel || PC_InvUtils.getFuelValue(stack, FUEL_STRENGTH) == 0)) {

								if (stack.itemID == mod_PCcore.powerCrystal.blockID) {
									if (stack.stackSize <= 1) {
										continue;
									}
									crystal = true;
									stack = getStackInSlot(i).copy();
									stack.stackSize--;
									decrStackSize(i, stack.stackSize);
								}

								stored = PC_InvUtils.addWholeItemStackToInventory(chest, stack);

							}

							if (stored && !crystal) {
								setInventorySlotContents(i, null);
							}
						}

						if (shouldMakeEffects()) {
							worldObj.playSoundAtEntity(this, "random.pop", 0.2F, 0.5F + rand.nextFloat() * 0.3F);
						}

						return;
					}
				}
			}
		}
	}

	/**
	 * Place torches on ground and on walls, if enabled.
	 */
	private void performTorchPlacing() {
		if (level < 3) {
			return;
		}

		int y = (int) Math.floor(posY + 0.0001F);
		int x = (int) Math.round(posX);
		int z = (int) Math.round(posZ);

		if (getBrightness(1.0F) > 0.2F) {
			return;
		}
		if (handleWaterMovement()) {
			return;
		}

		if (!hasTorch()) {
			return;
		}

		int leftX = x, leftZ = z, rightX = x, rightZ = z;

		if (rotationYaw == 0) {
			rightZ = z - 1;
			leftZ = z;
		}
		if (rotationYaw == 90) {
			rightX = x;
			leftX = x - 1; /* rightZ=leftZ=z-1; */
		}
		if (rotationYaw == 180) {
			leftZ = z - 1;
			rightZ = z;
			rightX = leftX = x - 1;
		}
		if (rotationYaw == 270) {
			rightX = x - 1;
			leftX = x;
			leftZ = rightZ = z - 1;
		}

		Block torch = Block.torchWood;

		if (!torchesOnlyOnFloor) {
			if (worldObj.getBlockId(rightX, y + 1, rightZ) == 0 && torch.canPlaceBlockAt(worldObj, rightX, y + 1, rightZ)) {
				worldObj.setBlockWithNotify(rightX, y + 1, rightZ, torch.blockID);
				getTorch();
				return;
			}
			if (worldObj.getBlockId(leftX, y + 1, leftZ) == 0 && torch.canPlaceBlockAt(worldObj, leftX, y + 1, leftZ)) {
				worldObj.setBlockWithNotify(leftX, y + 1, leftZ, torch.blockID);
				getTorch();
				return;
			}
		}

		if (worldObj.getBlockId(rightX, y, rightZ) == 0 && torch.canPlaceBlockAt(worldObj, rightX, y, rightZ)) {
			worldObj.setBlockWithNotify(rightX, y, rightZ, torch.blockID);

			// set on floor if not building stairs.
			if (realCommand != PCmo_Command.UP) {
				Block.torchWood.onBlockPlaced(worldObj, rightX, y, rightZ, 1);
			}
			getTorch();
			return;
		}

		if (worldObj.getBlockId(leftX, y, leftZ) == 0 && torch.canPlaceBlockAt(worldObj, leftX, y, leftZ)) {
			worldObj.setBlockWithNotify(leftX, y, leftZ, torch.blockID);

			// set on floor if not building stairs.
			if (realCommand != PCmo_Command.UP) {
				Block.torchWood.onBlockPlaced(worldObj, leftX, y, leftZ, 1);
			}
			getTorch();
			return;
		}

		return;
	}

	/**
	 * @return torch was consumed from inventory
	 */
	private boolean getTorch() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null && getStackInSlot(i).itemID == Block.torchWood.blockID) {
				decrStackSize(i, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return inventory has some torches
	 */
	private boolean hasTorch() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null && getStackInSlot(i).itemID == Block.torchWood.blockID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * fill bucket with lava
	 * 
	 * @return lava was removed (to bucket)
	 */
	private boolean lavaFillBucket() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				int id = getStackInSlot(i).itemID;
				if (id == Item.bucketEmpty.shiftedIndex) {
					setInventorySlotContents(i, new ItemStack(Item.bucketLava, 1, 0));
					return true;
				}
			}
		}
		return false;
	}

	// TODO JavaDoc!

	// === UPDATE TICK ===

	@SuppressWarnings("unchecked")
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

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		// EXECUTE CURRENT COMMAND

		boolean stop = programmingGuiOpen;

		if (!stop) {
			if (waitingForFuel > 0) {
				if (addFuelForCost(waitingForFuel)) {
					waitingForFuel = 0;
					prepareForCommandExecution();
				}
			}

			releaseAllocatedFuelIfNoLongerNeeded();
			if (waitingForFuel < 0) {
				waitingForFuel = 0;
			}
			if (currentCommand == -1 && waitingForFuel != 0) {
				waitingForFuel = 0;
			}

			if (waitingForFuel == 0) {

				// execute rotation and check if target angle is reached.
				if (PCmo_Command.isCommandTurn(currentCommand)) {
					motionX = motionZ = 0;
					posX = target.x;
					posZ = target.z;

					if (Math.abs(rotationRemaining) < 3) {
						currentCommand = -1;
						posX = target.x;
						posZ = target.z;
						rotationRemaining = 0;
						roundRotation();
					} else {
						playMotionEffect();
						int step = MathHelper.clamp_int(level, 3, 7);
						step = MathHelper.clamp_int(step, 0, Math.abs(rotationRemaining));

						int incr = rotationRemaining > 0 ? step : -step;
						rotationYaw = rotationYaw + incr;
						if (rotationYaw < 0) {
							rotationYaw = prevRotationYaw = 360F + rotationYaw;
						}
						if (rotationYaw > 360F) {
							rotationYaw = prevRotationYaw = rotationYaw - 360F;
						}

						rotationRemaining -= incr;
					}
				}

				if (currentCommand != -1) {
					burriedFix(false);
				}

				// check if movement destination is reached
				if (PCmo_Command.isCommandMove(currentCommand) || (currentCommand == PCmo_Command.UP && isMiningDone())) {

					roundRotation();
					performTorchPlacing();

					if (isMinerAtTargetPos()) {
						consumeAllocatedFuel(getStepCost());
						fillLavaNearby();
						fillWaterNearby();

						if (getTargetDistanceX() > 0.03125D) {
							posX = prevPosX = target.x;
						}

						if (getTargetDistanceZ() > 0.03125D) {
							posZ = prevPosZ = target.z;
						}

						stepCounter--;
						if (stepCounter <= 0) {
							currentCommand = -1;
							if (commandList.length() == 0) {
								motionX = 0;
								motionZ = 0;
							}
							stepCounter = 0;
						} else {
							// prepare next target position.
							prepareForCommandExecution();
						}
					}
				}

				// perform movement and optional mining forwards!
				// previous command may have set waitingForFuel to step cost.
				if (PCmo_Command.isCommandMove(currentCommand) || currentCommand == PCmo_Command.DOWN || currentCommand == PCmo_Command.UP) {

					roundRotation();

					boolean fw = (currentCommand == PCmo_Command.FORWARD);
					boolean dwn = (currentCommand == PCmo_Command.DOWN);
					boolean up = (currentCommand == PCmo_Command.UP);
					boolean back = (currentCommand == PCmo_Command.BACKWARD);

					// for checks
					int x = (int) Math.round(posX);
					int y = (int) Math.floor(posY + 0.0001F);
					if (isOnHalfStep()) {
						y += 1;
					}
					int z = (int) Math.round(posZ);

					boolean bridgeOk = true;
					if (!bridgeDone) {
						bridgeOk = performBridgeBuilding();
						if (!bridgeOk) {

						} else {
							bridgeDone = true;
						}
					}

					if (isMiningInProgress() || !bridgeOk) {
						motionX = motionZ = 0;
					}

					boolean miningDone = isMiningDone();

					boolean canMove = bridgeOk && !dwn && (!up || miningDone);

					if (up && !miningDone) {
						performMiningUpdate(new PC_CoordI(x, y + 2, z), 8);
						performMiningUpdate(new PC_CoordI(x - 1, y + 2, z), 9);
						performMiningUpdate(new PC_CoordI(x, y + 2, z - 1), 10);
						performMiningUpdate(new PC_CoordI(x - 1, y + 2, z - 1), 11);
					}

					double motionAdd = (MOTION_SPEED[level - 1] * ((fw || up) ? 1 : -1)) * 0.5D;

					if (rotationYaw == 180) {
						if (!miningDone && (!back) && miningEnabled) {
							performMiningUpdate(new PC_CoordI(x + 1, y, z - 1), 0);
							performMiningUpdate(new PC_CoordI(x + 1, y, z), 1);
							performMiningUpdate(new PC_CoordI(x + 1, y + 1, z), 2);
							performMiningUpdate(new PC_CoordI(x + 1, y + 1, z - 1), 3);

							if (dwn) {
								performMiningUpdate(new PC_CoordI(x + 1, y - 1, z - 1), 4);
								performMiningUpdate(new PC_CoordI(x + 1, y - 1, z), 5);
							}

							if (up) {
								performMiningUpdate(new PC_CoordI(x + 1, y + 2, z - 1), 6);
								performMiningUpdate(new PC_CoordI(x + 1, y + 2, z), 7);
							}
						}
						if (isLocationEmpty(target.setY(y)) && canMove) {
							motionX += motionAdd;
						}
						motionZ = 0;
					}

					if (rotationYaw == 270) {
						if (!miningDone && (!back) && miningEnabled) {

							performMiningUpdate(new PC_CoordI(x - 1, y, z + 1), 0);
							performMiningUpdate(new PC_CoordI(x, y, z + 1), 1);
							performMiningUpdate(new PC_CoordI(x - 1, y + 1, z + 1), 2);
							performMiningUpdate(new PC_CoordI(x, y + 1, z + 1), 3);

							if (dwn) {
								performMiningUpdate(new PC_CoordI(x - 1, y - 1, z + 1), 4);
								performMiningUpdate(new PC_CoordI(x, y - 1, z + 1), 5);
							}

							if (up) {
								performMiningUpdate(new PC_CoordI(x - 1, y + 2, z + 1), 6);
								performMiningUpdate(new PC_CoordI(x, y + 2, z + 1), 7);
							}

						}
						if (isLocationEmpty(target.setY(y)) && canMove) {
							motionZ += motionAdd;
						}
						motionX = 0;
					}

					if (rotationYaw == 0) {
						if (!miningDone && (!back) && miningEnabled) {

							performMiningUpdate(new PC_CoordI(x - 2, y, z - 1), 0);
							performMiningUpdate(new PC_CoordI(x - 2, y, z), 1);
							performMiningUpdate(new PC_CoordI(x - 2, y + 1, z), 2);
							performMiningUpdate(new PC_CoordI(x - 2, y + 1, z - 1), 3);

							if (dwn) {
								performMiningUpdate(new PC_CoordI(x - 2, y - 1, z - 1), 4);
								performMiningUpdate(new PC_CoordI(x - 2, y - 1, z), 5);
							}

							if (up) {
								performMiningUpdate(new PC_CoordI(x - 2, y + 2, z - 1), 6);
								performMiningUpdate(new PC_CoordI(x - 2, y + 2, z), 7);
							}
						}
						if (isLocationEmpty(target.setY(y)) && canMove) {
							motionX -= motionAdd;
						}
						motionZ = 0;
					}

					if (rotationYaw == 90) {
						if (!miningDone && (!back) && miningEnabled) {

							performMiningUpdate(new PC_CoordI(x - 1, y, z - 2), 0);
							performMiningUpdate(new PC_CoordI(x, y, z - 2), 1);
							performMiningUpdate(new PC_CoordI(x - 1, y + 1, z - 2), 2);
							performMiningUpdate(new PC_CoordI(x, y + 1, z - 2), 3);
							if (dwn) {
								performMiningUpdate(new PC_CoordI(x - 1, y - 1, z - 2), 4);
								performMiningUpdate(new PC_CoordI(x, y - 1, z - 2), 5);
							}

							if (up) {
								performMiningUpdate(new PC_CoordI(x - 1, y + 2, z - 2), 6);
								performMiningUpdate(new PC_CoordI(x, y + 2, z - 2), 7);
							}
						}
						if (isLocationEmpty(target.setY(y)) && canMove) {
							motionZ -= motionAdd;
						}
						motionX = 0;
					}

					if (dwn && !isMiningInProgress()) {
						currentCommand = -1;
					}

					if (up && isMiningDone() && !upStepLaid) {
						switch ((int) Math.floor(rotationYaw)) {
							case 0:
								layHalfStep(x - 2, y, z - 1, true);
								layHalfStep(x - 2, y, z, true);
								break;

							case 90:
								layHalfStep(x - 1, y, z - 2, true);
								layHalfStep(x, y, z - 2, true);
								break;

							case 180:
								layHalfStep(x + 1, y, z - 1, true);
								layHalfStep(x + 1, y, z, true);
								break;

							case 270:
								layHalfStep(x - 1, y, z + 1, true);
								layHalfStep(x, y, z + 1, true);
								break;
						}
						upStepLaid = true;
					}

					// stop if bumped into wall
					if ((!miningEnabled || !isMiningInProgress() || currentCommand == PCmo_Command.BACKWARD) && !isLocationEmpty(target.setY(y))) {

						burriedFix(fw && miningEnabled);

						if (!isLocationEmpty(target.setY(y))) {
							if (!miningEnabled || currentCommand == PCmo_Command.BACKWARD) {
								currentCommand = -1;
								resetMineCounter();
								consumeAllocatedFuel(getStepCost());
								target.x = (int) Math.round(posX);
								target.z = (int) Math.round(posZ);
								target.y = (int) Math.round(posY + 0.001F);

								stepCounter = 0;
							}
							motionX = motionZ = 0;
						}
					}

				}

			}

		}

		// FALL
		if (!onGround) {
			motionY -= 0.04D;
		}

		// speed limit.
		double d7 = MOTION_SPEED[level - 1];
		if (motionX < -d7) {
			motionX = -d7;
		}
		if (motionX > d7) {
			motionX = d7;
		}
		if (motionZ < -d7) {
			motionZ = -d7;
		}
		if (motionZ > d7) {
			motionZ = d7;
		}

		// GET NEW COMMAND FROM QUEUE
		if (!stop && onGround && currentCommand == -1 && commandList.length() > 0) {
			int oldCmd = currentCommand;
			currentCommand = getNextCommand(); // gets command and removes it
												// from queue
			if (currentCommand != -1 && currentCommand != oldCmd) {
				alignToBlocks();
			}
			if (currentCommand == -1 && !keyboardControlled) {
				alignToBlocks();
			}

			prepareForCommandExecution();

			if (currentCommand != -1) {
				setSprinting(true);
			}
		}

		// slow down if no more commands are available (halt)
		if (currentCommand == -1 && commandList.length() == 0) {
			motionX = 0D;
			motionZ = 0D;
			setSprinting(false);
		}

		if (Math.abs(motionX) > 0.0001D || Math.abs(motionZ) > 0.0001D) {
			playMotionEffect();
		}

		// pick up items.
		List<EntityItem> list;

		list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, boundingBox.expand(1.5D, 0.5D, 1.5D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				EntityItem entity = list.get(j1);
				if (entity.delayBeforeCanPickup >= 7) {
					continue;
				}

				int id = entity.item.itemID;
				int dmg = entity.item.getItemDamage();

				boolean xtal = id == mod_PCcore.powerCrystal.blockID;
				boolean compress = (id == Block.sand.blockID || id == Item.snowball.shiftedIndex || id == Item.diamond.shiftedIndex || (id == Item.dyePowder.shiftedIndex && dmg == 4) || id == Item.lightStoneDust.shiftedIndex);

				if (shouldDestroyStack(entity.item)) {
					entity.setDead();
					continue;
				}

				if (PC_InvUtils.addItemStackToInventory(this, entity.item)) {
					entity.setDead();
				}
				if (xtal) {
					updateLevel();
				}
				if (compress && compressBlocks) {
					compressInv();
				}
			}
		}

		// push items
		list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.2D, 0.01D, 0.2D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				Entity entity = list.get(j1);
				if (entity instanceof EntityFX || entity instanceof EntityXPOrb) {
					continue;
				}
				if (entity.isDead) {
					continue;
				}

				if (entity instanceof EntityArrow) {
					PC_InvUtils.addItemStackToInventory(this, new ItemStack(Item.arrow, 1, 0));
					entity.setDead();
					return;
				}

				// keep the same old velocity
				double motionX_prev = motionX;
				double motionY_prev = motionY;
				double motionZ_prev = motionZ;

				entity.applyEntityCollision(this);

				motionX = motionX_prev;
				motionY = motionY_prev;
				motionZ = motionZ_prev;
			}
		}

		moveEntity(Math.min(motionX, getTargetDistanceX()), motionY, Math.min(motionZ, getTargetDistanceZ()));
		motionX *= 0.7D;
		motionZ *= 0.7D;

	}

	@Override
	public void applyEntityCollision(Entity entity) {
		if (entity.riddenByEntity == this || entity.ridingEntity == this) {
			return;
		}

		double d = entity.posX - posX;
		double d1 = entity.posZ - posZ;
		double d2 = MathHelper.abs_max(d, d1);
		if (d2 >= 0.0099999997764825821D) {
			d2 = MathHelper.sqrt_double(d2);
			d /= d2;
			d1 /= d2;
			double d3 = 1.0D / d2;
			if (d3 > 1.0D) {
				d3 = 1.0D;
			}
			d *= d3;
			d1 *= d3;
			d *= 0.05000000074505806D;
			d1 *= 0.05000000074505806D;
			d *= 1.0F - entityCollisionReduction;
			d1 *= 1.0F - entityCollisionReduction;
			isAirBorne = true;

			// this entity won't be moved!

			entity.addVelocity(d, 0.0D, d1);
		}
	}

	// NBT TAGs

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {

		NBTTagCompound code = new NBTTagCompound();

		code.setString("CommandList", commandList);
		code.setShort("CurrentCommand", (short) currentCommand);
		code.setShort("RealCommand", (short) realCommand);
		code.setShort("StepCounter", (short) stepCounter);

		code.setString("CommandListSaved", commandListSaved);
		code.setString("Program", program);
		code.setBoolean("Paused", paused);
		code.setBoolean("Keyboard", keyboardControlled);

		tag.setTag("MinerCode", code);


		// status compound tag
		NBTTagCompound status = new NBTTagCompound();

		status.setByte("Level", (byte) level);
		status.setShort("RotationRemaining", (short) rotationRemaining);

		PC_Utils.writeWrappedToNBT(status, "target", target);

		status.setInteger("fuelBuffer", fuelBuffer);
		status.setInteger("fuelAllocated", fuelAllocated);
		status.setInteger("waitingForFuel", waitingForFuel);
		status.setBoolean("miningEnabled", miningEnabled);
		status.setBoolean("bridgeEnabled", bridgeEnabled);
		status.setBoolean("lavaFillingEnabled", lavaFillingEnabled);
		status.setBoolean("waterEnabled", waterFillingEnabled);
		status.setBoolean("keepAllFuel", keepAllFuel);
		status.setBoolean("torchesOnlyOnFloor", torchesOnlyOnFloor);
		status.setBoolean("craftCompress", compressBlocks);
		status.setByte("Destroy", DESTROY);

		status.setInteger("waitingForFuel", waitingForFuel);

		status.setInteger("mineTime0", mineCounter[0]);
		status.setInteger("mineTime1", mineCounter[1]);
		status.setInteger("mineTime2", mineCounter[2]);
		status.setInteger("mineTime3", mineCounter[3]);
		status.setInteger("mineTime4", mineCounter[4]);
		status.setInteger("mineTime5", mineCounter[5]);

		tag.setTag("MinerStatus", status);



		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		tag.setTag("Items", nbttaglist);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {

		NBTTagCompound code = tag.getCompoundTag("MinerCode");

		commandList = code.getString("CommandList");
		currentCommand = code.getShort("CurrentCommand");
		realCommand = code.getShort("RealCommand");
		stepCounter = code.getShort("StepCounter");

		commandListSaved = code.getString("CommandListSaved");
		program = code.getString("Program");
		paused = code.getBoolean("Paused");
		keyboardControlled = code.getBoolean("Keyboard");

		NBTTagCompound status = tag.getCompoundTag("MinerStatus");

		level = MathHelper.clamp_int(status.getByte("Level"), 1, 8);
		rotationRemaining = status.getShort("RotationRemaining");

		PC_Utils.readWrappedFromNBT(status, "target", target);

		fuelBuffer = status.getInteger("fuelBuffer");
		fuelAllocated = status.getInteger("fuelAllocated");
		waitingForFuel = status.getInteger("waitingForFuel");
		miningEnabled = status.getBoolean("miningEnabled");
		bridgeEnabled = status.getBoolean("bridgeEnabled");
		lavaFillingEnabled = status.getBoolean("lavaFillingEnabled");
		waterFillingEnabled = status.getBoolean("waterEnabled");
		keepAllFuel = status.getBoolean("keepAllFuel");
		torchesOnlyOnFloor = status.getBoolean("torchesOnlyOnFloor");
		compressBlocks = status.getBoolean("craftCompress");

		DESTROY = status.getByte("Destroy");

		waitingForFuel = status.getInteger("waitingForFuel");

		mineCounter[0] = status.getInteger("mineTime0");
		mineCounter[1] = status.getInteger("mineTime1");
		mineCounter[2] = status.getInteger("mineTime2");
		mineCounter[3] = status.getInteger("mineTime3");
		mineCounter[4] = status.getInteger("mineTime4");
		mineCounter[5] = status.getInteger("mineTime5");

		NBTTagList nbttaglist = tag.getTagList("Items");

		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < inventory.length) {
				inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		if (keyboardControlled) {
			PCmo_MinerControlHandler.setMinerForKeyboardControl(this, true);
		}
	}

	// === PLAYER INTERACTION ===

	/** The Console GUI opened for this miner. */
	boolean openedGui = false;

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer) {
			return true;
		}

		if (!worldObj.isRemote) {
			if (entityplayer.isSneaking()) {
				programmingGuiOpen = true;
				openedGui = true;
				PC_Utils.openGres(entityplayer, new PCmo_GuiMinerConsole(this));
				return true;
			}

			// set for keyboard control or open gui.
			if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == mod_PCcore.activator.shiftedIndex) {
				setKeyboardControl(!keyboardControlled);

			} else if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.stick.shiftedIndex) {
				PC_Utils.chatMsg(rotationYaw + "°", true);
				PC_Utils.chatMsg("PROGRAM: " + program, false);
				PC_Utils.chatMsg("Buffer: " + commandList.length() + " commands.", false);
				PC_Utils.chatMsg("Inctruction = " + Character.toString(PCmo_Command.getCharFromInt(currentCommand)) + ", Step counter = " + stepCounter, false);
				PC_Utils.chatMsg("FUEL: burning = " + fuelBuffer + ", allocated = " + fuelAllocated + ", deficit = " + waitingForFuel, false);
				PC_Utils.chatMsg("bridge = " + bridgeEnabled + ", mining = " + miningEnabled + ", destroy_flags = " + DESTROY, false);

			} else {
				openedGui = true;
				ModLoader.openGUI(entityplayer, new GuiChest(entityplayer.inventory, this));
			}
		}
		return true;
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

	// === IINVENTORY ===

	private ItemStack[] inventory = new ItemStack[54];

	@Override
	public int getSizeInventory() {
		return 54;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return PC_Lang.tr("pc.miner.chestName");
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return entityplayer.getDistanceSq(posX, posY, posZ) <= 64D;
	}

	@Override
	public void openChest() {};

	@Override
	public void closeChest() {
		openedGui = false;

		updateLevel();
	}

	@Override
	public void onInventoryChanged() {}

	// === FUEL STUFF ===
	private void consumeAllocatedFuel(int count) {
		fuelAllocated -= count;
		fuelBuffer -= count;
		if (fuelBuffer < 0) {
			fuelBuffer = 0;
		}
		if (fuelAllocated < 0) {
			fuelAllocated = 0;
		}
	}

	private void releaseAllocatedFuelIfNoLongerNeeded() {
		if (!isMiningInProgress() && currentCommand == -1) {
			fuelAllocated = 0;
		}
	}

	private boolean addFuelForCost(int cost) {

		if (fuelBuffer - fuelAllocated >= cost) {
			fuelAllocated += cost;
			return true;
		} else {
			for (int s = 0; s < getSizeInventory(); s++) {
				ItemStack stack = getStackInSlot(s);
				int bt = PC_InvUtils.getFuelValue(stack, FUEL_STRENGTH);
				if (bt > 0) {
					fuelBuffer += bt;
					if (stack.getItem().hasContainerItem()) {
						setInventorySlotContents(s, new ItemStack(stack.getItem().getContainerItem(), 1, 0));
					} else {
						decrStackSize(s, 1);
					}

					if ((fuelBuffer - fuelAllocated) >= cost) {
						fuelAllocated += cost;
						return true;
					}
				}
			}

			if ((fuelBuffer - fuelAllocated) >= cost) {
				fuelAllocated += cost;
				return true;
			}
		}

		if (waitingForFuel <= 0) waitingForFuel += cost - (fuelBuffer + fuelAllocated);
		return false;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (inventory[par1] != null) {
			ItemStack itemstack = inventory[par1];
			inventory[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

}
