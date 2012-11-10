package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldChunkManager;
import powercraft.core.PC_Block;
import powercraft.core.PC_ISpecialAccessInventory;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PCma_TileEntityRoaster extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory {
	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		return stack != null && PC_Utils.isFuel(stack);
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return canPlayerInsertStackTo(slot, stack);
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return PC_InvUtils.addWholeItemStackToInventory(this, stack);
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return true;
	}

	private ItemStack roasterContents[];
	private Random random;
	/** max allowes stack size */
	public static final int MAXSTACK = 16;
	/** # of slots */
	public static final int SIZE = 9;
	/** fuel in the fireplace buffer */
	public int burnTime = 0;
	/** netherrack in fireplace buffer */
	public int netherTime = 0;
	/** timer till next "growing tick" to warts */
	public int netherActionTime = 100;
	private boolean noNetherrack = false;

	/**
	 * roaster TE
	 */
	public PCma_TileEntityRoaster() {
		roasterContents = new ItemStack[SIZE];
		random = new Random();
	}

	@Override
	public int getSizeInventory() {
		return SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return roasterContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (roasterContents[i] != null) {
			if (roasterContents[i].stackSize <= j) {
				ItemStack itemstack = roasterContents[i];
				roasterContents[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = roasterContents[i].splitStack(j);
			if (roasterContents[i].stackSize == 0) {
				roasterContents[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		roasterContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return PC_Utils.tr("tile.PCmaRoaster.name") + " - " + PC_Utils.tr("pc.roaster.insertFuel");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);

		burnTime = nbttagcompound.getInteger("burning");
		netherTime = nbttagcompound.getInteger("netherTime");
		netherActionTime = nbttagcompound.getInteger("netherActionTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);

		nbttagcompound.setInteger("burning", burnTime);
		nbttagcompound.setInteger("netherTime", netherTime);
		nbttagcompound.setInteger("netherActionTime", netherActionTime);
	}

	@Override
	public int getInventoryStackLimit() {
		return MAXSTACK;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {
		noNetherrack = false; // force check for netherrack
	}

	/**
	 * @return forge = can't update, false
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		// not powered -> off
		if (!PCma_BlockRoaster.isIndirectlyPowered(worldObj, xCoord, yCoord, zCoord)) {
			return;
		}

		PC_Block laserB = PC_Utils.getPCBlockByName("PCli_BlockLaser");
		
		boolean laser = false;
		if(laserB!=null){
			 laser = PC_Utils.getBID(worldObj, xCoord, yCoord + 1, zCoord) == laserB.blockID;
		}

		if (burnTime > 0) {
			burnTime -= laser ? 4 : 2; // laser consumes more
		}
		// fuel.
		if (burnTime <= 0) {
			addFuelForTime(40);
		}
		if (!laser) {
			smeltItems();
		}
		if (!laser && burnTime > 0) {
			burnCreatures();
		}
		if (netherTime > 0) {
			netherTime--;
		}

		if (netherTime <= 0 && !noNetherrack) {
			addNetherrack();
		}

		if (netherActionTime > 0 && netherTime > 0) {
			netherActionTime--;
		}

		if (netherActionTime <= 0) {
			int success = 0;
			for (int i = 0; i < 10; i++) {
				if (netherAction()) {
					success++;
				}
				if (success == 4) {
					break;
				}
			}

			//
			WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();
			if (worldchunkmanager != null) {
				BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(xCoord, zCoord);
				if (biomegenbase instanceof BiomeGenHell) {
					netherActionTime = 50 + random.nextInt(150);
				} else {
					netherActionTime = 100 + random.nextInt(200);
				}
			} else {
				netherActionTime = 100 + random.nextInt(200);
			}

		}

	}

	/**
	 * Do smelt items above the device.
	 */
	public void smeltItems() {
		List<EntityItem> itemsList = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class,
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));

		nextItem:
		for (EntityItem entityitem : itemsList) {

			if (entityitem.isDead || mod_PowerCraftMachines.roasterIgnoreBlockIDs.contains(entityitem.item.itemID)) {
				continue nextItem;
			}
			ItemStack result = getResult(entityitem.item);
			if (result == null) {
				continue nextItem;
			}

			if (burnTime <= getItemSmeltTime(entityitem.item)) {
				if (!addFuelForItem(entityitem.item)) {
					continue nextItem;
				}
			}

			if (burnTime >= getItemSmeltTime(entityitem.item)) {
				// DO smelting.
				burnTime -= getItemSmeltTime(entityitem.item);
				EntityItem eitem = new EntityItem(worldObj, entityitem.posX - 0.1F + random.nextFloat() * 0.2F, entityitem.posY, entityitem.posZ
						- 0.1F + random.nextFloat() * 0.2F, result.copy());
				eitem.motionX = entityitem.motionX;
				eitem.motionY = entityitem.motionY;
				eitem.motionZ = entityitem.motionZ;
				eitem.delayBeforeCanPickup = 7;
				if(!worldObj.isRemote)
					worldObj.spawnEntityInWorld(eitem);
				if (--entityitem.item.stackSize <= 0) {
					entityitem.setDead();
				}
			}
		}
	}

	/**
	 * Burn creatures on top.
	 */
	public void burnCreatures() {
		if (burnTime <= 0) {
			return;
		}

		List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class,
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));

		nextEliving:
		for (EntityLiving eliving : entities) {
			if (eliving.isDead) {
				continue nextEliving;
			}
			if(!eliving.isImmuneToFire()){
				eliving.attackEntityFrom(DamageSource.inFire, 3);
			}
			if (!eliving.isWet()) {
				eliving.setFire(15);
			}
		}
	}

	/**
	 * make netherwarts grow
	 * 
	 * @return some warts affected
	 */
	public boolean netherAction() {
		int x = -6 + random.nextInt(13);
		int z = -6 + random.nextInt(13);

		for (int y = -2; y <= 2; y++) {
			int id = worldObj.getBlockId(xCoord + x, yCoord + y, zCoord + z);
			int meta = worldObj.getBlockMetadata(xCoord + x, yCoord + y, zCoord + z);

			if (id == Block.netherStalk.blockID) {
				if (meta < 3) {
					worldObj.setBlockMetadataWithNotify(xCoord + x, yCoord + y, zCoord + z, ++meta);
					return true;
				}
			}

			if (id == Block.gravel.blockID) {
				worldObj.setBlockWithNotify(xCoord + x, yCoord + y, zCoord + z, Block.slowSand.blockID);
				return true;
			}
		}
		return false;
	}

	/**
	 * Add fuel needed to smelt this item
	 * 
	 * @param itemstack
	 * @return fuel added
	 */
	private boolean addFuelForItem(ItemStack itemstack) {
		for (int s = 0; s < getSizeInventory(); s++) {
			int bt = PC_InvUtils.getFuelValue(getStackInSlot(s), 1.0D);
			if (bt > 0) {
				burnTime += bt;
				decrStackSize(s, 1);
				if (burnTime >= getItemSmeltTime(itemstack)) {
					return true;
				}
			}
		}
		if (burnTime >= getItemSmeltTime(itemstack)) {
			return true;
		}
		return false;
	}

	/**
	 * Add fuel which last this time
	 * 
	 * @param time
	 * @return added enough
	 */
	private boolean addFuelForTime(int time) {
		for (int s = 0; s < getSizeInventory(); s++) {
			int bt = PC_InvUtils.getFuelValue(getStackInSlot(s), 1.0D);
			if (bt > 0) {
				burnTime += bt;
				decrStackSize(s, 1);
				if (burnTime >= time) {
					return true;
				}
			}
		}
		if (burnTime >= time) {
			return true;
		}
		return false;
	}

	/**
	 * Add some netherrack
	 */
	private void addNetherrack() {
		for (int s = 0; s < getSizeInventory(); s++) {
			if (getStackInSlot(s) != null && getStackInSlot(s).itemID == Block.netherrack.blockID) {
				netherTime += 600;
				decrStackSize(s, 1);
				noNetherrack = false;
				return;
			}
		}
		noNetherrack = true;
	}

	/**
	 * Get smelting result
	 * 
	 * @param item smelted item
	 * @return resulting stack
	 */
	private ItemStack getResult(ItemStack item) {
		return FurnaceRecipes.smelting().getSmeltingResult(item.getItem().shiftedIndex);
	}

	/**
	 * @param stack
	 * @return how long it takes to smelt (this is used only to measure amount
	 *         of fuel consumed, smelting is instant)
	 */
	private int getItemSmeltTime(ItemStack stack) {
		if (stack.getItem() instanceof ItemFood) {
			return 180;
		}
		if (stack.itemID == Block.wood.blockID) {
			return 300;
		}
		return 350;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (roasterContents[par1] != null) {
			ItemStack itemstack = roasterContents[par1];
			roasterContents[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("burnTime"))
				burnTime = (Integer)o[p++];
			else if(var.equals("netherTime"))
				netherTime = (Integer)o[p++];
			else if(var.equals("netherActionTime"))
				netherActionTime = (Integer)o[p++];
			else if(var.equals("noNetherrack"))
				noNetherrack = (Boolean)o[p++];
		}
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"burnTime", burnTime,
				"netherTime", netherTime,
				"netherActionTime", netherActionTime,
				"noNetherrack", noNetherrack
		};
	}
}
