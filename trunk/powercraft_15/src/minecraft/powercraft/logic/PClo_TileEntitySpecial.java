package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.inventory.PC_ISpecialAccessInventory;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.tileentity.PC_TileEntityWithInventory;

public class PClo_TileEntitySpecial extends PC_TileEntityWithInventory
{
	
	@PC_ClientServerSync(clientChangeAble=false)
    private int type = 0;
	
	public PClo_TileEntitySpecial() {
		super("Special Inventory", 1);
	}
	 
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
    	type = stack.getItemDamage();
    }

    public int getType(){
        return type;
    }

    @Override
    public void updateEntity()
    {
        int nextUpdate = 0;
        boolean shouldState = false;
        int rot = PClo_BlockSpecial.getRotation_static(GameInfo.getMD(worldObj, xCoord, yCoord, zCoord));
        int xAdd = 0, zAdd = 0;

        if (rot == 0)
        {
            zAdd = 1;
        }
        else if (rot == 1)
        {
            xAdd = -1;
        }
        else if (rot == 2)
        {
            zAdd = -1;
        }
        else if (rot == 3)
        {
            xAdd = 1;
        }

        switch (getType())
        {
            case PClo_SpecialType.DAY:
                shouldState = worldObj.isDaytime();
                break;

            case PClo_SpecialType.NIGHT:
                shouldState = !worldObj.isDaytime();
                break;

            case PClo_SpecialType.RAIN:
                shouldState = worldObj.isRaining();
                break;

            case PClo_SpecialType.CHEST_EMPTY:
            	shouldState = PC_InventoryUtils.getInventoryCountOf(PC_InventoryUtils.getInventoryAt(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd), getStackInSlot(0), -1)==0;
                break;

            case PClo_SpecialType.CHEST_FULL:
                shouldState = PC_InventoryUtils.getInventorySpaceFor(PC_InventoryUtils.getInventoryAt(worldObj, xCoord + xAdd, yCoord, zCoord + zAdd), getStackInSlot(0), -1)==0;
                break;

            case PClo_SpecialType.SPECIAL:
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord + 1, yCoord, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord - 1, yCoord, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord + 1, zCoord);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord + 1);
                ValueWriting.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord - 1);

            default:
                return;
        }

        if (PClo_BlockSpecial.isActive(worldObj, xCoord, yCoord, zCoord) != shouldState)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, GameInfo.getBID(worldObj, xCoord, yCoord, zCoord), 1);
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        PC_InventoryUtils.loadInventoryFromNBT(nbtTagCompound, "Items", this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        PC_InventoryUtils.saveInventoryToNBT(nbtTagCompound, "Items", this);
    }

    @Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
}
