package net.minecraft.src;

public class PCco_GuiCallerSpawnerEditor implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 3;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		TileEntity te = (new PC_CoordI(var2, var3, var4)).getTileEntity(player.worldObj);
		if(te instanceof TileEntityMobSpawner)
			return new PCco_GuiSpawnerEditor(player, (TileEntityMobSpawner)te);
		return null;
	}

}
