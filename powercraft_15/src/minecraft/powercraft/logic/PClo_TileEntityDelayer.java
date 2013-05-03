package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.block.PC_Block;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;

public class PClo_TileEntityDelayer extends PC_TileEntity
{
	@PC_ClientServerSync(clientChangeAble=false)
	private int type = 0;
	@PC_ClientServerSync
	private boolean stateBuffer[] = new boolean[20];
	private int remainingTicks = 0;
    private int ticks = 20;
    
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	type = stack.getItemDamage();
    }

    public int getType()
    {
    	return type;
    }

    public boolean[] getStateBuffer()
    {
    	return stateBuffer;
    }

    public int getDelay()
    {
        return getStateBuffer().length;
    }

    public void setDelay(int delay)
    {
    	stateBuffer = new boolean[delay];
    	ticks = delay;
    	notifyChanges("stateBuffer");
    }

    public void resetRemainingTicks(){
    	remainingTicks = ticks;
    }
    
    public boolean decRemainingTicks(){
    	if(remainingTicks>0){
	    	remainingTicks--;
	    	if(remainingTicks==0){
	    		remainingTicks = 0;
	    	}else{
	    		return false;
	    	}
    	}
    	return true;
    }
    
    @Override
    public void updateEntity(){
        boolean stop = false;
        boolean reset = false;

        PC_Block b = PC_Utils.getBlock(worldObj, getCoord());
        
        if (getType() == PClo_DelayerType.FIFO)
        {
            stop = b.getRedstonePowereValueFromInput(worldObj, xCoord, yCoord, zCoord, PC_Direction.RIGHT)>0;
            reset = b.getRedstonePowereValueFromInput(worldObj, xCoord, yCoord, zCoord, PC_Direction.LEFT)>0;
        }
        
        if (!stop || reset)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, PC_Utils.getBID(worldObj, xCoord, yCoord, zCoord), PClo_App.delayer.tickRate(worldObj));
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }
}
