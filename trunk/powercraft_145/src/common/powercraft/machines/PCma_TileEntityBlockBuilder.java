package powercraft.machines;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemMonsterPlacer;
import net.minecraft.src.ItemReed;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_FakePlayer;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;

public class PCma_TileEntityBlockBuilder extends PC_TileEntity implements IInventory {

	private ItemStack stacks[];
	private Random rand;
	private PC_FakePlayer fakeplayer;

	/**
	 * @return forge - can update; false
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}

	/**
	 * Block Builder's TE
	 */
	public PCma_TileEntityBlockBuilder() {
		stacks = new ItemStack[9];
		rand = new Random();
	}

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (stacks[i] != null) {
			if (stacks[i].stackSize <= j) {
				ItemStack itemstack = stacks[i];
				stacks[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = stacks[i].splitStack(j);
			if (stacks[i].stackSize == 0) {
				stacks[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	/**
	 * Use some random item from the dispenser inventory.
	 */
	public void useItem() {

		if (fakeplayer == null) {
			fakeplayer = new PC_FakePlayer(worldObj);
		}

		int maxdist = 150;
		if (worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == Block.stoneBrick.blockID) {
			maxdist = 1;
		}
		int i = -1;
		int j = 1;
		for (int k = 0; k < stacks.length; k++) {
			if (stacks[k] != null && rand.nextInt(j++) == 0) {
				i = k;
			}
		}

		if (i >= 0) {
			for (int dist = 0; dist < maxdist; dist++) {
				int state = try2useItem(getStackInSlot(i).copy(), dist);
				if (state == -1) {
					return;
				}
				if (state == 0) {
					continue;
				}
				if (state != 0) {
					if (GameInfo.isSoundEnabled()) {
						worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
					}
				}
				if (state == 1) {
					decrStackSize(i, 1);
				}
				if (state == 2) {
					getStackInSlot(i).damageItem(1, fakeplayer);
					if (getStackInSlot(i) != null && getStackInSlot(i).stackSize <= 0) {
						setInventorySlotContents(i, null);
					}
				}
				return;
			}
		} else {
			if (GameInfo.isSoundEnabled()) {
				worldObj.playAuxSFX(1001, xCoord, yCoord, zCoord, 0);
			}
			return;
		}
	}

	/**
	 * Use item, return state flag
	 * 
	 * @param itemstack stack to use
	 * @param dist distance form device
	 * @return 0 = nothing happened to the stack. 1 = decrement stack size, 2 =
	 *         damage item
	 */
	private int try2useItem(ItemStack itemstack, int dist) {
		int x = xCoord, y = yCoord, z = zCoord;

		int l = worldObj.getBlockMetadata(x, y, z) & 7;
		int incX = Facing.offsetsXForSide[l];
		int incZ = Facing.offsetsZForSide[l];

		x += dist * incX;
		z += dist * incZ;

		PC_VecI front = new PC_VecI(x + incX, y, z + incZ);
		PC_VecI below = new PC_VecI(x + incX, y - 1, z + incZ);
		PC_VecI above = new PC_VecI(x + incX, y + 1, z + incZ);


		int idFront = GameInfo.getBID(worldObj, front);
		int metaFront = GameInfo.getMD(worldObj, front);

		int idBelow = GameInfo.getBID(worldObj, below);
		int metaBelow = GameInfo.getMD(worldObj, below);

		int idAbove = GameInfo.getBID(worldObj, above);
		int metaAbove = GameInfo.getMD(worldObj, above);

		int id = idFront;

		// try to put minecart
		if (itemstack.getItem() instanceof ItemMinecart) {
			
			if (GameInfo.isBlock(worldObj, front, "PCtr_BlockBelt") || Block.blocksList[id] instanceof BlockRail) {
				if (!worldObj.isRemote) {
					worldObj.spawnEntityInWorld(new EntityMinecart(worldObj, (float) x + incX + 0.5F, y + 0.5F, (float) z + incZ + 0.5F,
							((ItemMinecart) itemstack.getItem()).minecartType));
				}
				return 1;
			}
		}

		// ending block
		if (id == 49
				|| id == 7
				|| id == 98
				|| (GameInfo.hasFlag(worldObj, front, PC_Utils.HARVEST_STOP))) {
			return -1;
		}

		// try to place front
		if (itemstack.getItem() instanceof ItemBlock) {

			if(GameInfo.hasFlag(itemstack, PC_Utils.NO_BUILD)){
				return 0;
			}
			
			ItemBlock item = ((ItemBlock) itemstack.getItem());

			if (Block.blocksList[item.shiftedIndex].canPlaceBlockAt(worldObj, x + incX, y, z + incZ)) {
				worldObj.setBlockAndMetadataWithNotify(x + incX, y, z + incZ, item.shiftedIndex, item.getMetadata(itemstack.getItemDamage()));
				return 1;
			}

			if (isEmptyBlock(idFront) && Block.blocksList[item.shiftedIndex].canPlaceBlockAt(worldObj, x + incX * 2, y, z + incZ * 2)) {
				worldObj.setBlockAndMetadataWithNotify(x + incX * 2, y, z + incZ * 2, item.shiftedIndex, item.getMetadata(itemstack.getItemDamage()));
				return 1;
			}

			return 0;
		}

		// use on front block (usually bonemeal on crops)
		if (!isEmptyBlock(idFront) && !(itemstack.getItem() instanceof ItemReed)) {
			System.out.println("try2useItem");
			int dmgOrig = itemstack.getItemDamage();
			int sizeOrig = itemstack.stackSize;

			if (itemstack.getItem().onItemUse(itemstack, fakeplayer, worldObj, x + incX, y, z + incZ, 1, 0.0f, 0.0f, 0.0f)) {

				System.out.println("OK");
				
				if (itemstack.getItem() instanceof ItemMonsterPlacer) {
					return 1;
				}

				int idFrontNew = worldObj.getBlockId(x + incX, y, z + incZ);
				int metaFrontNew = worldObj.getBlockMetadata(x + incX, y, z + incZ);
				int idAboveNew = worldObj.getBlockId(x + incX, y + 1, z + incZ);
				int metaAboveNew = worldObj.getBlockMetadata(x + incX, y + 1, z + incZ);

				int dmgNew = itemstack.getItemDamage();
				int sizeNew = itemstack.stackSize;

				// if not bonemeal, or if target block was changed
				if (!(itemstack.getItem().shiftedIndex == Item.dyePowder.shiftedIndex && itemstack.getItemDamage() == 15)
						|| (idFront != idFrontNew || metaFront != metaFrontNew || idAbove != idAboveNew || metaAbove != metaAboveNew)) {
					if (dmgOrig != dmgNew) {
						return 2;
					}
					if (sizeOrig != sizeNew) {
						return 1;
					}
				}
			}
		}

		// use below
		if (isEmptyBlock(idFront) && !isEmptyBlock(idBelow)) {
			int dmg1 = itemstack.getItemDamage();
			int size1 = itemstack.stackSize;

			if (itemstack.getItem().onItemUse(itemstack, fakeplayer, worldObj, x + incX, y - 1, z + incZ, 1, 0.0f, 0.0f, 0.0f)) {

				if (itemstack.getItem() instanceof ItemMonsterPlacer) {
					return 1;
				}

				int idBelowNew = worldObj.getBlockId(x + incX, y - 1, z + incZ);
				int metaBelowNew = worldObj.getBlockMetadata(x + incX, y - 1, z + incZ);
				int idFrontNew = worldObj.getBlockId(x + incX, y, z + incZ);
				int metaFrontNew = worldObj.getBlockMetadata(x + incX, y, z + incZ);

				int dmg2 = itemstack.getItemDamage();
				int size2 = itemstack.stackSize;
				// if not bonemeal, or if target block was changed
				if (!(itemstack.getItem().shiftedIndex == Item.dyePowder.shiftedIndex && itemstack.getItemDamage() == 15)
						|| (idBelow != idBelowNew || metaBelow != metaBelowNew || idFront != idFrontNew || metaFront != metaFrontNew)) {
					if (dmg1 != dmg2) {
						return 2;
					}
					if (size1 != size2) {
						return 1;
					}
				}
			}
		}

		return 0;
	}

	private boolean isEmptyBlock(int id) {
		return (id == 0 || id == 8 || id == 9 || id == 10 || id == 11 || id == Block.snow.blockID);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		stacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Block Dispenser";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
	public void closeChest() {}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (stacks[par1] != null) {
			ItemStack itemstack = stacks[par1];
			stacks[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

}
