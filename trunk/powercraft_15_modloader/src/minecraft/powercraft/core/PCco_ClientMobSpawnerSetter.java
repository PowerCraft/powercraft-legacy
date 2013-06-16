package powercraft.core;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MobSpawnerBaseLogic;
import net.minecraft.src.TileEntityMobSpawner;
import powercraft.api.reflect.PC_ReflectHelper;

public class PCco_ClientMobSpawnerSetter extends PCco_MobSpawnerSetter {

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		TileEntityMobSpawner tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
		MobSpawnerBaseLogic msbl = tems.func_98049_a();
		msbl.setMobID((String)o[3]);
		PC_ReflectHelper.setValue(MobSpawnerBaseLogic.class, msbl, 9, null, Entity.class);
		msbl.func_98281_h();
		return true;
	}
	
}
