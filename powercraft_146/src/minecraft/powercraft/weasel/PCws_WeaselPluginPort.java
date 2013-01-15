package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.PC_Utils.Gres;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import net.minecraft.entity.player.EntityPlayer;

public class PCws_WeaselPluginPort extends PCws_WeaselPlugin {

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

	@Override
	protected List<String> getProvidedPluginFunctionNames() {
		return new ArrayList<String>();
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine,
			String functionName, WeaselObject[] args) {
		throw new WeaselRuntimeException("Invalid call of function " + functionName);
	}

	@Override
	protected List<String> getProvidedPluginVariableNames() {
		List<String> l = new ArrayList<String>();
		l.add("b");
		l.add("back");
		l.add("l");
		l.add("left");
		l.add("r");
		l.add("right");
		l.add("f");
		l.add("front");
		l.add("u");
		l.add("up");
		l.add("top");
		l.add("d");
		l.add("down");
		l.add("bottom");
		return l;
	}

	@Override
	protected void setPluginVariable(String name, Object value) {
		setOutport(portToNum(name), Calc.toBoolean(value));
	}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		return new WeaselBoolean(getInport(portToNum(name)));
	}

}
