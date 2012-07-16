package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * @author MightyPork
 */
public class PClo_WeaselPluginDisplay extends PClo_WeaselPlugin {

	/** Displayed text. "\n" is a newline. */
	public String text = "";
	/** Hex color for the text */
	public int color = 0x000000;
	/** Hex color for the background */
	public int bgcolor = 0x92c392;
	/** Text align (-1,0,1) */
	public int align = 0;

	/** Rotation, like sign, 0-15 */
	public int rotation;



	/**
	 * A display device.
	 * 
	 * @param tew
	 */
	public PClo_WeaselPluginDisplay(PClo_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return false;
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if (name.equals(getName())) return new WeaselString(text);
		if (name.equals(getName() + ".text")) return new WeaselString(text);
		if (name.equals(getName() + ".color")) return new WeaselInteger(color);
		if (name.equals(getName() + ".bgcolor")) return new WeaselInteger(bgcolor);
		if (name.equals(getName() + ".bg")) return new WeaselInteger(bgcolor);
		if (name.equals(getName() + ".background")) return new WeaselInteger(bgcolor);
		if (name.equals(getName() + ".align")) return new WeaselString(align == -1 ? "L" : align == 0 ? "C" : "R");
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {

		if (name.equals(getName()) || name.equals(getName() + ".text")) {
			text = Calc.toString(object);
			return;
		}

		if (name.equals(getName() + ".color")) {
			if (object instanceof WeaselInteger || object instanceof Number) {
				color = Calc.toInteger(object);
			} else {
				Integer clr = PC_Color.getHexColorForName(Calc.toString(object));
				if (clr == null) {
					throw new WeaselRuntimeException("Display: " + object + " is not a valid color.");
				} else {
					color = clr;
				}
			}
			return;
		}

		if (name.equals(getName() + ".background") || name.equals(getName() + ".bgcolor") || name.equals(getName() + ".bg")) {
			if (object instanceof WeaselInteger || object instanceof Number) {
				bgcolor = Calc.toInteger(object);
			} else {
				Integer clr = PC_Color.getHexColorForName(Calc.toString(object));
				if (clr == null) {
					throw new WeaselRuntimeException("Display: " + object + " is not a valid bgcolor.");
				} else {
					bgcolor = clr;
				}
			}
			return;
		}

		if (name.equals(getName() + ".align")) {

			if (object instanceof WeaselInteger || object instanceof Number) {

				int al = Calc.toInteger(object);
				if (al < 0) {
					align = -1;
				} else if (al == 0) {
					align = 0;
				} else if (al > 0) {
					align = 1;
				}

			} else {

				String al = Calc.toString(object);
				if (al.equalsIgnoreCase("L") || al.equalsIgnoreCase("left")) {
					align = -1;
				} else if (al.equalsIgnoreCase("C") || al.equalsIgnoreCase("M") || al.equalsIgnoreCase("center") || al.equalsIgnoreCase("centre") || al.equalsIgnoreCase("middle")) {
					align = 0;
				} else if (al.equalsIgnoreCase("R") || al.equalsIgnoreCase("right")) {
					align = 1;
				} else {
					throw new WeaselRuntimeException("Display: " + object + " is not a valid alignment.");
				}

			}
			return;
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		list.add(getName());
		list.add(getName() + ".text");
		list.add(getName() + ".color");
		list.add(getName() + ".bgcolor");
		list.add(getName() + ".background");
		list.add(getName() + ".bg");
		list.add(getName() + ".align");
		return list;
	}

	@Override
	public int getType() {
		return PClo_WeaselType.DISPLAY;
	}

	@Override
	protected void updateTick() {}

	@Override
	public void onRedstoneSignalChanged() {}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	protected void onDeviceDestroyed() {}

	@Override
	public Object callFunctionExternalDelegated(String function, Object... args) {
		return null;
	}

	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		text = tag.getString("text");
		color = tag.getInteger("color");
		bgcolor = tag.getInteger("bgcolor");
		align = tag.getInteger("align");
		rotation = tag.getInteger("rotation");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);
		tag.setInteger("color", color);
		tag.setInteger("bgcolor", bgcolor);
		tag.setInteger("align", align);
		tag.setInteger("rotation", rotation);
		return tag;
	}

	@Override
	public void restartDevice() {
		text = "";
		color = 0x000000;
		bgcolor = 0x92c392;
		align = 0;
		notifyBlockChange();
	}

}
