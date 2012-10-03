package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCnt_WeaselPluginTerminal extends PCnt_WeaselPlugin {

	/** Displayed text. "\n" is a newline. */
	public String text = "";

	/**  */
	private ArrayList<String> userInput = new ArrayList<String>();

	/** Flag that is changed and needs save */
	public boolean isChanged;

	private long lastTime = -1;
	
	/**
	 * A display device.
	 */
	public PCnt_WeaselPluginTerminal() {
		super();
	}
	
	/**
	 * A display device.
	 * 
	 * @param id
	 */
	public PCnt_WeaselPluginTerminal(int id) {
		super(id);
	}

	/**
	 * Add user input to the buffer - if not empty
	 * @param input user input
	 */
	public void addInput(String input) {
		if (input.trim().length() > 0) {
			userInput.add(input.trim());
		}
		if (userInput.size() > 16) {
			userInput.remove(0);
		}

		addText("> " + input.trim() + "\n");
	}


	@Override
	public boolean onClick(EntityPlayer player) {
		if (player.isSneaking()) {
			//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselTerminal(this));
		} else {
			//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselTerminalTerm(this));
		}

		return true;
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	private int countIn(String str, char c) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Add text to the terminal, if too long remove oldest.
	 * @param text
	 */
	public void addText(String text) {
		if(System.currentTimeMillis() - lastTime > 100)
		world().playSoundEffect(coord().x + 0.5F, coord().y + 0.5F, coord().z + 0.5F, "random.click", 0.05F, 3F);
		lastTime = System.currentTimeMillis();
		
		
		this.text += text.replace("\\n", "\n");
		if (countIn(this.text, '\n') > 60) {
			while (countIn(this.text, '\n') > 60) {
				this.text = this.text.substring(this.text.indexOf('\n') + 1);
			}
		}
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		isChanged = true;

		if (functionName.equals(getName())) {
			if (args.length == 0) {
				functionName = getName() + ".in";
			} else if (args.length == 1) {
				if(System.currentTimeMillis() - lastTime < 50) {
					engine.requestPause();
				}
				functionName = getName() + ".out";
			}
		}

		if (functionName.equals(getName() + ".reset") || functionName.equals(getName() + ".restart")) restartDevice();
		if (functionName.equals(getName() + ".cls") || functionName.equals(getName() + ".clear")) {
			text = "";
			userInput.clear();
		}

		if (functionName.equals(getName() + ".out") || functionName.equals(getName() + ".print")) {
			addText(Calc.toString(args[0]) + "\n");
		}
		if (functionName.equals(getName() + ".hasInput")) return new WeaselBoolean(userInput.size() > 0);
		if (functionName.equals(getName() + ".in") || functionName.equals(getName() + ".getInput")) {
			if (userInput.size() == 0) return new WeaselString("");
			WeaselObject o = new WeaselString(userInput.get(0));
			userInput.remove(0);
			return o;
		}
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if (name.equals(getName()+".text")||name.equals(getName()+".txt")) return new WeaselString(text);
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {

		if (name.equals(getName()) || name.equals(getName() + ".text") || name.equals(getName() + ".txt")) {
			text = "";
			addText(Calc.toString(object));
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
		list.add(getName() + ".print");
		list.add(getName() + ".in");
		list.add(getName() + ".out");
		list.add(getName() + ".getInput");
		list.add(getName() + ".hasInput");
		list.add(getName());
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		list.add(getName() + ".text");
		list.add(getName() + ".txt");
		return list;
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.TERMINAL;
	}

	@Override
	protected boolean updateTick() {
		if (isChanged) {
			isChanged = false;
			return true;
		}
		return false;
	}

	@Override
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		text = tag.getString("text");
		int count = tag.getInteger("Count");
		userInput.clear();
		for (int i = 0; i < count; i++) {
			userInput.add(tag.getString("in_" + i));
		}
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);

		tag.setInteger("Count", userInput.size());
		for (int i = 0; i < userInput.size(); i++) {
			tag.setString("in_" + i, userInput.get(i));
		}
		return tag;
	}

	@Override
	public void restartDevice() {
		text = "";
		userInput.clear();
	}

	@Override
	public float[] getBounds() {
		return new float[] { 0.0625F, 0, 0.0625F, 1 - 0.0625F, 1 - 3 * 0.0625F, 1 - 0.0625F };
	}

}
