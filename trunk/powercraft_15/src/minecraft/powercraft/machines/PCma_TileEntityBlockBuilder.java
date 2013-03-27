package powercraft.machines;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.entity.PC_FakePlayer;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.tileentity.PC_TileEntityWithInventory;

public class PCma_TileEntityBlockBuilder extends PC_TileEntityWithInventory  {

	private static Random rand = new Random();
	private PC_FakePlayer fakeplayer;

	public PCma_TileEntityBlockBuilder() {
		super("Block Builder", 9);
	}
	
	/**
	 * @return forge - can update; false
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public void setWorldObj(World world) {
		super.setWorldObj(world);
		fakeplayer = new PC_FakePlayer(world);
	}

	/**
	 * Use some random item from the dispenser inventory.
	 */
	
	public boolean useItem(PC_VecI coord) {
		int i = -1;
		int j = 1;
		for (int k = 0; k < inventoryContents.length; k++) {
			if (inventoryContents[k] != null && rand.nextInt(j++) == 0) {
				i = k;
			}
		}
		if (i >= 0) {
			int state = try2useItem(getStackInSlot(i).copy(), coord);
			if (state == -1) {
				return true;
			}
			if (state == 0) {
				return false;
			}
			if (state != 0) {
				if (PC_SoundRegistry.isSoundEnabled()) {
					worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
				}
			}
			if(!worldObj.isRemote){
				if (state == 1) {
					decrStackSize(i, 1);
				}
				if (state == 2) {
					getStackInSlot(i).damageItem(1, fakeplayer);
					if (getStackInSlot(i) != null && getStackInSlot(i).stackSize <= 0) {
						setInventorySlotContents(i, null);
					}
				}
			}
			return true;
		} else {
			if (PC_SoundRegistry.isSoundEnabled()) {
				worldObj.playAuxSFX(1001, xCoord, yCoord, zCoord, 0);
			}
			return false;
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
	private int try2useItem(ItemStack itemstack, PC_VecI front) {

		int l = GameInfo.getMD(worldObj, front) & 7;

		PC_VecI below = new PC_VecI(front.offset(0, -1, 0));
		PC_VecI above = new PC_VecI(front.offset(0, 1, 0));


		int idFront = GameInfo.getBID(worldObj, front);
		int metaFront = GameInfo.getMD(worldObj, front);

		int idBelow = GameInfo.getBID(worldObj, below);
		int metaBelow = GameInfo.getMD(worldObj, below);

		int idAbove = GameInfo.getBID(worldObj, above);
		int metaAbove = GameInfo.getMD(worldObj, above);

		int id = idFront;

		// try to put minecart
		if (itemstack.getItem() instanceof ItemMinecart) {
			
			if (PC_BlockRegistry.isBlock(worldObj, front, "PCtr_BlockBelt") || Block.blocksList[id] instanceof BlockRail) {
				if (!worldObj.isRemote) {
					worldObj.spawnEntityInWorld(EntityMinecart.func_94090_a(worldObj, front.x + 0.5F, front.y + 0.5F, front.z + 0.5F,
							((ItemMinecart) itemstack.getItem()).minecartType));
				}
				return 1;
			}
		}

		// ending block
		if (id == 49
				|| id == 7
				|| id == 98
				|| (PC_MSGRegistry.hasFlag(worldObj, front, PC_Utils.HARVEST_STOP))) {
			return -1;
		}

		// try to place front
		if (itemstack.getItem() instanceof ItemBlock) {

			if(PC_MSGRegistry.hasFlag(itemstack, PC_Utils.NO_BUILD)){
				return 0;
			}
			
			ItemBlock item = ((ItemBlock) itemstack.getItem());

			if (Block.blocksList[item.itemID].canPlaceBlockAt(worldObj, front.x, front.y, front.z)) {
				ValueWriting.setBID(worldObj, front.x, front.y, front.z, item.itemID, item.getMetadata(itemstack.getItemDamage()));
				return 1;
			}

			/*if (isEmptyBlock(idFront) && Block.blocksList[item.itemID].canPlaceBlockAt(worldObj, x + incX * 2, y, z + incZ * 2)) {
				ValueWriting.setBID(worldObj, x + incX * 2, y, z + incZ * 2, item.itemID, item.getMetadata(itemstack.getItemDamage()));
				return 1;
			}*/

			return 0;
		}

		// use on front block (usually bonemeal on crops)
		if (!isEmptyBlock(idFront) && !(itemstack.getItem() instanceof ItemReed)) {
			int dmgOrig = itemstack.getItemDamage();
			int sizeOrig = itemstack.stackSize;

			if (itemstack.getItem().onItemUse(itemstack, fakeplayer, worldObj, front.x, front.y, front.z, 1, 0.0f, 0.0f, 0.0f)) {
				
				if (itemstack.getItem() instanceof ItemMonsterPlacer) {
					return 1;
				}

				int idFrontNew = GameInfo.getBID(worldObj, front);
				int metaFrontNew = GameInfo.getMD(worldObj, front);
				int idAboveNew = GameInfo.getBID(worldObj, above);
				int metaAboveNew = GameInfo.getMD(worldObj, above);

				int dmgNew = itemstack.getItemDamage();
				int sizeNew = itemstack.stackSize;

				// if not bonemeal, or if target block was changed
				if (!(itemstack.getItem().itemID == Item.dyePowder.itemID && itemstack.getItemDamage() == 15)
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

			if (itemstack.getItem().onItemUse(itemstack, fakeplayer, worldObj, below.x, below.y, below.z, 1, 0.0f, 0.0f, 0.0f)) {

				if (itemstack.getItem() instanceof ItemMonsterPlacer) {
					return 1;
				}

				int idBelowNew = GameInfo.getBID(worldObj, below);
				int metaBelowNew = GameInfo.getMD(worldObj, below);
				int idFrontNew = GameInfo.getBID(worldObj, front);
				int metaFrontNew = GameInfo.getMD(worldObj, front);

				int dmg2 = itemstack.getItemDamage();
				int size2 = itemstack.stackSize;
				// if not bonemeal, or if target block was changed
				if (!(itemstack.getItem().itemID == Item.dyePowder.itemID && itemstack.getItemDamage() == 15)
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

}
