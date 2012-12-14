package powercraft.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntityMobSpawner;
import powercraft.management.PC_Utils.ValueWriting;

public class PCco_ClientMobSpawnerSetter extends PCco_MobSpawnerSetter {

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		TileEntityMobSpawner tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
		tems.setMobID((String)o[3]);
		ValueWriting.setPrivateValue(TileEntityMobSpawner.class, tems, 8, null);
		tems.getMobEntity();
		return true;
	}
	
}
