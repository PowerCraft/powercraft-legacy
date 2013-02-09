package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraft.world.biome.WorldChunkManager;
import powercraft.management.PC_Block;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.inventory.PC_ISpecialAccessInventory;

public class PCma_TileEntityRoaster extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory
{
	
	public static final String BURNTIME = "burnTime", NETHERTIME = "netherTime", NETHERACTIONTIME = "netherActionTime", NONETHERRACK = "noNetherrack", ACTIVE = "active";
	
	private static Random random = new Random();
	
	private ItemStack roasterContents[] = new ItemStack[SIZE];

    public static final int MAXSTACK = 16;

    public static final int SIZE = 9;
//
//    public int burnTime = 0;
//
//    public int netherTime = 0;
//
//    public int netherActionTime = 100;
//    private boolean noNetherrack = false;
//
//    public boolean isActive;
    
    public PCma_TileEntityRoaster()
    {
    	setData(BURNTIME, 0);
    	setData(NETHERTIME, 0);
    	setData(NETHERACTIONTIME, 100);
    	setData(NONETHERRACK, false);
    	setData(ACTIVE, false);
    }
	
    public int getBurnTime() {
		return (Integer)getData(BURNTIME);
	}

	public void setBurnTime(int burnTime) {
		setData(BURNTIME, burnTime);
	}

	public int getNetherTime() {
		return (Integer)getData(NETHERTIME);
	}

	public void setNetherTime(int netherTime) {
		setData(NETHERTIME, netherTime);
	}

	public int getNetherActionTime() {
		return (Integer)getData(NETHERACTIONTIME);
	}

	public void setNetherActionTime(int netherActionTime) {
		setData(NETHERACTIONTIME, netherActionTime);
	}

	public boolean isNoNetherrack() {
		return (Boolean)getData(NONETHERRACK);
	}

	public void setNoNetherrack(boolean noNetherrack) {
		setData(NONETHERRACK, noNetherrack);
	}

	public boolean isActive() {
		return (Boolean)getData(ACTIVE);
	}

	public void setActive(boolean isActive) {
		setData(ACTIVE, isActive);
	}

