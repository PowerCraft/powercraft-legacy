package net.minecraft.src;


import java.util.Random;


/**
 * Replacer's Tile Entity
 * 
 * @author MightyPork, XOR19, Rapus
 */
public class PCma_TileEntityReplacer extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory {

	/** the building stack */
	public ItemStack buildBlock;
	private static final int MAXSTACK = 1;
	private static final int SIZE = 1;
	/** offset coordinate for replacing */
	public PC_CoordI coordOffset = new PC_CoordI(0, 1, 0);
	/** particle frame shown */
	public boolean aidEnabled = false;
	private PC_Color aidcolor;

	/** state (redstone). To prevent action on each neighbour block change. */
	public boolean state = false;
	private Random rand;
	private boolean init = false;
	/**
	 * metadata of the block at the time it was replaced from world to inventory.
	 *  Is cleared when user touches the inventory slot.
	 */
	public int extraMeta = -1;






	@Override
	public void updateEntity() {

		if (!init) {
			init = true;
			Random rnd = new Random((145896555 + xCoord) ^ yCoord ^ (zCoord ^ 132));

			double used = 2D;

			double r = rnd.nextDouble() * 1D;
			used -= r;
			double g = rnd.nextDouble() * 1D;
			used -= g;
			double b = used;

			if (rnd.nextBoolean()) {
				double f = r;
				r = g;
				g = f;
			}

			if (rnd.nextBoolean()) {
				double f = g;
				g = b;
				b = f;
			}

			if (rnd.nextBoolean()) {
				double f = b;
				b = r;
				r = f;
			}

			aidcolor = new PC_Color(r, g, b);

			rand = new Random();
		}
		
		
		

		if (aidEnabled) {

			double d = xCoord + rand.nextFloat();
			double d1 = yCoord + 1.1D;
			double d2 = zCoord + rand.nextFloat();

			int a = rand.nextInt(3);
			int b = rand.nextInt(3);

			ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(worldObj, new PC_CoordD(d, d1, d2), aidcolor, new PC_CoordI(), 0));

			for (int q = 0; q < 8; q++) {

				d = xCoord + coordOffset.x + rand.nextFloat();
				d1 = yCoord + coordOffset.y + rand.nextFloat();
				d2 = zCoord + coordOffset.z + rand.nextFloat();

				a = rand.nextInt(3);
				b = rand.nextInt(3);
				while (a == b) {
					b = rand.nextInt(3);
				}
				boolean aa = rand.nextBoolean();
				boolean bb = rand.nextBoolean();

				switch (a) {
					case 0:
						d = aa ? Math.floor(d) : Math.ceil(d);
						break;
					case 1:
						d1 = aa ? Math.floor(d1) : Math.ceil(d1);
						break;
					case 2:
						d2 = aa ? Math.floor(d2) : Math.ceil(d2);
						break;
				}

				switch (b) {
					case 0:
						d = bb ? Math.floor(d) : Math.ceil(d);
						break;
					case 1:
						d1 = bb ? Math.floor(d1) : Math.ceil(d1);
						break;
					case 2:
						d2 = bb ? Math.floor(d2) : Math.ceil(d2);
						break;
				}

				ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(worldObj, new PC_CoordD(d, d1, d2), aidcolor, new PC_CoordI(), 0));

			}
		}

		super.updateEntity();
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		if (stack.getItem() instanceof ItemBlock) {
			return true;
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return buildBlock;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (j > 0) {
			ItemStack itemStack = buildBlock;
			buildBlock = null;
			return itemStack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (buildBlock != null) {
			ItemStack itemstack = buildBlock;
			buildBlock = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		buildBlock = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Block Replacer";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");

		if (nbttaglist.tagCount() > 0) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(0);
			buildBlock = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}

		PC_Utils.readWrappedFromNBT(nbttagcompound, "targetPos", coordOffset);
		state = nbttagcompound.getBoolean("state");
		aidEnabled = nbttagcompound.getBoolean("aid");
		extraMeta = nbttagcompound.getInteger("extraMeta");

		if (coordOffset.equals(new PC_CoordI(0, 0, 0))) {
			coordOffset.setTo(0, 1, 0);
		}
		init = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		NBTTagList nbttaglist = new NBTTagList();
		if (buildBlock != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			buildBlock.writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("Items", nbttaglist);
		nbttagcompound.setBoolean("state", state);
		nbttagcompound.setBoolean("aid", aidEnabled);
		nbttagcompound.setInteger("extraMeta", extraMeta);

		PC_Utils.writeWrappedToNBT(nbttagcompound, "targetPos", coordOffset);
	}

	@Override
	public int getInventoryStackLimit() {
		return MAXSTACK;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return PC_InvUtils.addWholeItemStackToInventory(this, stack);
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return true;
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return canPlayerInsertStackTo(slot, stack);
	}

}
