package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;


/**
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCmo_EntityMiner extends Entity {

	/** Fuel strength multiplier. It's also affected by level. */
	private static final double FUEL_STRENGTH = 0.9D;

	private static final Random rand = new Random();

	/** Fake player instance used for block mining */
	private EntityPlayer fakePlayer;


	/** Miner configuration */
	protected MinerConfig cfg = new MinerConfig();
	/** Miner controller */
	protected MinerBrain brain = new MinerBrain();
	/** Miner status */
	protected MinerStatus st = new MinerStatus();
	/** Cargo inventory with all items */
	protected MinerCargoInventory cargo = new MinerCargoInventory();

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

		st.target.x = (int) dx;
		st.target.y = (int) dy;
		st.target.z = (int) dz;
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
		if (damagesource != DamageSource.outOfWorld
				&& (worldObj.isRemote || isDead || (damagesource.getSourceOfDamage() == null && damagesource != DamageSource.explosion))) {
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
//		motionX = velocityX;
//		motionY = velocityY;
//		motionZ = velocityZ;
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


	/**
	 * Class containing miner settings, which can be manupulated from gui or
	 * from the program.
	 * 
	 * @author MightyPork
	 */
	public class MinerConfig implements PC_INBT {
		/** Should keep all fuel when depositing? */
		public boolean keepAllFuel = false;

		/** Place torches only on floor - good for large rooms. */
		public boolean torchesOnlyOnFloor = false;

		/** Compress collected blocks to storage blocks if possible */
		public boolean compressBlocks = false;

		/** Mining enabled */
		public boolean miningEnabled = true;

		/** Bridge enabled. */
		public boolean bridgeEnabled = false;

		/** Lava filling enabled */
		public boolean lavaFillingEnabled = true;

		/** Water filling enabled */
		public boolean waterFillingEnabled = false;

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			tag.setBoolean("KeepFuel", keepAllFuel);
			tag.setBoolean("TorchesOnFloor", torchesOnlyOnFloor);
			tag.setBoolean("AutoCompress", compressBlocks);
			tag.setBoolean("Mining", miningEnabled);
			tag.setBoolean("Bridge", bridgeEnabled);
			tag.setBoolean("Lava", lavaFillingEnabled);
			tag.setBoolean("Water", waterFillingEnabled);
			return tag;
		}

		@Override
		public PC_INBT readFromNBT(NBTTagCompound tag) {
			keepAllFuel = tag.getBoolean("KeepFuel");
			torchesOnlyOnFloor = tag.getBoolean("TorchesOnFloor");
			compressBlocks = tag.getBoolean("AutoCompress");
			miningEnabled = tag.getBoolean("Mining");
			bridgeEnabled = tag.getBoolean("Bridge");
			lavaFillingEnabled = tag.getBoolean("Lava");
			waterFillingEnabled = tag.getBoolean("Water");
			return this;
		}
	}

	/**
	 * Weasel controller, class monitoring and controlling miner based on weasel
	 * program.
	 * 
	 * @author MightyPork
	 */
	public class MinerBrain implements PC_INBT, IWeaselHardware {

		/** error message. */
		public String error = "";
		/** source code */
		public String program = "";

		private WeaselEngine engine = new WeaselEngine(this);
		
		/**
		 * Disconnect from keyboard, reset status and start program execution.
		 * 
		 * @throws PCmo_CommandException when the program contains errors.
		 */
		public void launchProgram() throws PCmo_CommandException {
			st.keyboardControlled = false;
			st.paused = false;
			st.halted = false;
			error = "";
			PCmo_MinerControlHandler.disconnectMinerFromKeyboardControl(PCmo_EntityMiner.this, true);

			alignToBlocks();
			resetStatus();

			st.commandList = "";
			st.commandListSaved = "";
			st.currentCommand = -1;
			
			try {
				engine.insertNewProgram(WeaselEngine.compileProgram(program));
			} catch (Exception e) {
				e.printStackTrace();
				throw new PCmo_CommandException(e.getMessage());
			}
		}

		/**
		 * @return true if miner crashed
		 */
		public boolean hasError() {
			return error.length() > 0;
		}

		private void setError(String message) {
			error = message;
		}

		/**
		 * Run weasel and try to get next instruction.
		 */
		public void run() {
			if (!st.paused && !st.halted && !hasError()) {
				try {
					engine.run(400);
				} catch (WeaselRuntimeException wre) {
					setError(wre.getMessage());
					System.out.println(error);
				}
			}
		}


		// NBT saving


		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			PC_Utils.saveToNBT(tag, "Engine", engine);
			tag.setString("Error", error);
			tag.setString("Program", program);
			return tag;
		}

		@Override
		public PC_INBT readFromNBT(NBTTagCompound tag) {
			PC_Utils.loadFromNBT(tag, "Engine", engine);
			error = tag.getString("Error");
			program = tag.getString("Program");
			return this;
		}


		// Weasel interfacing

		@Override
		public boolean doesProvideFunction(String functionName) {
			return getProvidedFunctionNames().contains(functionName);
		}

		@Override
		public WeaselObject callProvidedFunction(WeaselEngine engine, String name, WeaselObject[] args) {

			try {

				if (name.equals("run")) name = "do";

				if (name.equals("do")) {
					int num = 1;
					if (args.length == 2) {
						num = (Integer) args[1].get();
					}
					for (int i = 0; i < num; i++) {
						appendCode((String) args[0].get());
					}
					engine.requestPause();
					return null;
				}


				if (name.equals("forward")) name = "fw";
				if (name.equals("go")) name = "fw";

				if (name.equals("fw")) {
					int num = 1;
					if (args.length == 1) {
						num = (Integer) args[0].get();
					}

					// spaces are for safety - when there are two numbers next to each other.
					appendCode(" " + num + " ");
					engine.requestPause();
					return null;
				}

				if (name.equals("backward")) name = "bw";
				if (name.equals("back")) name = "bw";

				if (name.equals("bw")) {
					int num = 1;
					if (args.length == 1) {
						num = (Integer) args[0].get();
					}
					num = -num;
					// spaces are for safety - when there are two numbers next to each other.
					appendCode(" " + num + " ");
					engine.requestPause();
					return null;
				}


				if (name.equals("left")) {
					int num = 1;
					if (args.length == 1) {
						num = (Integer) args[0].get();
					}
					boolean R = num < 0;
					if (R) num = -num;
					for (int i = 0; i < num; i++) {
						appendCode(R ? "R" : "L");
					}
					engine.requestPause();
					return null;
				}

				if (name.equals("right")) {
					int num = 1;
					if (args.length == 1) {
						num = (Integer) args[0].get();
					}
					boolean L = num < 0;
					if (L) num = -num;
					for (int i = 0; i < num; i++) {
						appendCode(L ? "L" : "R");
					}
					engine.requestPause();
					return null;
				}

				if (name.equals("turn")) {
					int num = 2;
					if (args.length == 1) {
						num = (Integer) args[0].get();
					}
					boolean L = num < 0;
					if (L) num = -num;
					for (int i = 0; i < num; i++) {
						appendCode(L ? "L" : "R");
					}
					engine.requestPause();
					return null;
				}
				
				
				if (name.equals("xplus")) name = "east";
				if (name.equals("xminus")) name = "west";
				if (name.equals("zplus")) name = "south";
				if (name.equals("zminus")) name = "north";

				if (name.equals("north")) {
					appendCode("N");
					engine.requestPause();
					return null;
				}
				if (name.equals("south")) {
					appendCode("S");
					engine.requestPause();
					return null;
				}
				if (name.equals("east")) {
					appendCode("E");
					engine.requestPause();
					return null;
				}
				if (name.equals("west")) {
					appendCode("W");
					engine.requestPause();
					return null;
				}
				if (name.equals("deposit")) {
					appendCode("Q");
					engine.requestPause();
					return null;
				}
			} catch (PCmo_CommandException e) {
				e.printStackTrace();
				throw new WeaselRuntimeException(e.getMessage());
			}

			return null;
		}


		@Override
		public WeaselObject getVariable(String name) {
			if (name.equals("pos.x")) {
				return new WeaselInteger(Math.round(posX));
			}
			if (name.equals("pos.y")) {
				return new WeaselInteger(Math.round(posY) + (isOnHalfStep() ? 1 : 0));
			}
			if (name.equals("pos.z")) {
				return new WeaselInteger(Math.round(posZ));
			}
			return null;
		}

		@Override
		public void setVariable(String name, Object object) {}


		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = new ArrayList<String>(0);
			list.add("run");
			list.add("do");
			list.add("fw");
			list.add("forward");
			list.add("go");
			list.add("bw");
			list.add("back");
			list.add("backward");
			list.add("left");
			list.add("right");
			list.add("turn");
			list.add("north");
			list.add("south");
			list.add("east");
			list.add("west");
			list.add("xplus");
			list.add("xminus");
			list.add("zplus");
			list.add("zminus");
			list.add("deposit");
			return list;
		}

		@Override
		public List<String> getProvidedVariableNames() {
			List<String> list = new ArrayList<String>(0);
			list.add("pos.x");
			list.add("pos.y");
			list.add("pos.z");
			return list;
		}

	}


	/**
	 * Class containing internal miner data, often temporary and not important
	 * for the outer world.
	 * 
	 * @author MightyPork
	 */
	public class MinerStatus implements PC_INBT {

		/** Is program execution paused? */
		private boolean paused = false;

		/**
		 * Flag set if program is stopped and miner should not move until it
		 * receives keyboard command or gets new program.
		 */
		public boolean halted = false;

		/** List of miner commands waiting for execution */
		private String commandList = "";

		/** Command currently being processed */
		private int currentCommand = -1;

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
		private String commandListSaved = "";

		/** Flag: Is connected to keyboard? */
		public boolean keyboardControlled = false;

		/** Flag: Is the programming GUI open? -> Ignore keyboard control */
		// no nbt
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
		 * Flag that this entity is being created and inventory should not be
		 * checked for Power Crystals.
		 */
		// no nbt
		private boolean minerBeingCreated = false;

		/**
		 * The miner's level.<br>
		 * Calculated from count of PowerCrystals.
		 */
		public int level = 1;

		/** Fuel consumed from items and waiting for usage */
		private int fuelBuffer = 0;
		/**
		 * Fuel allocated for current operation.<br>
		 * This fuel is already in the buffer, but won't be consumed until the
		 * operation is really finished.<br>
		 * that prevents fuel consumption when miner hits something it can't
		 * mine.
		 */
		private int fuelAllocated = 0;
		/**
		 * Fuel needed to execute current command - wonẗ move until some fuel is
		 * added
		 */
		private int fuelDeficit = 0;

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

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			tag.setBoolean("Paused", paused);
			tag.setBoolean("Halted", halted);
			tag.setString("CommandList", commandList);
			tag.setString("CommandListSaved", commandListSaved);
			tag.setInteger("Cmd", currentCommand);
			tag.setInteger("CmdReal", realCommand);
			tag.setInteger("Steps", stepCounter);
			tag.setBoolean("Keyboard", keyboardControlled);
			tag.setBoolean("UpStepLaid", upStepLaid);
			tag.setBoolean("BridgeDone", bridgeDone);
			PC_Utils.saveToNBT(tag, "Target", target);
			tag.setInteger("RotationRemaining", rotationRemaining);
			tag.setInteger("Level", level);
			tag.setInteger("FuelBuffer", fuelBuffer);
			tag.setInteger("FuelAllocated", fuelAllocated);
			tag.setInteger("FuelDeficit", fuelDeficit);

			for (int i = 0; i < mineCounter.length; i++) {
				tag.setInteger("mt" + i, mineCounter[i]);
			}

			return tag;
		}

		@Override
		public PC_INBT readFromNBT(NBTTagCompound tag) {
			paused = tag.getBoolean("Paused");
			halted = tag.getBoolean("Halted");
			commandList = tag.getString("CommandList");
			commandListSaved = tag.getString("CommandListSaved");
			currentCommand = tag.getInteger("Cmd");
			realCommand = tag.getInteger("CmdReal");
			stepCounter = tag.getInteger("Steps");
			keyboardControlled = tag.getBoolean("Keyboard");
			upStepLaid = tag.getBoolean("UpStepLaid");
			bridgeDone = tag.getBoolean("BridgeDone");
			PC_Utils.loadFromNBT(tag, "Target", target);
			rotationRemaining = tag.getInteger("RotationRemaining");
			level = tag.getInteger("Level");
			fuelBuffer = tag.getInteger("FuelBuffer");
			fuelAllocated = tag.getInteger("FuelAllocated");
			fuelDeficit = tag.getInteger("FuelDeficit");

			for (int i = 0; i < mineCounter.length; i++) {
				mineCounter[i] = tag.getInteger("mt" + i);
			}
			return this;
		}


		/**
		 * Consume part of the allocated fuel
		 * 
		 * @param count fuel points to consume
		 */
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

		/**
		 * If there is no fuel-consuming process, release allocated fuel in fuel
		 * buffer for other uses.
		 */
		private void releaseAllocatedFuelIfNoLongerNeeded() {
			if (!isMiningInProgress() && currentCommand == -1) {
				fuelAllocated = 0;
			}
		}

		/**
		 * Add fuel to fuel buffer for given co If there isn't enough fuel in
		 * the cargo inventory, add it to deficit counter.
		 * 
		 * @param cost fuel points needed
		 * @return true on success
		 */
		private boolean addFuelForCost(int cost) {

			if (fuelBuffer - fuelAllocated >= cost) {
				fuelAllocated += cost;
				return true;
			} else {
				for (int s = 0; s < cargo.getSizeInventory(); s++) {
					ItemStack stack = cargo.getStackInSlot(s);
					int bt = PC_InvUtils.getFuelValue(stack, FUEL_STRENGTH);
					if (bt > 0) {
						fuelBuffer += bt;
						if (stack.getItem().hasContainerItem()) {
							cargo.setInventorySlotContents(s, new ItemStack(stack.getItem().getContainerItem(), 1, 0));
						} else {
							cargo.decrStackSize(s, 1);
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

			if (fuelDeficit <= 0) fuelDeficit += cost - (fuelBuffer + fuelAllocated);
			return false;
		}

	}


	/**
	 * Cargo inventory and some inventory manipulation methods.
	 * 
	 * @author MightyPork
	 */
	public class MinerCargoInventory extends InventoryBasic implements IInventory {
		/**
		 * inventory
		 */
		public MinerCargoInventory() {
			super(PC_Lang.tr("pc.miner.chestName"), 54);
		}
		
		@Override
		public void closeChest() {
			super.closeChest();
			updateLevel();
		}

		/**
		 * Compress blocks in inventory to storage blocks.<br>
		 * <b>Time expensive, do only if needed!</b>
		 */
		private void compressInv() {
			if (st.level < 5) {
				return;
			}
			if (!cfg.compressBlocks) {
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


			return id != mod_PCcore.powerCrystal.blockID && Block.blocksList[is.itemID] != null
					&& Block.blocksList[is.itemID].blockMaterial.isSolid();
		}

		/**
		 * @return torch was consumed from inventory
		 */
		private boolean getTorch() {
			for (int i = 0; i < cargo.getSizeInventory(); i++) {
				if (cargo.getStackInSlot(i) != null && cargo.getStackInSlot(i).itemID == Block.torchWood.blockID) {
					cargo.decrStackSize(i, 1);
					return true;
				}
			}
			return false;
		}

		/**
		 * @return inventory has some torches
		 */
		private boolean hasTorch() {
			for (int i = 0; i < cargo.getSizeInventory(); i++) {
				if (cargo.getStackInSlot(i) != null && cargo.getStackInSlot(i).itemID == Block.torchWood.blockID) {
					return true;
				}
			}
			return false;
		}


		/**
		 * Deposit depositable blocks to nearby chest.
		 */
		private void depositToNearbyChest() {

			int y1 = (int) Math.floor(posY + 0.0002F);
			int x1 = (int) Math.round(posX);
			int z1 = (int) Math.round(posZ);

			for (int x = x1 - 2; x <= x1 + 1; x++) {
				for (int y = y1; y <= y1 + 1; y++) {
					for (int z = z1 - 2; z <= z1 + 1; z++) {
						IInventory chest = PC_InvUtils.getCompositeInventoryAt(worldObj, new PC_CoordI(x, y, z));
						if (chest != null) {
							// cycle through and deposit.
							for (int i = 0; i < cargo.getSizeInventory(); i++) {
								boolean stored = false;
								boolean crystal = false;
								ItemStack stack = cargo.getStackInSlot(i);
								if (stack != null && stack.itemID != mod_PCcore.powerDust.shiftedIndex && stack.itemID != Block.torchWood.blockID
										&& stack.itemID != Item.bucketEmpty.shiftedIndex && stack.itemID != Item.bucketLava.shiftedIndex
										&& (!cfg.keepAllFuel || PC_InvUtils.getFuelValue(stack, FUEL_STRENGTH) == 0)) {

									if (stack.itemID == mod_PCcore.powerCrystal.blockID) {
										if (stack.stackSize <= 1) {
											continue;
										}
										crystal = true;
										stack = cargo.getStackInSlot(i).copy();

										stack.stackSize--;
										cargo.decrStackSize(i, stack.stackSize);
									}

									stored = PC_InvUtils.addWholeItemStackToInventory(chest, stack);

								}

								if (stored && !crystal) {
									cargo.setInventorySlotContents(i, null);
								}
							}

							if (shouldMakeEffects()) {
								worldObj.playSoundAtEntity(PCmo_EntityMiner.this, "random.pop", 0.2F, 0.5F + rand.nextFloat() * 0.3F);
							}

							return;
						}
					}
				}
			}
		}



		/**
		 * Find block for halfstep
		 * 
		 * @return metadata
		 */
		private ItemStack getHalfStep() {
			for (int pass = 0; pass < 3; pass++) {
				for (int i = 0; i < cargo.getSizeInventory(); i++) {
					if (isItemGoodForHalfStep(cargo.getStackInSlot(i), pass)) {
						ItemStack returned = cargo.decrStackSize(i, 1);

						if (returned.itemID == Block.stairSingle.blockID) {
							return cargo.decrStackSize(i, 1);
						}

						ItemStack step = makeHalfstep(returned);

						PC_InvUtils.addItemStackToInventory(cargo, step.copy());
						return step;
					}
				}
			}
			return null;
		}

		private ItemStack makeHalfstep(ItemStack stack) {
			int id = stack.itemID;
			@SuppressWarnings("unused")
			int dmg = stack.getItemDamage();

			if (id == Block.stone.blockID) {
				return new ItemStack(Block.stairSingle, 1, 0);
			}

			if (id == Block.sandStone.blockID) {
				return new ItemStack(Block.stairSingle, 1, 1);
			}

			if (id == Block.planks.blockID) {
				return new ItemStack(Block.stairSingle, 1, 2);
			}

			if (id == Block.cobblestone.blockID) {
				return new ItemStack(Block.stairSingle, 1, 3);
			}

			if (id == Block.brick.blockID) {
				return new ItemStack(Block.stairSingle, 1, 4);
			}

			if (id == Block.stoneBrick.blockID) {
				return new ItemStack(Block.stairSingle, 1, 5);
			}

			return new ItemStack(Block.stairSingle, 1, 0);
		}

		/**
		 * Check if stack can be crafted to halfstep.
		 * 
		 * @param is stack
		 * @param pass pass; 0 = stone, 1 = planks+smoothstone, 2 =
		 *            sandstone+stonebrick+brick.
		 * @return is good
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


	}


	// STATUS VARIABLES and FLAGS


	/** Speed based on level. */
	private static final double[] MOTION_SPEED = { 0.04D, 0.05D, 0.06D, 0.07D, 0.08D, 0.09D, 0.11D, 0.12D };



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
//		if (stack.itemID == Block.cobblestone.blockID) {
//			return (DESTROY & COBBLE) != 0;
//		}
//		if (stack.itemID == Block.dirt.blockID) {
//			return (DESTROY & DIRT) != 0;
//		}
//		if (stack.itemID == Block.gravel.blockID) {
//			return (DESTROY & GRAVEL) != 0;
//		}
		return false;
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
		st.commandList = "";
		st.commandListSaved = "";
		st.currentCommand = -1;
		alignToBlocks();
		st.paused = false;
		st.halted = false;
		resetStatus();
	}

	/**
	 * Pause program execution when the GUI is opened for running Miner.
	 */
	public void pauseProgram() {
		if (st.paused) {
			return;
		}

		// pack last instruction into saved codebuffer
		st.commandListSaved = new String(st.commandList);
		String instruction = Character.toString(PCmo_Command.getCharFromInt(st.currentCommand));
		if (st.stepCounter > 0 && PCmo_Command.isCommandMove(st.currentCommand)) {
			instruction = (st.currentCommand == PCmo_Command.FORWARD ? "" : "-") + Integer.toString(st.stepCounter);
		} else if (instruction.equals("?")) {
			instruction = "";
		}

		st.commandListSaved = instruction + " " + st.commandListSaved;
		st.commandList = "";

		resetStatus();
		st.paused = true;
	}

	/**
	 * Resume program after GUI is closed.<br>
	 * If not paused, do nothing.
	 */
	public void resumeProgram() {
		if (!st.paused) {
			return;
		}

		resetStatus();
		st.commandList = new String(st.commandListSaved.trim());
		st.commandListSaved = "";

		st.paused = false;
	}



	/**
	 * Reset various status flags and counters, but keep fuel buffer.
	 */
	private void resetStatus() {
		st.currentCommand = -1;
		roundRotation();
		st.target.x = (int) posX;
		st.target.z = (int) posZ;
		resetMineCounter();
		st.stepCounter = 0;
		st.fuelDeficit = 0;
		st.fuelAllocated = 0;
		st.bridgeDone = false;
		st.upStepLaid = false;
	}

	/**
	 * Set keyboard control status
	 * 
	 * @param yes keyboard control enabled
	 */
	public void setKeyboardControl(boolean yes) {
		st.keyboardControlled = yes;
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
		st.commandList = st.commandList.trim();
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
				brain.launchProgram();
			} catch (PCmo_CommandException e) {}
		}
		if (i == PCmo_Command.RESET) {
			resetEverything();
		} else {
			Character chr = PCmo_Command.getCharFromInt(i);
			if (chr.equals('?')) {
				return;
			}
			st.commandList += chr.toString();
		}
	}

	/**
	 * Append given code to the command list.<br>
	 * Used for "turn around" command, which sends RR.
	 * 
	 * @param code code to append
	 * @throws PCmo_CommandException if code is invalid
	 */
	public void appendCode(String code) throws PCmo_CommandException {
		st.commandList = st.commandList + " " + PCmo_Command.parseCode(code);
	}

	/**
	 * Put code to the commands list.
	 * 
	 * @param code the code to parse and start
	 * @throws PCmo_CommandException if code is invalid
	 */
	public void setCode(String code) throws PCmo_CommandException {
		st.commandList = PCmo_Command.parseCode(code);
	}

	/**
	 * Get next command from buffer or step count, prepare for execution and
	 * start it.
	 * 
	 * @return the command, or -1 if buffer is empty
	 */
	private int getNextCommand() {
		st.commandList = st.commandList.trim();

		// if there are no more commands, try to get some from weasel
		if (st.commandList.length() <= 0) {
			brain.run();
		}

		if (st.commandList.length() > 0) {
			Character first = st.commandList.charAt(0);

			int cmd = PCmo_Command.getIntFromChar(first);
			if (cmd != -1) {
				st.commandList = st.commandList.substring(1);
				if (cmd == PCmo_Command.FORWARD || cmd == PCmo_Command.BACKWARD || cmd == PCmo_Command.UP) {
					st.stepCounter = 1;
				}
				return cmd;
			} else if (Character.isDigit(first) || first.equals('-')) {
				String numbuff = Character.toString(first);
				st.commandList = st.commandList.substring(1);

				while (st.commandList.length() > 0) {
					first = Character.valueOf(st.commandList.charAt(0));

					if (Character.isDigit(first)) {
						numbuff += first.toString();
					} else {
						break;
					}
					st.commandList = st.commandList.substring(1);
				}

				try {
					st.stepCounter = Integer.valueOf(numbuff);
					cmd = st.stepCounter > 0 ? PCmo_Command.FORWARD : PCmo_Command.BACKWARD;

					st.stepCounter = Math.abs(st.stepCounter);
					if (st.stepCounter == 0) {
						return -1;
					}

					return cmd;

				} catch (NumberFormatException nfe) {
					return -1;
				}
			} else {
				st.commandList = st.commandList.substring(1);
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
		if (st.currentCommand == PCmo_Command.FORWARD || st.currentCommand == PCmo_Command.UP) {
			if (rotationYaw == 0) {
				return posX <= st.target.x;
			}
			if (rotationYaw == 90) {
				return posZ <= st.target.z;
			}
			if (rotationYaw == 180) {
				return posX >= st.target.x;
			}
			if (rotationYaw == 270) {
				return posZ >= st.target.z;
			}
		} else if (st.currentCommand == PCmo_Command.BACKWARD) {
			if (rotationYaw == 0) {
				return posX >= st.target.x;
			}
			if (rotationYaw == 90) {
				return posZ >= st.target.z;
			}
			if (rotationYaw == 180) {
				return posX <= st.target.x;
			}
			if (rotationYaw == 270) {
				return posZ <= st.target.z;
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
		return Math.abs(posX - st.target.x);
	}

	/**
	 * Get Miner's absolute distance to the target Z coordinate
	 * 
	 * @return distance
	 */
	private double getTargetDistanceZ() {
		return Math.abs(posZ - st.target.z);
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
		st.rotationRemaining = 0;
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
		if (st.currentCommand > -1) {
			st.realCommand = st.currentCommand;
			prevPosX = posX = (int) Math.round(posX);
			prevPosX = posZ = (int) Math.round(posZ);
			int x = (int) Math.round(posX);
			int z = (int) Math.round(posZ);
			int y = (int) Math.floor(posY + 0.0002F);

			roundRotation();

			if (st.currentCommand == PCmo_Command.DEPOSIT) {
				cargo.depositToNearbyChest();
				st.currentCommand = -1;

			} else if (st.currentCommand == PCmo_Command.DISASSEMBLY) {
				turnIntoBlocks();
				return;
			} else if (st.currentCommand == PCmo_Command.BRIDGE_ENABLE) {

				cfg.bridgeEnabled = true;
				st.currentCommand = -1;
			} else if (st.currentCommand == PCmo_Command.BRIDGE_DISABLE) {
				cfg.bridgeEnabled = false;
				st.currentCommand = -1;

			} else if (st.currentCommand == PCmo_Command.MINING_ENABLE) {
				cfg.miningEnabled = true;
				st.currentCommand = -1;
			} else if (st.currentCommand == PCmo_Command.MINING_DISABLE) {
				cfg.miningEnabled = false;
				st.currentCommand = -1;

			} else if (st.currentCommand == PCmo_Command.LAVA_ENABLE) {
				cfg.lavaFillingEnabled = true;
				st.currentCommand = -1;
			} else if (st.currentCommand == PCmo_Command.LAVA_DISABLE) {
				cfg.lavaFillingEnabled = false;
				st.currentCommand = -1;

			} else if (st.currentCommand == PCmo_Command.WATER_ENABLE) {
				cfg.waterFillingEnabled = true;
				st.currentCommand = -1;
			} else if (st.currentCommand == PCmo_Command.WATER_DISABLE) {
				cfg.waterFillingEnabled = false;
				st.currentCommand = -1;

			} else if (st.currentCommand == PCmo_Command.DOWN) {
				if (!cfg.miningEnabled) {
					st.currentCommand = -1;
				} else {
					resetMineCounter();
					st.mineCounter[4] = -1;
					st.mineCounter[5] = -1;
				}

			} else if (st.currentCommand == PCmo_Command.UP) {
				if (!cfg.miningEnabled) {
					st.currentCommand = -1;
				} else {
					if (st.addFuelForCost(getStepCost())) {

						resetMineCounter();
						if (rotationYaw == 0) {
							st.target = new PC_CoordI(x - 1, y, z);
						}
						if (rotationYaw == 90) {
							st.target = new PC_CoordI(x, y, z - 1);
						}
						if (rotationYaw == 180) {
							st.target = new PC_CoordI(x + 1, y, z);
						}
						if (rotationYaw == 270) {
							st.target = new PC_CoordI(x, y, z + 1);
						}

						if (!isOnHalfStep()) {
							st.mineCounter[6] = -1;
							st.mineCounter[7] = -1;
							st.mineCounter[8] = -1;
							st.mineCounter[9] = -1;
							st.mineCounter[10] = -1;
							st.mineCounter[11] = -1;

							st.upStepLaid = false;
						} else {
							st.currentCommand = PCmo_Command.FORWARD;

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

			} else if (st.currentCommand == PCmo_Command.FORWARD) {
				if (st.addFuelForCost(getStepCost())) {
					resetMineCounter();
					st.bridgeDone = false;
					if (rotationYaw == 0) {
						st.target.x = x - 1;
						st.target.z = z;
					}
					if (rotationYaw == 90) {
						st.target.z = z - 1;
						st.target.x = x;
					}
					if (rotationYaw == 180) {
						st.target.x = x + 1;
						st.target.z = z;
					}
					if (rotationYaw == 270) {
						st.target.z = z + 1;
						st.target.x = x;
					}
				}

			} else if (st.currentCommand == PCmo_Command.BACKWARD) {
				if (st.addFuelForCost(getStepCost())) {
					st.bridgeDone = false;
					if (rotationYaw == 0) {
						st.target.x = x + 1;
						st.target.z = z;
					}
					if (rotationYaw == 90) {
						st.target.z = z + 1;
						st.target.x = x;
					}
					if (rotationYaw == 180) {
						st.target.x = x - 1;
						st.target.z = z;
					}
					if (rotationYaw == 270) {
						st.target.z = z - 1;
						st.target.x = x;
					}
				}

			} else if (st.currentCommand == PCmo_Command.LEFT) {

				st.rotationRemaining = -90;

			} else if (st.currentCommand == PCmo_Command.RIGHT) {

				st.rotationRemaining = 90;

			} else if (st.currentCommand == PCmo_Command.NORTH) {

				if (rotationYaw == 0) {
					st.currentCommand = PCmo_Command.RIGHT;
					st.rotationRemaining = 90;
				}
				if (rotationYaw == 180) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = -90;
				}
				if (rotationYaw == 90) {
					st.currentCommand = -1;
				}
				if (rotationYaw == 270) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}

			} else if (st.currentCommand == PCmo_Command.SOUTH) {

				if (rotationYaw == 0) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = -90;
				}
				if (rotationYaw == 180) {
					st.currentCommand = PCmo_Command.RIGHT;
					st.rotationRemaining = 90;
				}
				if (rotationYaw == 90) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 270) {
					st.currentCommand = -1;
				}

			} else if (st.currentCommand == PCmo_Command.EAST) {

				if (rotationYaw == 0) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 180) {
					st.currentCommand = -1;
				}
				if (rotationYaw == 90) {
					st.currentCommand = PCmo_Command.RIGHT;
					st.rotationRemaining = 90;
				}
				if (rotationYaw == 270) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = -90;
				}

			} else if (st.currentCommand == PCmo_Command.WEST) {

				if (rotationYaw == 0) {
					st.currentCommand = -1;
				}
				if (rotationYaw == 180) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = rand.nextBoolean() ? 180 : -180;
				}
				if (rotationYaw == 90) {
					st.currentCommand = PCmo_Command.LEFT;
					st.rotationRemaining = -90;
				}
				if (rotationYaw == 270) {
					st.currentCommand = PCmo_Command.RIGHT;
					st.rotationRemaining = 90;
				}

			} else {
				st.currentCommand = -1;
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
		return MathHelper.clamp_int(st.level / 2, 1, 4);
	}

	/**
	 * Look if there is any player who may appreciate the awesome effects and
	 * sounds.
	 * 
	 * @return should make effects
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
	 * @param pos position
	 * @param block_index index of the block in mining list
	 * 
	 */
	private void playMiningEffect(PC_CoordI pos, int block_index) {
		st.miningTickCounter++;

		if (!shouldMakeEffects()) {
			return;
		}
		int id = pos.getId(worldObj);

		Block block = Block.blocksList[id];
		if (st.miningTickCounter % 8 == 0 && block != null) {
			ModLoader.getMinecraftInstance().sndManager.playSound(block.stepSound.getBreakSound(), pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F,
					(block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
		}

		if (block != null) {
			ModLoader.getMinecraftInstance().effectRenderer.addBlockHitEffects(pos.x, pos.y, pos.z, block_index < 4 ? getSideFromYaw()
					: (block_index < 6 ? 1 : 0));
		}
	}

	/**
	 * Convert "rotation yaw" angle to block side index.
	 * 
	 * @return block side for particles
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

		if (st.mineCounter[loc] <= 0) {

			if (shouldIgnoreBlockForHarvesting(pos, id)) {
				if (st.mineCounter[loc] < 0) {
					st.mineCounter[loc] = 0;
				}
				return;
			}

			if (Block.blocksList[id] != null) {
				int cost = getBlockMiningCost(pos, id);
				if (id == 7 && st.level == 8 && pos.y == 0) {
					cost = -1;
				}

				if (cost > 0) {
					if (st.addFuelForCost(cost)) {
						st.mineCounter[loc] = cost;
					}
				}
			}

		}

		if (st.fuelDeficit == 0 && st.mineCounter[loc] > 0) {
			int step = st.level;
			if (st.mineCounter[loc] < step) {
				step = st.mineCounter[loc];
				st.mineCounter[loc] = 0;
			} else {
				st.mineCounter[loc] -= step;
			}

			st.consumeAllocatedFuel(step);

			if (st.mineCounter[loc] == 0) {
				harvestBlock_do(pos);
			}
		}

		if (st.mineCounter[loc] != 0 && Block.blocksList[id] != null) {
			playMiningEffect(pos, loc);
		}

	}

	/**
	 * @return is mining in progress
	 */
	private boolean isMiningInProgress() {
		for (int counter : st.mineCounter) {
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
		for (int element : st.mineCounter) {
			st.fuelAllocated -= element;
			if (st.fuelAllocated <= 0) {
				st.fuelAllocated = 0;
				break;
			}
		}

		for (int i = 0; i < 4; i++) {
			st.mineCounter[i] = -1;
		}
		for (int i = 4; i < st.mineCounter.length; i++) {
			st.mineCounter[i] = 0;
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

		if (id == 0 || Block.blocksList[id] == null || Block.blocksList[id] instanceof BlockTorch || id == Block.fire.blockID
				|| id == Block.portal.blockID || id == Block.endPortal.blockID || Block.blocksList[id] instanceof BlockFluid || id == 55 || id == 70
				|| id == 72 || PC_BlockUtils.hasFlag(worldObj, pos, "LIFT") || PC_BlockUtils.hasFlag(worldObj, pos, "BELT")) {
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
		if (id == 7 && st.level == 8) {
			return true;
		}
		if (isBlockUnbreakable(id) || PC_BlockUtils.hasFlag(worldObj, pos, "HARVEST_STOP") || PC_BlockUtils.hasFlag(worldObj, pos, "NO_HARVEST")) {
			return false;
		}
		switch (st.level) {
			case 1: // all but rocks and iron
				return Block.blocksList[id].blockMaterial != Material.rock && Block.blocksList[id].blockMaterial != Material.iron
						&& id != mod_PCcore.powerCrystal.blockID;
			case 2: // everything but precious ores (cobble, coal, iron)
				return id != 49 && id != 14 && id != 21 && id != 22 && id != 41 && id != 56 && id != 57 && id != 73 && id != 74
						&& id != mod_PCcore.powerCrystal.blockID;
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
		return (posY - Math.floor(posY + 0.0002)) >= 0.4D;
	}

	/**
	 * Place bridge blocks at target pos; target is X+ Z+.
	 * 
	 * @return false if it run out of material
	 */
	private boolean performBridgeBuilding() {
		if (!cfg.bridgeEnabled) {
			return true;
		}

//		int y = (int) Math.floor(posY - 0.9999F);
		if (isOnHalfStep()) {
			return true;
		}
		if (!bridgeBuilding_do(st.target.offset(0, -1, 0))) {
			return false;
		}
		if (!bridgeBuilding_do(st.target.offset(-1, -1, 0))) {
			return false;
		}
		if (!bridgeBuilding_do(st.target.offset(0, -1, -1))) {
			return false;
		}
		if (!bridgeBuilding_do(st.target.offset(-1, -1, -1))) {
			return false;
		}

		return true;
	}

	/**
	 * Place bridge block at this exact position.
	 * 
	 * @param pos position
	 * @return success
	 */
	private boolean bridgeBuilding_do(PC_CoordI pos) {
		if (checkIfAir(pos, false)) {
			if (st.level < 3) {
				st.currentCommand = -1;
				return false;
			}
			ItemStack fill = cargo.getBlockForBuilding();
			if (fill == null) {
				return false;
			}

			int id = fill.itemID;
			int meta = fill.getItemDamage();
			pos.setBlock(worldObj, id, meta);

			if (shouldMakeEffects()) {
				worldObj.playSoundEffect(pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, Block.blocksList[id].stepSound.getStepSound(),
						(Block.blocksList[id].stepSound.getVolume() + 1.0F) / 2.0F, Block.blocksList[id].stepSound.getPitch() * 0.8F);
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
				ItemStack halfstep = cargo.getHalfStep();
				if (halfstep != null) {
					worldObj.setBlockAndMetadataWithNotify(x, y, z, halfstep.itemID,
							((ItemBlock) halfstep.getItem()).getMetadata(halfstep.getItemDamage()));
				}

			}
		}

		// fix for in front of.
		int id = worldObj.getBlockId(x, y + (step ? -1 : 0), z);
		if (id == 0 || id == 8 || id == 9 || id == 10 || id == 11 || Block.blocksList[id].getCollisionBoundingBoxFromPool(worldObj, x, y, z) == null) {
			ItemStack fill = cargo.getBlockForBuilding();
			if (fill == null) {
				return;
			}

			id = fill.itemID;
			int meta = fill.getItemDamage();
			worldObj.setBlockAndMetadataWithNotify(x, y + (step ? -1 : 0), z, id, meta);
			if (shouldMakeEffects()) {
				worldObj.playSoundEffect(x + 0.5F, (float) y + (step ? -1 : 0) + 0.5F, z + 0.5F, Block.blocksList[id].stepSound.getStepSound(),
						(Block.blocksList[id].stepSound.getVolume() + 1.0F) / 2.0F, Block.blocksList[id].stepSound.getPitch() * 0.8F);
			}
		}
	}



	/**
	 * Fill nearby lava with stones from inventory, get lava into bucket.
	 */
	private void fillLavaNearby() {
		if (st.level < 4 || !cfg.lavaFillingEnabled) {
			return;
		}

		int y1 = (int) Math.floor(posY + 0.0002F);
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
							ItemStack fill = cargo.getBlockForBuilding();
							if (fill != null) {
								fillId = fill.itemID;
								fillMeta = fill.getItemDamage();
							}
						}
						worldObj.setBlockAndMetadataWithNotify(x, y, z, fillId, fillMeta);
						if (Block.blocksList[fillId] != null) {
							if (shouldMakeEffects()) {
								worldObj.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.blocksList[fillId].stepSound.getStepSound(),
										(Block.blocksList[fillId].stepSound.getVolume() + 1.0F) / 2.0F,
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
		if (st.level < 6 || !cfg.waterFillingEnabled) {
			return;
		}

		int y1 = (int) Math.floor(posY + 0.0002F);
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
							ItemStack fill = cargo.getBlockForBuilding();
							if (fill != null) {
								fillId = fill.itemID;
								fillMeta = fill.getItemDamage();
							}
						}
						worldObj.setBlockAndMetadataWithNotify(x, y, z, fillId, fillMeta);
						if (Block.blocksList[fillId] != null) {
							if (shouldMakeEffects()) {
								worldObj.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.blocksList[fillId].stepSound.getStepSound(),
										(Block.blocksList[fillId].stepSound.getVolume() + 1.0F) / 2.0F,
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

		int y1 = (int) Math.floor(posY + 0.0002F);

		if (isOnHalfStep()) {
			y1++;
		}

		int x1 = targetPos ? st.target.x : (int) Math.round(posX);
		int z1 = targetPos ? st.target.z : (int) Math.round(posZ);

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
	 * Place torches on ground and on walls, if enabled.
	 */
	private void performTorchPlacing() {
		if (st.level < 3) {
			return;
		}

		int y = (int) Math.floor(posY + 0.0002F);
		int x = (int) Math.round(posX);
		int z = (int) Math.round(posZ);

		if (getBrightness(1.0F) > 0.2F) {
			return;
		}
		if (handleWaterMovement()) {
			return;
		}

		if (!cargo.hasTorch()) {
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

		if (!cfg.torchesOnlyOnFloor) {
			if (worldObj.getBlockId(rightX, y + 1, rightZ) == 0 && torch.canPlaceBlockAt(worldObj, rightX, y + 1, rightZ)) {
				worldObj.setBlockWithNotify(rightX, y + 1, rightZ, torch.blockID);
				cargo.getTorch();
				return;
			}
			if (worldObj.getBlockId(leftX, y + 1, leftZ) == 0 && torch.canPlaceBlockAt(worldObj, leftX, y + 1, leftZ)) {
				worldObj.setBlockWithNotify(leftX, y + 1, leftZ, torch.blockID);
				cargo.getTorch();
				return;
			}
		}

		if (worldObj.getBlockId(rightX, y, rightZ) == 0 && torch.canPlaceBlockAt(worldObj, rightX, y, rightZ)) {
			worldObj.setBlockWithNotify(rightX, y, rightZ, torch.blockID);

			// set on floor if not building stairs.
			if (st.realCommand != PCmo_Command.UP) {
				Block.torchWood.onBlockPlaced(worldObj, rightX, y, rightZ, 1);
			}
			cargo.getTorch();
			return;
		}

		if (worldObj.getBlockId(leftX, y, leftZ) == 0 && torch.canPlaceBlockAt(worldObj, leftX, y, leftZ)) {
			worldObj.setBlockWithNotify(leftX, y, leftZ, torch.blockID);

			// set on floor if not building stairs.
			if (st.realCommand != PCmo_Command.UP) {
				Block.torchWood.onBlockPlaced(worldObj, leftX, y, leftZ, 1);
			}
			cargo.getTorch();
			return;
		}

		return;
	}



	/**
	 * fill bucket with lava
	 * 
	 * @return lava was removed (to bucket)
	 */
	private boolean lavaFillBucket() {
		for (int i = 0; i < cargo.getSizeInventory(); i++) {
			if (cargo.getStackInSlot(i) != null) {
				int id = cargo.getStackInSlot(i).itemID;
				if (id == Item.bucketEmpty.shiftedIndex) {
					cargo.setInventorySlotContents(i, new ItemStack(Item.bucketLava, 1, 0));
					return true;
				}
			}
		}
		return false;
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

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		// EXECUTE CURRENT COMMAND

		boolean stop = st.programmingGuiOpen;

		if (!stop) {
			if (st.fuelDeficit > 0) {
				if (st.addFuelForCost(st.fuelDeficit)) {
					st.fuelDeficit = 0;
					prepareForCommandExecution();
				}
			}

			st.releaseAllocatedFuelIfNoLongerNeeded();

			// normalize fuel deficit
			if (st.fuelDeficit < 0) {
				st.fuelDeficit = 0;
			}

			// if stopped and fuel deficit stays > 0
			if (st.currentCommand == -1 && st.fuelDeficit != 0) {
				st.fuelDeficit = 0;
			}

			// if there is enough fuel for current operation
			if (st.fuelDeficit == 0) {

				// execute rotation and check if target angle is reached.
				if (PCmo_Command.isCommandTurn(st.currentCommand)) {
					motionX = motionZ = 0;
					posX = st.target.x;
					posZ = st.target.z;

					if (Math.abs(st.rotationRemaining) < 3) {
						st.currentCommand = -1;
						posX = st.target.x;
						posZ = st.target.z;
						st.rotationRemaining = 0;
						roundRotation();
					} else {
						playMotionEffect();
						int step = MathHelper.clamp_int(st.level, 3, 7);
						step = MathHelper.clamp_int(step, 0, Math.abs(st.rotationRemaining));

						int incr = st.rotationRemaining > 0 ? step : -step;
						rotationYaw = rotationYaw + incr;
						if (rotationYaw < 0) {
							rotationYaw = prevRotationYaw = 360F + rotationYaw;
						}
						if (rotationYaw > 360F) {
							rotationYaw = prevRotationYaw = rotationYaw - 360F;
						}

						st.rotationRemaining -= incr;
					}
				}

				if (st.currentCommand != -1) {
					burriedFix(false);
				}

				// check if movement destination is reached
				if (PCmo_Command.isCommandMove(st.currentCommand) || (st.currentCommand == PCmo_Command.UP && isMiningDone())) {

					roundRotation();
					performTorchPlacing();

					// if target is reached
					if (isMinerAtTargetPos()) {

						// consume step cost from buffer
						st.consumeAllocatedFuel(getStepCost());

						// fill nearby liquids
						fillLavaNearby();
						fillWaterNearby();

						// normalize position
						if (getTargetDistanceX() > 0.03125D) {
							posX = prevPosX = st.target.x;
						}

						if (getTargetDistanceZ() > 0.03125D) {
							posZ = prevPosZ = st.target.z;
						}

						// decrement step counter - used for commands like 100
						st.stepCounter--;
						if (st.stepCounter <= 0) {
							// out of code - will ask weasel next turn.
							st.currentCommand = -1;
							if (st.commandList.length() == 0) {
								// if no more commands, stop.
								motionX = 0;
								motionZ = 0;
							}
							// normalize step counter
							st.stepCounter = 0;
						} else {
							// prepare next target position.
							prepareForCommandExecution();
						}
					}
				}

				// perform movement and optional mining forwards
				// previous command may have set waitingForFuel to step cost.
				if (PCmo_Command.isCommandMove(st.currentCommand) || st.currentCommand == PCmo_Command.DOWN || st.currentCommand == PCmo_Command.UP) {

					// round rotation to world sides.
					roundRotation();

					boolean fw = (st.currentCommand == PCmo_Command.FORWARD);
					boolean dwn = (st.currentCommand == PCmo_Command.DOWN);
					boolean up = (st.currentCommand == PCmo_Command.UP);
					boolean back = (st.currentCommand == PCmo_Command.BACKWARD);

					// for checks
					int x = (int) Math.round(posX);
					int y = (int) Math.floor(posY + 0.0002F);
					if (isOnHalfStep()) {
						y += 1;
					}
					int z = (int) Math.round(posZ);

					boolean bridgeOk = true;
					if (!st.bridgeDone) {
						bridgeOk = performBridgeBuilding();
						if (!bridgeOk) {
							// bridge building failed!

						} else {
							st.bridgeDone = true;
						}
					}

					// if it cant move, stop.
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

					double motionAdd = (MOTION_SPEED[st.level - 1] * ((fw || up) ? 1 : -1)) * 0.5D;

					if (rotationYaw == 180) {
						if (!miningDone && (!back) && cfg.miningEnabled) {
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
						if (isLocationEmpty(st.target.setY(y)) && canMove) {
							motionX += motionAdd;
						}
						motionZ = 0;
					}

					if (rotationYaw == 270) {
						if (!miningDone && (!back) && cfg.miningEnabled) {

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
						if (isLocationEmpty(st.target.setY(y)) && canMove) {
							motionZ += motionAdd;
						}
						motionX = 0;
					}

					if (rotationYaw == 0) {
						if (!miningDone && (!back) && cfg.miningEnabled) {

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
						if (isLocationEmpty(st.target.setY(y)) && canMove) {
							motionX -= motionAdd;
						}
						motionZ = 0;
					}

					if (rotationYaw == 90) {
						if (!miningDone && (!back) && cfg.miningEnabled) {

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
						if (isLocationEmpty(st.target.setY(y)) && canMove) {
							motionZ -= motionAdd;
						}
						motionX = 0;
					}

					if (dwn && !isMiningInProgress()) {
						st.currentCommand = -1;
					}

					if (up && isMiningDone() && !st.upStepLaid) {
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
						st.upStepLaid = true;
					}

					// stop if bumped into wall
					if ((!cfg.miningEnabled || !isMiningInProgress() || st.currentCommand == PCmo_Command.BACKWARD)
							&& !isLocationEmpty(st.target.setY(y))) {

						burriedFix(fw && cfg.miningEnabled);

						if (!isLocationEmpty(st.target.setY(y))) {
							if (!cfg.miningEnabled || st.currentCommand == PCmo_Command.BACKWARD) {
								st.currentCommand = -1;
								resetMineCounter();
								st.consumeAllocatedFuel(getStepCost());
								st.target.x = (int) Math.round(posX);
								st.target.z = (int) Math.round(posZ);
								st.target.y = (int) Math.round(posY + 0.001F);

								st.stepCounter = 0;
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
		double d7 = MOTION_SPEED[st.level - 1];
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
		if (!stop && onGround && st.currentCommand == -1) {
			int oldCmd = st.currentCommand;
			st.currentCommand = getNextCommand(); // gets command and removes it
													// from queue
			if (st.currentCommand != -1 && st.currentCommand != oldCmd) {
				alignToBlocks();
			}
			if (st.currentCommand == -1 && !st.keyboardControlled) {
				alignToBlocks();
			}

			prepareForCommandExecution();

			if (st.currentCommand != -1) {
				setSprinting(true);
			}
		}

		// slow down if no more commands are available (halt)
		if (st.currentCommand == -1 && st.commandList.length() == 0) {
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
				boolean compress = (id == Block.sand.blockID || id == Item.snowball.shiftedIndex || id == Item.diamond.shiftedIndex
						|| (id == Item.dyePowder.shiftedIndex && dmg == 4) || id == Item.lightStoneDust.shiftedIndex);

				if (shouldDestroyStack(entity.item)) {
					entity.setDead();
					continue;
				}

				if (PC_InvUtils.addItemStackToInventory(cargo, entity.item)) {
					entity.setDead();
				}
				if (xtal) {
					updateLevel();
				}
				if (compress && cfg.compressBlocks) {
					cargo.compressInv();
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
					PC_InvUtils.addItemStackToInventory(cargo, new ItemStack(Item.arrow, 1, 0));
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
		if (d2 >= 0.001D) {
			d2 = MathHelper.sqrt_double(d2);
			d /= d2;
			d1 /= d2;
			double d3 = 1.0D / d2;
			if (d3 > 1.0D) {
				d3 = 1.0D;
			}
			d *= d3;
			d1 *= d3;
			d *= 0.05D;
			d1 *= 0.05D;
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

		PC_Utils.saveToNBT(tag, "Settings", cfg);
		PC_Utils.saveToNBT(tag, "Status", st);
		PC_Utils.saveToNBT(tag, "Brain", brain);

		PC_InvUtils.saveInventoryToNBT(tag, "CargoInv", cargo);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {

		PC_Utils.loadFromNBT(tag, "Settings", cfg);
		PC_Utils.loadFromNBT(tag, "Status", st);
		PC_Utils.loadFromNBT(tag, "Brain", brain);

		PC_InvUtils.loadInventoryFromNBT(tag, "CargoInv", cargo);

		if (st.keyboardControlled) {
			PCmo_MinerControlHandler.setMinerForKeyboardControl(this, true);
		}
	}

	// === PLAYER INTERACTION ===

	/** The Console GUI opened for this miner. */
	public boolean openedGui = false;

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer) {
			return true;
		}

		if (!worldObj.isRemote) {
			if (entityplayer.isSneaking()) {
				st.programmingGuiOpen = true;
				openedGui = true;
				PC_Utils.openGres(entityplayer, new PCmo_GuiMinerConsole(this));
				return true;
			}

			// set for keyboard control or open gui.
			if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == mod_PCcore.activator.shiftedIndex) {
				setKeyboardControl(!st.keyboardControlled);

			} else {
				openedGui = true;
				ModLoader.openGUI(entityplayer, new GuiChest(entityplayer.inventory, cargo));
			}
		}
		return true;
	}

	// === WATCHER ===

	@SuppressWarnings("javadoc")
	public void setDamageTaken(int i) {
		dataWatcher.updateObject(19, Integer.valueOf(i));
	}

	@SuppressWarnings("javadoc")
	public int getDamageTaken() {
		return dataWatcher.getWatchableObjectInt(19);
	}

	@SuppressWarnings("javadoc")
	public void setTimeSinceHit(int i) {
		dataWatcher.updateObject(17, Integer.valueOf(i));
	}

	@SuppressWarnings("javadoc")
	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	@SuppressWarnings("javadoc")
	public void setForwardDirection(int i) {
		dataWatcher.updateObject(18, Integer.valueOf(i));
	}

	@SuppressWarnings("javadoc")
	public int getForwardDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}


	// SPAWNING AND DESPAWNING

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
					if (pos.getId(world) == steel && pos.offset(1, 0, 0).getId(world) == steel && pos.offset(1, 0, 1).getId(world) == steel
							&& pos.offset(0, 0, 1).getId(world) == steel) {

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
		st.minerBeingCreated = true; // disable crystal counting.

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
		PC_InvUtils.moveStacks(inv, cargo);

		// remove blocks.
		removeSpawnStructure(world, pos);

		// update level.
		st.minerBeingCreated = false;
		cargo.closeChest();

		setLocationAndAngles((double) pos.x + 1, pos.y, (double) pos.z + 1, (rot * 90F), 0.0F);
		st.target = new PC_CoordI(pos.x + 1, pos.y, pos.z + 1);
		world.spawnEntityInWorld(this);
		return true;
	}

	/**
	 * count crystals and update level; turn to blocks if there arent any.
	 */
	private void updateLevel() {
		if (!st.minerBeingCreated) {
			int cnt = PC_InvUtils.countPowerCrystals(cargo);
			if (cnt == 0) {
				turnIntoBlocks();
				return;
			}

			st.level = Math.min(cnt, 8);

			cfg.bridgeEnabled &= (st.level >= 3);
			cfg.waterFillingEnabled &= (st.level >= 6);
			cfg.lavaFillingEnabled &= (st.level >= 4);
		}
	}



	/**
	 * Despawn the miner, recreate build structure at it's position; Called when
	 * miner is killed or "to blocks" key is pressed
	 */
	private void turnIntoBlocks() {
		st.minerBeingCreated = true;
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
			PC_InvUtils.moveStacks(cargo, inv);
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

}
