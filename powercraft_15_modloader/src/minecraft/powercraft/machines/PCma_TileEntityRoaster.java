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
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.WorldChunkManager;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_RecipeRegistry;
import powercraft.api.tileentity.PC_TileEntityWithInventory;
import powercraft.api.utils.PC_Utils;

public class PCma_TileEntityRoaster extends PC_TileEntityWithInventory{

	private static Random random = new Random();

    @PC_ClientServerSync(clientChangeAble=false)
    public int burnTime = 0;
    @PC_ClientServerSync(clientChangeAble=false)
    public int netherTime = 0;
    @PC_ClientServerSync(clientChangeAble=false)
    public int netherActionTime = 100;
    @PC_ClientServerSync(clientChangeAble=false)
    private boolean noNetherrack = false;
    @PC_ClientServerSync(clientChangeAble=false)
    public boolean isActive;
	
    public PCma_TileEntityRoaster() {
		super(PC_LangRegistry.tr("tile.PCmaRoaster.name") + " - " + PC_LangRegistry.tr("pc.roaster.insertFuel"), 9);
	}

    public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		if(this.burnTime != burnTime){
			this.burnTime = burnTime;
			notifyChanges("burnTime");
		}
	}

	public int getNetherTime() {
		return netherTime;
	}

	public void setNetherTime(int netherTime) {
		if(this.netherTime != netherTime){
			this.netherTime = netherTime;
			notifyChanges("netherTime");
		}
	}

	public int getNetherActionTime() {
		return netherActionTime;
	}

	public void setNetherActionTime(int netherActionTime) {
		if(this.netherActionTime != netherActionTime){
			this.netherActionTime = netherActionTime;
			notifyChanges("netherActionTime");
		}
	}

	public boolean isNoNetherrack() {
		return noNetherrack;
	}

	public void setNoNetherrack(boolean noNetherrack) {
		if(this.noNetherrack != noNetherrack){
			this.noNetherrack = noNetherrack;
			notifyChanges("noNetherrack");
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		if(this.isActive != isActive){
			this.isActive = isActive;
			notifyChanges("isActive");
		}
	}

	@Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return stack != null && PC_RecipeRegistry.isFuel(stack);
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 16;
    }

    @Override
    public void closeChest()
    {
    	setNoNetherrack(false);
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

        PC_Block laserB = PC_BlockRegistry.getPCBlockByName("PCli_BlockLaser");
        boolean laser = false;

        if (laserB != null)
        {
            laser = PC_Utils.getBID(worldObj, xCoord, yCoord + 1, zCoord) == laserB.blockID;
        }

        if (getBurnTime() > 0)
        {
            setBurnTime(getBurnTime() - (laser ? 4 : 2));
        }

        if (getBurnTime() <= 0)
        {
            addFuelForTime(40);
        }

        if (!laser && !worldObj.isRemote)
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
            if (entityitem.isDead || PCma_App.roasterIgnoreBlockIDs.contains(entityitem.getEntityItem().itemID))
            {
                continue nextItem;
            }

            ItemStack result = getResult(entityitem.getEntityItem());

            if (result == null)
            {
                continue nextItem;
            }

            if (getBurnTime() <= getItemSmeltTime(entityitem.getEntityItem()))
            {
                if (!addFuelForItem(entityitem.getEntityItem()))
                {
                    continue nextItem;
                }
            }

            if (getBurnTime() >= getItemSmeltTime(entityitem.getEntityItem()))
            {
            	setBurnTime(getBurnTime() - getItemSmeltTime(entityitem.getEntityItem()));
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

                if (--entityitem.getEntityItem().stackSize <= 0)
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
                	PC_Utils.setMD(worldObj, xCoord + x, yCoord + y, zCoord + z, ++meta);
                    return true;
                }
            }

            if (id == Block.gravel.blockID)
            {
            	PC_Utils.setBID(worldObj, xCoord + x, yCoord + y, zCoord + z, Block.slowSand.blockID, 0);
                return true;
            }
        }

        return false;
    }

    private boolean addFuelForItem(ItemStack itemstack)
    {
    	return addFuelForTime(getItemSmeltTime(itemstack));
    }

    private boolean addFuelForTime(int time)
    {
    	int bt = PC_InventoryUtils.useFuel(this, worldObj, getCoord());
    	while(bt>0){
    		setBurnTime(getBurnTime()+bt);
    		if (getBurnTime() >=  time)
    			return true;
    		bt = PC_InventoryUtils.useFuel(this, worldObj, getCoord());
    	}

        if (getBurnTime() >=  time)
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
    	return PC_RecipeRegistry.getSmeltingResult(item);
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
    
}
