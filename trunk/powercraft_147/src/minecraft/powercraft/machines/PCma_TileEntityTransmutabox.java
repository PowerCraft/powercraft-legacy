package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.inventory.PC_ISpecialAccessInventory;
import powercraft.management.inventory.PC_IStateReportingInventory;

public class PCma_TileEntityTransmutabox extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory, PC_IStateReportingInventory
{
	
	public static final String LOADTIME = "loadTime", NEEDLOADTIME = "needLoadTime", TIMECRITICAL = "needLoadTime";
	
    private ItemStack[] itemStacks = new ItemStack[35];
    private int burnTime = 0;
    //private int loadTime = 0;
    private boolean finished = true;
    //private int needLoadTime = 0;
    //private boolean timeCritical = false;
    
    public PCma_TileEntityTransmutabox(){
    	setData(LOADTIME, 0);
    	setData(NEEDLOADTIME, 0);
    	setData(TIMECRITICAL, false);
    }
    
    public void setTimeCritical(boolean checked) {
    	setData(TIMECRITICAL, checked);
	}
    
    public float getProgress()
    {
    	if((Integer)getData(LOADTIME)<=0)
    		return 0.0f;
        return (Integer)getData(LOADTIME)/(float)(Integer)getData(NEEDLOADTIME);
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return PC_InvUtils.insetItemTo(stack, this, 11, 23)==0;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return true;
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
    	if (slot >= 1 && slot < 9)
            return GameInfo.isFuel(stack);
    	if (slot == 9 || slot == 10)
    		return false;
    	if (slot >= 23)
    		return false;
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        if (slot == 0)
            return false;
        if (slot >= 1 && slot < 9)
            return GameInfo.isFuel(stack);
        if (slot == 9 || slot == 10)
    		return false;
        if (slot >= 23)
    		return false;
        return true;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return slot>=23;
    }

    @Override
	public boolean canDropStackFrom(int slot) {
		return slot!=9&&slot!=10;
	}
    