	@Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return stack != null && GameInfo.isFuel(stack);
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return canPlayerInsertStackTo(slot, stack);
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return PC_InvUtils.addWholeItemStackToInventory(this, stack);
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return roasterContents[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (roasterContents[i] != null)
        {
            if (roasterContents[i].stackSize <= j)
            {
                ItemStack itemstack = roasterContents[i];
                roasterContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = roasterContents[i].splitStack(j);

            if (roasterContents[i].stackSize == 0)
            {
                roasterContents[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        roasterContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return Lang.tr("tile.PCmaRoaster.name") + " - " + Lang.tr("pc.roaster.insertFuel");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return MAXSTACK;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest()
    {
    	setData(NONETHERRACK, false);
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void updateEntity()
    {
        if (!PCma_BlockRoaster.isIndirectlyPowered(worldObj, xCoord, yCoord, zCoord))
        {
            return;
        }

        PC_Block laserB = ModuleInfo.getPCBlockByName("PCli_BlockLaser");
        boolean laser = false;

        if (laserB != null)
        {
            laser = GameInfo.getBID(worldObj, xCoord, yCoord + 1, zCoord) == laserB.blockID;
        }

        if (getBurnTime() > 0)
        {
            setBurnTime(getBurnTime() - (laser ? 4 : 2));
        }

        if (getBurnTime() <= 0)
        {
            addFuelForTime(40);
        }

        if (!laser)
        {
            smeltItems();
        }

        if (!laser && getBurnTime() > 0)
        {
            burnCreatures();
        }

        if (getNetherTime() > 0)
        {
            setNetherTime(getNetherTime()-1);
        }

        if (getNetherTime() <= 0 && !isNoNetherrack())
        {
            addNetherrack();
        }

        if (getNetherActionTime() > 0 && getNetherTime() > 0)
        {
        	setNetherActionTime(getNetherActionTime()-1);
        }

        if (getNetherActionTime() <= 0)
        {
            int success = 0;

            for (int i = 0; i < 10; i++)
            {
                if (netherAction())
                {
                    success++;
                }

                if (success == 4)
                {
                    break;
                }
            }

            WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();

            if (worldchunkmanager != null)
            {
                BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(xCoord, zCoord);

                if (biomegenbase instanceof BiomeGenHell)
                {
                	setNetherActionTime(50 + random.nextInt(150));
                }
                else
                {
                	setNetherActionTime(100 + random.nextInt(200));
                }
            }
            else
            {
            	setNetherActionTime(100 + random.nextInt(200));
            }
        }
    }

    public void smeltItems()
    {
        List<EntityItem> itemsList = worldObj.getEntitiesWithinAABB(EntityItem.class,
                AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        nextItem:

        for (EntityItem entityitem : itemsList)
        {
            if (entityitem.isDead || PCma_App.roasterIgnoreBlockIDs.contains(entityitem.func_92014_d().itemID))
            {
                continue nextItem;
            }

            ItemStack result = getResult(entityitem.func_92014_d());

            if (result == null)
            {
                continue nextItem;
            }

            if (getBurnTime() <= getItemSmeltTime(entityitem.func_92014_d()))
            {
                if (!addFuelForItem(entityitem.func_92014_d()))
                {
                    continue nextItem;
                }
            }

            if (getBurnTime() >= getItemSmeltTime(entityitem.func_92014_d()))
            {
            	setBurnTime(getBurnTime() - getItemSmeltTime(entityitem.func_92014_d()));
                EntityItem eitem = new EntityItem(worldObj, entityitem.posX - 0.1F + random.nextFloat() * 0.2F, entityitem.posY, entityitem.posZ
                        - 0.1F + random.nextFloat() * 0.2F, result.copy());
                eitem.motionX = entityitem.motionX;
                eitem.motionY = entityitem.motionY;
                eitem.motionZ = entityitem.motionZ;
                eitem.delayBeforeCanPickup = 7;

                if (!worldObj.isRemote)
                {
                    worldObj.spawnEntityInWorld(eitem);
                }

                if (--entityitem.func_92014_d().stackSize <= 0)
                {
                    entityitem.setDead();
                }
            }
        }
    }

    public void burnCreatures()
    {
        if (getBurnTime() <= 0)
        {
            return;
        }

        List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class,
                AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        nextEliving:

        for (EntityLiving eliving : entities)
        {
            if (eliving.isDead)
            {
                continue nextEliving;
            }

            if (!eliving.isImmuneToFire())
            {
                eliving.attackEntityFrom(DamageSource.inFire, 3);
            }

            if (!eliving.isWet())
            {
                eliving.setFire(15);
            }
        }
    }

    public boolean netherAction()
    {
        int x = -6 + random.nextInt(13);
        int z = -6 + random.nextInt(13);

        for (int y = -2; y <= 2; y++)
        {
            int id = worldObj.getBlockId(xCoord + x, yCoord + y, zCoord + z);
            int meta = worldObj.getBlockMetadata(xCoord + x, yCoord + y, zCoord + z);

            if (id == Block.netherStalk.blockID)
            {
                if (meta < 3)
                {
                    worldObj.setBlockMetadataWithNotify(xCoord + x, yCoord + y, zCoord + z, ++meta);
                    return true;
                }
            }

            if (id == Block.gravel.blockID)
            {
                worldObj.setBlockWithNotify(xCoord + x, yCoord + y, zCoord + z, Block.slowSand.blockID);
                return true;
            }
        }

        return false;
    }

    private boolean addFuelForItem(ItemStack itemstack)
    {
        for (int s = 0; s < getSizeInventory(); s++)
        {
            int bt = GameInfo.getFuelValue(getStackInSlot(s), 1.0D);

            if (bt > 0)
            {
            	setBurnTime(getBurnTime()+bt);
                decrStackSize(s, 1);

                if (getBurnTime() >= getItemSmeltTime(itemstack))
                {
                    return true;
                }
            }
        }

        if (getBurnTime() >= getItemSmeltTime(itemstack))
        {
            return true;
        }

        return false;
    }

    private boolean addFuelForTime(int time)
    {
        for (int s = 0; s < getSizeInventory(); s++)
        {
            int bt = GameInfo.getFuelValue(getStackInSlot(s), 1.0D);

            if (bt > 0)
            {
            	setBurnTime(getBurnTime()+bt);
                decrStackSize(s, 1);

                if (getBurnTime() >= time)
                {
                    return true;
                }
            }
        }

        if (getBurnTime() >= time)
        {
            return true;
        }

        return false;
    }

    private void addNetherrack()
    {
        for (int s = 0; s < getSizeInventory(); s++)
        {
            if (getStackInSlot(s) != null && getStackInSlot(s).itemID == Block.netherrack.blockID)
            {
            	setNetherTime(getNetherTime()+600);
                decrStackSize(s, 1);
                setNoNetherrack(false);
                return;
            }
        }
        setNoNetherrack(true);
    }

    private ItemStack getResult(ItemStack item)
    {
    	return GameInfo.getSmeltingResult(item);
    }

    private int getItemSmeltTime(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemFood)
        {
            return 180;
        }

        if (stack.itemID == Block.wood.blockID)
        {
            return 300;
        }

        return 350;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (roasterContents[par1] != null)
        {
            ItemStack itemstack = roasterContents[par1];
            roasterContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return false;
    }
    
    @Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return getInventoryStackLimit();
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}
    
}
