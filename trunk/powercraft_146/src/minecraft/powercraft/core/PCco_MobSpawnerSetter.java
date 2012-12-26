package powercraft.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;

public class PCco_MobSpawnerSetter implements PC_IPacketHandler, PC_IMSG
{
    @Override
    public boolean handleIncomingPacket(EntityPlayer player, Object[] o)
    {
        TileEntityMobSpawner tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
        tems.setMobID((String)o[3]);
        ValueWriting.setPrivateValue(TileEntityMobSpawner.class, tems, 9, null);
        return true;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_ON_ACTIVATOR_USED_ON_BLOCK:
			PC_VecI pos = (PC_VecI)obj[3];
			Gres.openGres("SpawnerEditor", (EntityPlayer)obj[1], pos.x, pos.y, pos.z);
	        return true;
		}
		return null;
	}
}
