package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityGate extends PC_TileEntity
{
	public static final String TYPE = "type", INP = "inp";
    //private int type = 0;
    //private int inp = 0;

	public PClo_TileEntityGate(){
		setData(TYPE, 0);
    	setData(INP, 0);
    }
	
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	setData(TYPE, stack.getItemDamage());
    	setData(INP, PClo_GateType.ROT_L_D_R);
    }

    public int getType()
    {
        return (Integer)getData(TYPE);
    }

    public int getInp()
    {
    	return (Integer)getData(INP);
    }

    public void rotInp()
    {
    	setData(INP, PClo_RepeaterType.change(getType(), getInp()));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

}
