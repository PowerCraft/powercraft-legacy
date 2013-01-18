package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginDisplay extends PCws_WeaselPlugin {

	private int bgcolor = 0x000000;
	private int fgcolor = 0xffffff;
	private int align = 0;
	private String text = "";
	
	@Override
	public WeaselFunctionManager makePluginProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		text = tag.getString("text");
		align = tag.getInteger("align");
		fgcolor = tag.getInteger("fgcolor");
		bgcolor = tag.getInteger("bgcolor");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);
		tag.setInteger("align", align);
		tag.setInteger("fgcolor", fgcolor);
		tag.setInteger("bgcolor", bgcolor);
		return tag;
	}

	@Override
	public void update() {}

	@Override
	public void refreshInport(){}
	
	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {
		tileEntityWeasel.setData("bgcolor", bgcolor);
		tileEntityWeasel.setData("fgcolor", fgcolor);
		tileEntityWeasel.setData("align", align);
		tileEntityWeasel.setData("text", text);
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselOnlyNet", player, getPos().x, getPos().y, getPos().z);
	}

	@Override
	public void restart() {
		bgcolor = 0x000000;
		fgcolor = 0xffffff;
		align = 0;
		text = "";
	}

}