    @Override
    public int getSizeInventory()
    {
        return 35;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return itemStacks[var1];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
    	if(i==9||i==10)
    		return null;
    	
        if (itemStacks[i] != null)
        {
            if (itemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = itemStacks[i];
                itemStacks[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = itemStacks[i].splitStack(j);

            if (itemStacks[i].stackSize == 0)
            {
                itemStacks[i] = null;
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
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (itemStacks[var1] != null)
        {
            ItemStack itemstack = itemStacks[var1];
            itemStacks[var1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        itemStacks[var1] = var2;

        if (var2 != null && var2.stackSize > getInventoryStackLimit())
        {
            var2.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

	@Override
    public String getInvName()
    {
        return "Transmutabox Inventory";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
	public int getSlotStackLimit(int slot) {
		if(slot==0)
			return 1;
		return 64;
	}
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isContainerEmpty()
    {
        for (int i = 1; i < 23; i++){
        	if(i==9)
        		i=11;
            if (getStackInSlot(i) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isContainerFull()
    {
    	 for (int i = 1; i < 23; i++){
         	if(i==9)
         		i=11;
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
            else if (getStackInSlot(i) != null && getStackInSlot(i + 9) != null)
            {
                if (getStackInSlot(i).stackSize < Math.min(getStackInSlot(i).getMaxStackSize(), getInventoryStackLimit()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean hasContainerNoFreeSlots()
    {
    	 for (int i = 1; i < 23; i++){
         	if(i==9)
         		i=11;
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasInventoryPlaceFor(ItemStack itemStack)
    {
    	 for (int i = 1; i < 23; i++){
         	if(i==9)
         		i=11;
            if (getStackInSlot(i) == null || (getStackInSlot(i).isItemEqual(itemStack) && getStackInSlot(i).stackSize < Math.min(getInventoryStackLimit(), getStackInSlot(i).getMaxStackSize())))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isContainerEmptyOf(ItemStack itemStack)
    {
    	 for (int i = 1; i < 23; i++){
         	if(i==9)
         		i=11;
            if (getStackInSlot(i) != null && !getStackInSlot(i).isItemEqual(itemStack))
            {
                return false;
            }
        }

        return true;
    }
    
    private ItemStack getItemStackForConvertation(){
    	for(int i=11; i<23; i++){
    		ItemStack is = decrStackSize(i, 1);
    		if(is!=null)
    			return is;
    	}
    	return null;
    }
    
    private int sendToOutput(ItemStack is){
    	return PC_InvUtils.insetItemTo(is, this, 23, 35);
    }
    
    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
        	if(finished){
        		if(itemStacks[0] != null){
        			
        			int free=0;
        			for(int i=23; i<35; i++){
        				if(itemStacks[i] == null){
        					free += Math.min(itemStacks[0].getMaxStackSize(), getInventoryStackLimit());
        				}else if(itemStacks[i].isItemEqual(itemStacks[0])){
        					free += Math.min(itemStacks[i].getMaxStackSize(), getInventoryStackLimit())-itemStacks[i].stackSize;
        				}
        			}
        			
        			if(free>0){
        			
        				float outRank = PCma_ItemRanking.getRank(new PC_ItemStack(itemStacks[0]));
        				if(outRank>0){
		        			for(int i=11; i<23; i++){
		        				if(itemStacks[i] != null){
		        					if(itemStacks[i].isItemEqual(itemStacks[0])){
		        						sendToOutput(itemStacks[i]);
		        						itemStacks[i] = null;
		        					}else{
		        						if(PCma_ItemRanking.getRank(new PC_ItemStack(itemStacks[i]))>0){
			        						if((Boolean)getData(TIMECRITICAL)){
				        						itemStacks[9] = itemStacks[i].copy();
				        						itemStacks[i] = null;
			        						}else{
			        							itemStacks[9] = decrStackSize(i, 1);
			        						}
		        						}
				        	    		if(itemStacks[9]!=null)
				        	    			break;
		        					}
		        				}
		        	    	}
		        			
		        			if(itemStacks[9] != null){
		        				
		        				float inRank = PCma_ItemRanking.getRank(new PC_ItemStack(itemStacks[9]));
		        				
		        				if(outRank<=0 || inRank<=0)
		        					return;
		        				
			        			itemStacks[10] = itemStacks[0].copy();
		        				inRank *= itemStacks[9].stackSize;
		        				
			        			if(!(Boolean)getData(TIMECRITICAL)){
			        				
			        				if(inRank>outRank){
			        					int num = (int)(inRank/outRank);
			        					int maxStack = Math.min(itemStacks[10].getMaxStackSize(), getInventoryStackLimit());
			        					maxStack = Math.min(free, maxStack);
			        					if(num>maxStack)
			        						num = maxStack;
			        					itemStacks[10].stackSize = num;
			        					outRank *= num;
			        				}
			        				
			        			}
			        			
			        			setData(NEEDLOADTIME, (int)(outRank / inRank * 20));
			        			
			        			finished = false;
		        			}
        				}
        			}
        		}
        	}else{
	            for (int i = 1; i < 9 && burnTime <= 0; i++)
	            {
	                if (itemStacks[i] != null)
	                {
	                    burnTime += GameInfo.getFuelValue(itemStacks[i], 1f);
	
	                    if (--itemStacks[i].stackSize == 0)
	                    {
	                        itemStacks[i] = null;
	                    }
	                }
	            }
	
	            if (burnTime > 0)
	            {
	            	addToLoadTime(1);
	                burnTime--;
	            }
	            
	            if (((Integer)getData(LOADTIME)) >= ((Integer)getData(NEEDLOADTIME)))
	            {
	            	addToLoadTime((Integer)getData(NEEDLOADTIME));
	                finished = true;
	                sendToOutput(itemStacks[10]);
	                itemStacks[9] = null;
	                itemStacks[10] = null;
	            }
        	}
        }
    }
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }

    public void addToLoadTime(int num){
    	setData(LOADTIME, ((Integer)getData(LOADTIME))+num);
    }
    
    public void addEnergy(int energy){
    	addToLoadTime(energy*2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        PC_InvUtils.loadInventoryFromNBT(nbttagcompound, "Items", this);
        burnTime = nbttagcompound.getInteger("burnTime");
        finished = nbttagcompound.getBoolean("finished");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        PC_InvUtils.saveInventoryToNBT(nbttagcompound, "Items", this);
        nbttagcompound.setInteger("burnTime", burnTime);
        nbttagcompound.setBoolean("finished", finished);
    }

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}
    
}
