package powercraft.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityMobSpawner;
import powercraft.api.reflect.PC_ReflectHelper;

public class PCco_ClientMobSpawnerSetter extends PCco_MobSpawnerSetter {

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		TileEntityMobSpawner tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
		tems.setMobID((String)o[3]);
		PC_ReflectHelper.setValue(TileEntityMobSpawner.class, tems, 8, null, Entity.class);
		tems.getMobEntity();
		return true;
	}
	
}
