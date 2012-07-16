package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public boolean onClick(EntityPlayer player) {
		PC_Utils.openGres(player, new PClo_GuiWeaselDisplay(this));
		return true;
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		if (functionName.equals(getName()+".reset") || functionName.equals(getName()+".restart")) restartDevice();
		if (functionName.equals(getName()+".cls") || functionName.equals(getName()+".clear")) text="";
		if (functionName.equals(getName()+".matrix") || functionName.equals(getName()+".grain")) {
			text = "§knnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
			text += "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
			
			
			bgcolor=0x000000;
			color=0x00ff00;			
		}
		if (functionName.equals(getName()+".print")) text += Calc.toString(args[0])+"\n";
		if (functionName.equals(getName()+".add") || functionName.equals(getName()+".append")) text += Calc.toString(args[0]);
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

		if (name.equals(getName()) || name.equals(getName() + ".text") || name.equals(getName() + ".txt")) {
			text = Calc.toString(object);
			return;
		}

		if (name.equals(getName() + ".color")||name.equals(getName() + ".fg")) {
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
		list.add(getName() + ".reset");
		list.add(getName() + ".restart");
		list.add(getName() + ".cls");
		list.add(getName() + ".clear");
		list.add(getName() + ".matrix");
		list.add(getName() + ".grain");
		list.add(getName() + ".print");
		list.add(getName() + ".add");
		list.add(getName() + ".append");
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		list.add(getName());
		list.add(getName() + ".text");
		list.add(getName() + ".txt");
		list.add(getName() + ".color");
		list.add(getName() + ".fg");
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
	
	@Override
	public void onBlockPlaced(EntityLiving entityliving) {
		rotation = MathHelper.floor_double(((entityliving.rotationYaw + 180F) * 16F) / 360F + 0.5D) & 0xf;
	}


	@Override
	public void onRandomDisplayTick(Random random) {}
	
	
	@Override
	public float[] getBounds() {
		return new float[] {0,0,0,1,1,1};
	}

}
