package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.tileentity.PC_TileEntity;

public class PClo_TileEntityGate extends PC_TileEntity
{
	@PC_ClientServerSync(clientChangeAble=false)
	private int type = 0;
	@PC_ClientServerSync
    private int inp = 0;
	
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	type = stack.getItemDamage();
    	inp = PClo_GateType.ROT_L_D_R;
    }

    public int getType()
    {
        return type;
    }

    public int getInp()
    {
    	return inp;
    }

    public void rotInp()
    {
    	inp = PClo_RepeaterType.change(type, inp);
    	notifyChanges("inp");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

}
