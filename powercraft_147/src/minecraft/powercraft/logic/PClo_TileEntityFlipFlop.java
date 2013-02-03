package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityFlipFlop extends PC_TileEntity
{
	public static final String TYPE = "type", CLOCK = "clock";
    //private int type = 0;
    //private boolean clock = false;

	public PClo_TileEntityFlipFlop(){
		setData(TYPE, 0);
    	setData(CLOCK, false);
    }
	
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	setData(TYPE, stack.getItemDamage());
    }

    public int getType()
    {
        return (Integer)getData(TYPE);
    }

    public boolean getClock()
    {
        return (Boolean)getData(CLOCK);
    }

    public void setClock(boolean state)
    {
    	setData(CLOCK, state);
    }

}
