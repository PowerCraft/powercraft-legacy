package net.minecraft.src;

public class PClo_GuiCallerRadio implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 9;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PClo_TileEntityRadio ter = PClo_BlockRadio.getTE(player.worldObj, var2, var3, var4);
		int rtype = ter.isTransmitter() ? PClo_GuiRadio.TRANSMITTER : PClo_GuiRadio.RECEIVER;
		String channel = ter.getChannel();
		return new PClo_GuiRadio(player.dimension, ter.getCoord(), channel, rtype);
	}

}
