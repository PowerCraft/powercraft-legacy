package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;

public class PCma_ContainerRoaster extends PC_GresBaseWithInventory<PCma_TileEntityRoaster>
{
    public PCma_ContainerRoaster(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityRoaster)te, o);
    }
}
