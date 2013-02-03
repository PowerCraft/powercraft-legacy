package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityDelayer extends PC_TileEntity
{
	public static final String TYPE = "type", STATEBUFFER = "stateBuffer";
    //private int type = 0;
	//private boolean stateBuffer[] = new boolean[20];
	private int remainingTicks = 0;
    private int ticks = 20;
    
    public PClo_TileEntityDelayer(){
    	setData(TYPE, 0);
    	setData(STATEBUFFER, new boolean[20]);
    }
    
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	setData(TYPE, stack.getItemDamage());
    }

    public int getType()
    {
    	return (Integer)getData(TYPE);
    }

    public boolean[] getStateBuffer()
    {
    	return (boolean[])getData(STATEBUFFER);
    }

    public void updateStateBuffer()
    {
        //PC_PacketHandler.setTileEntity(this, "stateBuffer", stateBuffer);
    }

    public int getDelay()
    {
        return getStateBuffer().length;
    }

    public void setDelay(int delay)
    {
    	boolean [] stateBuffer = new boolean[delay];
    	ticks = delay;
    	setData(STATEBUFFER, stateBuffer);
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
    public void updateEntity()
    {
        int rot = PClo_BlockDelayer.getRotation_static(GameInfo.getMD(worldObj, xCoord, yCoord, zCoord));
        boolean stop = false;
        boolean reset = false;

        if (getType() == PClo_DelayerType.FIFO)
        {
            stop = GameInfo.poweredFromInput(worldObj, xCoord, yCoord, zCoord, PC_Utils.RIGHT, rot);
            reset = GameInfo.poweredFromInput(worldObj, xCoord, yCoord, zCoord, PC_Utils.LEFT, rot);
        }
        
        if (!stop || reset)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, GameInfo.getBID(worldObj, xCoord, yCoord, zCoord), PClo_App.delayer.tickRate());
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }
}
