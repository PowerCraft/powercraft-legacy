package net.minecraft.src;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;


public class PCtr_TileEntitySeparationBelt extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory {

	Random rand = new Random();

	public PCtr_TileEntitySeparationBelt() {
		separatorContents = new ItemStack[18];
	}

	@Override
	public int getSizeInventory() {
		return 18;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean canUpdate() {
		return true;
	}

	private Hashtable<Entity, Integer> redirList = new Hashtable<Entity, Integer>();

	@Override
	public void updateEntity() {
		Enumeration<Entity> e = redirList.keys();
		while (e.hasMoreElements()) {
			Entity thisItem = e.nextElement();

			if (thisItem.posX < xCoord - 0.2F || thisItem.posY < yCoord - 0.2F || thisItem.posZ < zCoord - 0.2F || thisItem.posX > xCoord + 1.2F || thisItem.posY > yCoord + 1.2F || thisItem.posZ > zCoord + 1.2F) {
				redirList.remove(thisItem);
			}
		}
	}

	public int getDirection(Entity entity) {
		boolean notItem = false;
		ItemStack itemstack = null;
		if (entity instanceof EntityItem) {
			itemstack = ((EntityItem) entity).item;
		} else {
			notItem = true;
			if (entity instanceof EntityPig) {
				itemstack = new ItemStack(Item.porkRaw, 1, 0);
			}
			if (entity instanceof EntitySheep) {
				itemstack = new ItemStack(Block.cloth, 1, 0);
			}
			if (entity instanceof EntityCow) {
				itemstack = new ItemStack(Item.beefRaw, 1, 0);
			}
			if (entity instanceof EntityCreeper) {
				itemstack = new ItemStack(Item.gunpowder, 1, 0);
			}
			if (entity instanceof EntityZombie) {
				itemstack = new ItemStack(Item.rottenFlesh, 1, 0);
			}
			if (entity instanceof EntitySkeleton) {
				itemstack = new ItemStack(Item.bone, 1, 0);
			}
			if (entity instanceof EntitySlime) {
				itemstack = new ItemStack(Item.slimeBall, 1, 0);
			}
			if (entity instanceof EntityEnderman) {
				itemstack = new ItemStack(Item.enderPearl, 1, 0);
			}
			if (entity instanceof EntitySnowman) {
				itemstack = new ItemStack(Item.snowball, 1, 0);
			}
			if (entity instanceof EntityChicken) {
				itemstack = new ItemStack(Item.chickenRaw, 1, 0);
			}
			if (entity instanceof EntityXPOrb) {
				itemstack = new ItemStack(Item.diamond, 1, 0);
			}
			if (entity instanceof EntitySpider) {
				itemstack = new ItemStack(Item.silk, 1, 0);
			}

			if (entity instanceof EntityOcelot) {
				itemstack = new ItemStack(Item.fishRaw, 1, 0);
			}
			if (entity instanceof EntityMooshroom) {
				itemstack = new ItemStack(Block.mushroomRed, 1, 0);
			}
			if (entity instanceof EntityWolf) {
				itemstack = new ItemStack(Item.cookie, 1, 0);
			}
			if (entity instanceof EntityBlaze) {
				itemstack = new ItemStack(Item.blazePowder, 1, 0);
			}
			if (entity instanceof EntityMagmaCube) {
				itemstack = new ItemStack(Item.magmaCream, 1, 0);
			}
			if (entity instanceof EntityPigZombie) {
				itemstack = new ItemStack(Item.goldNugget, 1, 0);
			}
			if (entity instanceof EntityIronGolem) {
				itemstack = new ItemStack(Item.ingotIron, 1, 0);
			}
		}

		if (itemstack == null) {
			return 0;
		}

		if (redirList.containsKey(entity)) {
			return redirList.get(entity);
		}

		// decide for direction.

		int countLeft = 0; // 1
		int countRight = 0; // -1

		boolean group_logs = !mod_PCtransport.separate_wood_types;
		boolean group_planks = !mod_PCtransport.separate_plank_types;

		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null
					&& (stack.isItemEqual(itemstack) || (group_logs && stack.itemID == Block.wood.blockID && itemstack.itemID == Block.wood.blockID) || (group_planks && stack.itemID == Block.planks.blockID && itemstack.itemID == Block.planks.blockID))) {

				int tmpi = i % 6;
				if (tmpi >= 3) {
					countRight += stack.stackSize;
				}
				if (tmpi <= 2) {
					countLeft += stack.stackSize;
				}
			}
		}

