package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.annotation.PC_ClientServerSync;

public class PClo_TileEntityRepeater extends PC_TileEntity
{
	
	@PC_ClientServerSync
	private int type = 0;
	@PC_ClientServerSync
    private int state = 0;
	@PC_ClientServerSync
    private int inp = 0;
	
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	type = stack.getItemDamage();
    }

    public int getType()
    {
        return type;
    }

    public int getState()
    {
    	 return state;
    }

    public void setState(int b)
    {
    	if(state!=b){
    		state = b;
    		notifyChanges("state");
	        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}
    }

    public int getInp()
    {
    	return inp;
    }

    public void change()
    {
    	inp = PClo_RepeaterType.change(type, inp);
    	notifyChanges("inp");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    }
}
