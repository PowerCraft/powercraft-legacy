package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginDisplay extends PCws_WeaselPlugin {

	private int bgcolor = 0x000000;
	private int fgcolor = 0xffffff;
	private int align = 0;
	private String text = "";
	
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
	protected List<String> getProvidedPluginFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		list.add("cls");
		list.add("clear");
		list.add("matrix");
		list.add("grain");
		list.add("print");
		list.add("add");
		list.add("append");
		return list;
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		if (functionName.equals("cls") || functionName.equals("clear")){
			text = "";
			setData("text", text);
		}else if (functionName.equals("matrix") || functionName.equals("grain")) {
			text = "§knnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
			text += "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
			bgcolor = 0x000000;
			fgcolor = 0x00ff00;
			setData("text", text);
			setData("bgcolor", bgcolor);
			setData("fgcolor", fgcolor);
		}else if (functionName.equals("print")){
			text += Calc.toString(args[0]) + "\n";
			setData("text", text);
		}else if (functionName.equals("add") || functionName.equals("append")){
			text += Calc.toString(args[0]);
			setData("text", text);
		}else{
			throw new WeaselRuntimeException("Invalid call of function " + functionName);
		}
		needsSave();
		return new WeaselNull();
	}

	@Override
	protected List<String> getProvidedPluginVariableNames() {
		List<String> list = new ArrayList<String>();
		list.add("this");
		list.add("text");
		list.add("txt");
		list.add("color");
		list.add("fg");
		list.add("bgcolor");
		list.add("background");
		list.add("bg");
		list.add("align");
		return list;
	}

	@Override
	protected void setPluginVariable(String name, Object value) {
		if(name.equals("this")||name.equals("text")||name.equals("txt")){
			text = Calc.toString(value);
			setData("text", text);
		}else if(name.equals("color")||name.equals("fg")){
			if (value instanceof WeaselInteger || value instanceof Number) {
				fgcolor = Calc.toInteger(value);
			} else {
				Integer clr = PC_Color.getHexColorForName(Calc.toString(value));
				if (clr == null) {
					throw new WeaselRuntimeException("Display: " + value + " is not a valid color.");
				} else {
					fgcolor = clr;
				}
			}
			setData("fgcolor", fgcolor);
		}else if(name.equals("bgcolor")||name.equals("background")||name.equals("bg")){
			if (value instanceof WeaselInteger || value instanceof Number) {
				bgcolor = Calc.toInteger(value);
			} else {
				Integer clr = PC_Color.getHexColorForName(Calc.toString(value));
				if (clr == null) {
					throw new WeaselRuntimeException("Display: " + value + " is not a valid bgcolor.");
				} else {
					bgcolor = clr;
				}
			}
			setData("bgcolor", bgcolor);
		}else if(name.equals("align")){
			if (value instanceof WeaselInteger || value instanceof Number) {

				int al = Calc.toInteger(value);
				if (al < 0) {
					align = -1;
				} else if (al == 0) {
					align = 0;
				} else if (al > 0) {
					align = 1;
				}

			} else {

				String al = Calc.toString(value);
				if (al.equalsIgnoreCase("L") || al.equalsIgnoreCase("left")) {
					align = -1;
				} else if (al.equalsIgnoreCase("C") || al.equalsIgnoreCase("M") || al.equalsIgnoreCase("center") || al.equalsIgnoreCase("centre")
						|| al.equalsIgnoreCase("middle")) {
					align = 0;
				} else if (al.equalsIgnoreCase("R") || al.equalsIgnoreCase("right")) {
					align = 1;
				} else {
					throw new WeaselRuntimeException("Display: " + value + " is not a valid alignment.");
				}
			}
			setData("align", align);
		}
		needsSave();
	}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		if(name.equals("this")||name.equals("text")||name.equals("txt")) {
			return new WeaselString(text);
		}else if(name.equals("color")||name.equals("fg")){
			return new WeaselInteger(fgcolor);
		}else if(name.equals("bgcolor")||name.equals("background")||name.equals("bg")){
			return new WeaselInteger(fgcolor);
		}else if(name.equals("align")){
			return new WeaselString(align == -1 ? "L" : align == 0 ? "C" : "R");
		}
		return new WeaselNull();
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
