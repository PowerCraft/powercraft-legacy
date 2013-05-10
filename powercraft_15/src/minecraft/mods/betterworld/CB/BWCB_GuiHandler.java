package mods.betterworld.CB;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class BWCB_GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int var1, EntityPlayer var2, World var3,
			int var4, int var5, int var6) {
		System.out.println("getServerGuiElement");
		if (!var3.blockExists(var4, var5, var6)) {
			return null;
		} else {
			switch (var1) {
			case 150:
				BWCB_TileEntityBlockMachineBrick var7 = (BWCB_TileEntityBlockMachineBrick) var3
						.getBlockTileEntity(var4, var5, var6);
				return new BWCB_ContainerBrickMachine(var2.inventory, var7);
			default:
				return null;
			}
		}
	}

	@Override
	public Object getClientGuiElement(int var1, EntityPlayer var2, World var3,
			int var4, int var5, int var6) {
		System.out.println("getClientGuiElement");
		if (!var3.blockExists(var4, var5, var6)) {
			return null;
		} else {
			switch (var1) {
			case 150:
				BWCB_TileEntityBlockMachineBrick var7 = (BWCB_TileEntityBlockMachineBrick) var3
						.getBlockTileEntity(var4, var5, var6);
				return new BWCB_GuiBlockMachineBrick(var2.inventory, var7);
			default:
				return null;
			}
		}
	}
}