		if (countLeft == 0 && countRight == 0) {
			return 0; // forward
		}

		if (countLeft == 0 && countRight > 0) {
			return -1; // right
		}

		if (countLeft > 0 && countRight == 0) {
			return 1; // left
		}

		if (countLeft > 0 && countRight > 0) {
			if (notItem) {
				redirList.put(entity, Integer.valueOf(0));
				return 0;
			}

			int[] translate = { 1, 0, -1 };

			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			int leftX = xCoord, leftZ = zCoord;
			int rightX = xCoord, rightZ = zCoord;

			switch (((PCtr_BlockConveyorSeparator) mod_PCtransport.separationBelt).getRotation(meta)) {
				case 0: // z--
					leftX++;
					rightX--;
					break;
				case 1: // x++
					leftZ++;
					rightZ--;
					break;
				case 2: // z++
					leftX--;
					rightX++;
					break;
				case 3: // x--
					leftZ--;
					rightZ++;
					break;
			}

			translate[2] = (PCtr_BeltBase.isTransporterAt(worldObj, new PC_CoordI(leftX, yCoord, leftZ)) ? -1 : 0);
			translate[0] = (PCtr_BeltBase.isTransporterAt(worldObj, new PC_CoordI(rightX, yCoord, rightZ)) ? 1 : 0);

			if (translate[0] == translate[2]) {
				translate[0] = 1;
				translate[2] = -1;
			}

			// translate[1-x]

			if (itemstack.stackSize == 1) {
				int newredir = (1 + rand.nextInt(countLeft + countRight)) <= countLeft ? 1 : -1;
				redirList.put(entity, Integer.valueOf(translate[1 - newredir]));
				return translate[1 - newredir];
			}

			float fractionLeft = (float) countLeft / (float) (countLeft + countRight);
			int partLeft = Math.round(itemstack.stackSize * fractionLeft);
			int partRight = itemstack.stackSize - partLeft;

			if (partLeft > 0) {

				itemstack.stackSize = partLeft;
				redirList.put(entity, Integer.valueOf(translate[0]));

			} else {
				redirList.put(entity, Integer.valueOf(translate[2]));
				return translate[2];
			}

			if (partRight > 0) {
				ItemStack rightStack = itemstack.copy();
				rightStack.stackSize = partRight;

				EntityItem entityitem2 = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, rightStack);

				entityitem2.motionX = entity.motionX;
				entityitem2.motionY = entity.motionY;
				entityitem2.motionZ = entity.motionZ;

				worldObj.spawnEntityInWorld(entityitem2);

				redirList.put(entityitem2, Integer.valueOf(translate[2]));
			} else {
				redirList.put(entity, Integer.valueOf(translate[0]));
				return translate[0];
			}

			return translate[0]; // left
		}

		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return separatorContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (separatorContents[i] != null) {
			if (separatorContents[i].stackSize <= j) {
				ItemStack itemstack = separatorContents[i];
				separatorContents[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = separatorContents[i].splitStack(j);
			if (separatorContents[i].stackSize == 0) {
				separatorContents[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		separatorContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Item Separator";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		separatorContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < separatorContents.length) {
				separatorContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < separatorContents.length; i++) {
			if (separatorContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				separatorContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	private ItemStack separatorContents[];

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	};

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (separatorContents[par1] != null) {
			ItemStack itemstack = separatorContents[par1];
			separatorContents[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return false;
	}

	@Override
	public boolean needsSpecialInserter() {
		return true;
	}

	@Override
	public boolean canInsertStackTo(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return false;
	}
}
