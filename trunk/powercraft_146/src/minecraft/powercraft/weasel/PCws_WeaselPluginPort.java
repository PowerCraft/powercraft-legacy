package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import powercraft.management.PC_Utils.Gres;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import net.minecraft.entity.player.EntityPlayer;

public class PCws_WeaselPluginPort extends PCws_WeaselPlugin {

	@Override
	public WeaselFunctionManager makePluginProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void update() {}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {}
	
	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselOnlyNet", player, getPos().x, getPos().y, getPos().z);
	}

	@Override
	public void restart() {}
}
