package net.minecraft.src;

public class PCco_MobSpawnerSetter extends PC_PacketHandler {

	@Override
	public void handleIncomingPacket(World world, Object[] o) {
		int x = (Integer)o[0];
		int y = (Integer)o[1];
		int z = (Integer)o[2];
		String mob = (String)o[3];
		TileEntityMobSpawner tems = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
		System.out.println("PCco_MobSpawnerSetter.handleIncomingPacket mob" + mob);
		tems.setMobID(mob);
		try {
			ModLoader.setPrivateValue(TileEntityMobSpawner.class, tems, 8, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tems.getMobEntity();
	}

}
