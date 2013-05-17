package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.tileentity.PC_TileEntityWithInventory;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;

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
    	PC_Block block = PC_Utils.getBlock(worldObj, xCoord, yCoord, zCoord);
    	if(block==null || worldObj.isRemote){
    		return;
    	}
        int nextUpdate = 0;
        boolean shouldState = false;
        PC_Direction rot = block.getRotation(PC_Utils.getMD(worldObj, xCoord, yCoord, zCoord));
        int xAdd = rot.getOffset().x, zAdd = rot.getOffset().z;

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

            case PClo_SpecialType.CHEST_EMPTY:{
            	IInventory inv = PC_InventoryUtils.getInventoryAt(worldObj, xCoord - xAdd, yCoord, zCoord - zAdd);
            	if(inv!=null){
            		shouldState = PC_InventoryUtils.getInventoryCountOf(inv, getStackInSlot(0))==0;
            	}
            	break;

            }case PClo_SpecialType.CHEST_FULL:{
            	IInventory inv = PC_InventoryUtils.getInventoryAt(worldObj, xCoord - xAdd, yCoord, zCoord - zAdd);
            	if(inv!=null){
            		shouldState = PC_InventoryUtils.getInventorySpaceFor(inv, getStackInSlot(0))==0;
            	}
            	break;

            } case PClo_SpecialType.SPECIAL:
                PClo_BlockSpecial.preventSpawnerSpawning(worldObj, xCoord + 1, yCoord, zCoord);
                PClo_BlockSpecial.preventSpawnerSpawning(worldObj, xCoord - 1, yCoord, zCoord);
                PClo_BlockSpecial.preventSpawnerSpawning(worldObj, xCoord, yCoord + 1, zCoord);
                PClo_BlockSpecial.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord + 1);
                PClo_BlockSpecial.preventSpawnerSpawning(worldObj, xCoord, yCoord, zCoord - 1);

            default:
                return;
        }

        if (PClo_BlockSpecial.isActive(worldObj, xCoord, yCoord, zCoord) != shouldState)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, PC_Utils.getBID(worldObj, xCoord, yCoord, zCoord), 1);
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
    
    @Override
	public int getPickMetadata() {
		return type;
	}
    
}
