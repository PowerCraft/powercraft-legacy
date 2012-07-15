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
 * 
 * 
 * @author MightyPork
 *
 */
public class PClo_WeaselPluginDisplay extends PClo_WeaselPlugin {

	/** Displayed text. "\n" is a newline. */
	public String text = "";
	/** Hex color for the text */
	public int color = 0x000000;
	/** Text align (-1,0,1) */
	public int align = 0;

	/** Rotation, like sign, 0-15 */
	public int rotation;	

	private static HashMap<String, Integer> namedColord = new HashMap<String, Integer>();

	static {
		namedColord.put("white", 0xffffff);
		namedColord.put("silver", 0xc0c0c0);
		namedColord.put("gray", 0x808080);
		namedColord.put("black", 0x000000);
		namedColord.put("red", 0xff0000);
		namedColord.put("maroon", 0x800000);
		namedColord.put("yellow", 0xffff00);
		namedColord.put("olive", 0x808000);
		namedColord.put("lime", 0x00ff00);
		namedColord.put("green", 0x008000);
		namedColord.put("aqua", 0x00ffff);
		namedColord.put("teal", 0x008080);
		namedColord.put("blue", 0x0000ff);
		namedColord.put("navy", 0x000080);
		namedColord.put("fuchsia", 0xff00ff);
		namedColord.put("purple", 0x800080);
		namedColord.put("brick", 0xB22222);
		namedColord.put("darkred", 0x8B0000);
		namedColord.put("salmon", 0xFA8072);
		namedColord.put("pink", 0xff1493);
		namedColord.put("orange", 0xff4500);
		namedColord.put("gold", 0xffd700);
		namedColord.put("magenta", 0xff00ff);
		namedColord.put("violet", 0x9400d3);
		namedColord.put("indigo", 0x483D8B);
		namedColord.put("limegreen", 0x32cd32);
		namedColord.put("darkgreen", 0x006400);
		namedColord.put("cyan", 0x00ffff);
		namedColord.put("steel", 0x4682b4);
		namedColord.put("darkblue", 0x00008b);
		namedColord.put("brown", 0x8b4513);
		namedColord.put("lightgray", 0xd3d3d3);
		namedColord.put("darkgray", 0xa9a9a9);
	}

	/**
	 * Get a color index (hex) for given color name.
	 * 
	 * @param name
	 * @return hex color, or null.
	 */
	private Integer getColorForName(String name) {
		for (String key : namedColord.keySet()) {
			if (key.equalsIgnoreCase(name)) {
				return namedColord.get(key);
			}
		}
		return null;
	}

	/**
	 * A display device.
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
		if (name.equals(getName() + ".color")) return new WeaselInteger(color);
		if (name.equals(getName() + ".align")) return new WeaselString(align == -1 ? "L" : align == 0 ? "C" : "R");
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {

		if (name.equals(getName())) {
			text = Calc.toString(object);
			return;
		}

		if (name.equals(getName() + ".color")) {
			if (object instanceof WeaselInteger || object instanceof Number) {
				color = Calc.toInteger(object);
			} else {
				Integer clr = getColorForName(Calc.toString(object));				
				if (clr == null) {
					throw new WeaselRuntimeException("Display: " + object + " is not a valid color.");
				}else {
					color = clr;
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
		list.add(getName() + ".color");
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
		align = tag.getInteger("align");
		rotation = tag.getInteger("rotation");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);
		tag.setInteger("color", color);
		tag.setInteger("align", align);
		tag.setInteger("rotation",rotation);
		return tag;
	}

	@Override
	public void restartDevice() {
		text = "";
		color = 0x000000;
		align = 0;
		notifyBlockChange();
	}

}
