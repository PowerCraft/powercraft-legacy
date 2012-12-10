package powercraft.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntityMobSpawner;

public class PCco_MobSpawnerSetter implements PC_IPacketHandler
{
    @Override
    public boolean handleIncomingPacket(EntityPlayer player, Object[] o)
    {
        TileEntityMobSpawner tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
        tems.setMobID((String)o[3]);
        return true;
    }
}
