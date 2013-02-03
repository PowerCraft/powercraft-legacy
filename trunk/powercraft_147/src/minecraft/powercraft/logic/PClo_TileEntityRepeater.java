package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityRepeater extends PC_TileEntity
{
	
	public static final String TYPE = "type", STATE = "state", INP = "inp";
	
    //private int type = 0;
    //private int state = 0;
    //private int inp = 0;

	public PClo_TileEntityRepeater(){
		setData(TYPE, 0);
    	setData(STATE, 0);
    	setData(INP, 0);
    }
	
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	setData(TYPE, stack.getItemDamage());
    	setData(INP, 0);
    }

    public int getType()
    {
        return (Integer)getData(TYPE);
    }

    public int getState()
    {
    	 return (Integer)getData(STATE);
    }

    public void setState(int b)
    {
    	setData(STATE, b);
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getInp()
    {
    	return (Integer)getData(INP);
    }

    public void change()
    {
        setData(INP, PClo_RepeaterType.change(getType(), getInp()));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    }
}
