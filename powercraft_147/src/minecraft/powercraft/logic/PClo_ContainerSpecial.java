package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;

public class PClo_ContainerSpecial extends PC_GresBaseWithInventory<PClo_TileEntitySpecial>
{
    public PClo_ContainerSpecial(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PClo_TileEntitySpecial)te, o);
    }
}
