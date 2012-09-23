package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;


/**
 * @author MightyPork
 */
public class PCnt_WeaselPluginPort_UNUSED extends PCnt_WeaselPlugin_UNUSED {

	private boolean isChanged;

	/**
	 * @param tew tile entity weasel
	 */
	public PCnt_WeaselPluginPort_UNUSED(PCnt_TileEntityWeasel_UNUSED tew) {
		super(tew);
	}

	/**
	 * Helper for renderer, checks if device should be rendered with a red mark
	 * on top or not.
	 * 
	 * @return is active
	 */
	public boolean renderAsActive() {
		boolean flag = false;
		flag |= getOutport("F") == true;
		flag |= getOutport("L") == true;
		flag |= getOutport("R") == true;
		flag |= getOutport("B") == true;
		flag |= getOutport("U") == true;
		flag |= getOutport("D") == true;
		return flag;
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.PORT;
	}

	@Override
	public boolean onClick(EntityPlayer player) {
		//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselPort(this));
		return true;
	}

	@Override
	public boolean updateTick() {
		if (isChanged) {
			isChanged = false;
			return true;
		}
		return false;
	}

	@Override
	public void onRedstoneSignalChanged() {
		isChanged = true;
		if (getNetwork() != null)
			if (getNetwork().getMember("CORE") != null) ((PCnt_WeaselPlugin_UNUSED) getNetwork().getMember("CORE")).callFunctionOnEngine("update");
	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	/** rgb color shown on white lights. */
	public int rgbcolor = 0xffffff;

	@Override
	protected PCnt_WeaselPlugin_UNUSED readPluginFromNBT(NBTTagCompound tag) {
		rgbcolor = tag.getInteger("rgb");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setInteger("rgb", rgbcolor);
		return tag;
	}

	@Override
	public void onDeviceDestroyed() {}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		if (functionName.equals(getName() + ".empty")) {
			return this.chestEmptyTest(args);
		} else if (functionName.equals(getName() + ".full")) {
			return this.chestFullTest(args);
		} else if (functionName.equals(getName() + ".reset") || functionName.equals(getName() + ".restart")) {
			restartDevice();
		} else if (functionName.equals(getName() + ".on")) {
			isChanged = true;
			if (args.length == 0) {
				setOutport("F", true);
			} else {
				for (WeaselObject o : args) {
					setOutport((String) o.get(), true);
				}
			}
		} else if (functionName.equals(getName() + ".off")) {
			isChanged = true;
			if (args.length == 0) {
				setOutport("F", false);
			} else {
				for (WeaselObject o : args) {
					setOutport((String) o.get(), false);
				}
			}
		} else if (functionName.equals(getName() + ".switch") || functionName.equals(getName() + ".toggle")) {
			isChanged = true;
			if (args.length == 0) {
				setOutport("F", !getOutport("F"));
			} else {
				for (WeaselObject o : args) {
					setOutport((String) o.get(), !getOutport((String) o.get()));
				}
			}
		}
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if (name.equals(getName() + ".rgb") || name.equals(getName() + ".color")) return new WeaselInteger(rgbcolor);
		if (name.equals(getName())) return new WeaselBoolean(getInport("F"));
		if (name.startsWith(getName() + ".") && name.length() == getName().length() + 2) {
			String port = name.substring(name.length() - 1);
			return new WeaselBoolean(getInport(port.toUpperCase()));
		}
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
		isChanged = true;
		if (name.equals(getName() + ".color") || name.equals(getName() + ".rgb")) {
			if (object instanceof WeaselInteger || object instanceof Number) {
				rgbcolor = Calc.toInteger(object);
			} else {
				Integer clr = PC_Color.getHexColorForName(Calc.toString(object));
				if (clr == null) {
					throw new WeaselRuntimeException("port: " + object + " is not a valid color.");
				} else {
					rgbcolor = clr;
				}
			}
		} else if (name.equals(getName())) {
			boolean state = Calc.toBoolean(object);
			setOutport("F", state);
			setOutport("B", state);
			setOutport("L", state);
			setOutport("R", state);
			setOutport("U", state);
			setOutport("D", state);

		} else if (name.startsWith(getName() + ".") && name.length() == getName().length() + 2) {
			String port = name.substring(name.length() - 1);
			setOutport(port.toUpperCase(), Calc.toBoolean(object));
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);

		list.add(getName() + ".empty");
		list.add(getName() + ".full");
		list.add(getName() + ".reset");
		list.add(getName() + ".restart");
		list.add(getName() + ".off");
		list.add(getName() + ".on");
		list.add(getName() + ".switch");
		list.add(getName() + ".toggle");

		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(7);
		list.add(getName());
		list.add(getName() + ".F");
		list.add(getName() + ".L");
		list.add(getName() + ".R");
		list.add(getName() + ".B");
		list.add(getName() + ".U");
		list.add(getName() + ".D");
		list.add(getName() + ".f");
		list.add(getName() + ".l");
		list.add(getName() + ".r");
		list.add(getName() + ".b");
		list.add(getName() + ".u");
		list.add(getName() + ".d");
		list.add(getName() + ".rgb");
		list.add(getName() + ".color");
		return list;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public void callFunctionOnEngine(String function, Object... args) {}

	@Override
	public void restartDevice() {
		resetOutport();
		rgbcolor = 0xffffff;
	}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {}

	@Override
	public void onRandomDisplayTick(Random random) {}

	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 0.25F, 1 };
	}

}
