package powercraft.api;


import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_IBlock;
import powercraft.api.blocks.PC_TileEntity;
import powercraft.api.gres.PC_Gres;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class PC_PacketHandlerClient extends PC_PacketHandler {

	@Override
	public void packetBlockData(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {

		int x = dataInputStream.readInt();
		int y = dataInputStream.readInt();
		int z = dataInputStream.readInt();
		Block block = PC_Utils.getBlock(world, x, y, z);
		if (block != null) ((PC_IBlock) block).loadFromNBTPacket(world, x, y, z, readNBTTagCompound(dataInputStream));
	}


	@Override
	protected void packetGuiOpen(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {

		int x = dataInputStream.readInt();
		int y = dataInputStream.readInt();
		if (x == 0 && y == -1) {
			String guiOpenHandlerName = dataInputStream.readUTF();
			int windowId = dataInputStream.readInt();
			PC_Gres.openClientGui(player, guiOpenHandlerName, windowId);
		} else if (x == 0 && y == -2) {
			Item item = Item.itemsList[dataInputStream.readInt()];
			int windowId = dataInputStream.readInt();
			PC_Gres.openClientGui(player, item, windowId);
		} else {
			int z = dataInputStream.readInt();
			int windowId = dataInputStream.readInt();
			PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
			PC_Gres.openClientGui(player, tileEntity, windowId);
		}
	}

}
