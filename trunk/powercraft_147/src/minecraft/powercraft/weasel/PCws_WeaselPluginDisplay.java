package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.WeaselFunctionManager;
import powercraft.weasel.exception.WeaselRuntimeException;

public class PCws_WeaselPluginDisplay extends PCws_WeaselPlugin {

	private int bgcolor = 0x000000;
	private int fgcolor = 0xffffff;
	private int align = 0;
	private String text = "";
	
	@Override
	public WeaselFunctionManager makePluginProvider() {
		WeaselFunctionManager fp = new WeaselFunctionManager();
		fp.registerMethod("restart", "restartDevice", this);
		fp.registerMethod("reset", "restartDevice", this);
		fp.registerMethod("clear", this);
		fp.registerMethod("cls", "clear", this);
		fp.registerMethod("matrix", this);
		fp.registerMethod("grain", "matrix", this);
		fp.registerMethod("print", this);
		fp.registerMethod("add", this);
		fp.registerMethod("append", "add", this);
		
		fp.registerVariable("text", this);
		fp.registerVariable("txt", "text", this);
		fp.registerVariable("this", "text", this);
		fp.registerVariable("fgcolor", this);
		fp.registerVariable("color", "fgcolor", this);
		fp.registerVariable("fg", "fgcolor", this);
		fp.registerVariable("bgcolor", this);
		fp.registerVariable("background", "bgcolor", this);
		fp.registerVariable("bg", "bgcolor", this);
		fp.registerVariable("align", this);
		return fp;
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
		PC_GresRegistry.openGres("WeaselOnlyNet", player, getTE());
	}

	@Override
	public void restart() {
		bgcolor = 0x000000;
		fgcolor = 0xffffff;
		align = 0;
		text = "";
	}

	public void clear(){
		text = "";
		setData("text", text);
	}
	
	public void matrix(){
		text = "§knnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
		text += "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";
		bgcolor = 0x000000;
		fgcolor = 0x00ff00;
		setData("text", text);
		setData("bgcolor", bgcolor);
		setData("fgcolor", fgcolor);
	}
	
	public void print(String text){
		this.text += text + "\n";
		setData("text", text);
	}
	
	public void add(String text){
		this.text += text;
		setData("text", text);
	}
	
	public void text(String text){
		this.text = text;
		setData("text", text);
	}
	
	public String text(){
		return text;
	}
	
	public void fgcolor(int color){
		fgcolor = color;
		setData("fgcolor", fgcolor);
	}
	
	public void fgcolor(String color){
		Integer clr = PC_Color.getHexColorForName(Calc.toString(color));
		if (clr == null) {
			throw new WeaselRuntimeException("Display: " + color + " is not a valid color.");
		} else {
			fgcolor = clr;
		}
		setData("fgcolor", fgcolor);
	}
	
	public int fgcolor(){
		return fgcolor;
	}
	
	public void bgcolor(int color){
		bgcolor = color;
		setData("bgcolor", bgcolor);
	}
	
	public void bgcolor(String color){
		Integer clr = PC_Color.getHexColorForName(Calc.toString(color));
		if (clr == null) {
			throw new WeaselRuntimeException("Display: " + color + " is not a valid color.");
		} else {
			bgcolor = clr;
		}
		setData("bgcolor", bgcolor);
	}
	
	public int bgcolor(){
		return bgcolor;
	}
	
	public void align(Object object){
		if (object instanceof Number) {
			
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
			} else if (al.equalsIgnoreCase("C") || al.equalsIgnoreCase("M") || al.equalsIgnoreCase("center") || al.equalsIgnoreCase("centre")
					|| al.equalsIgnoreCase("middle")) {
				align = 0;
			} else if (al.equalsIgnoreCase("R") || al.equalsIgnoreCase("right")) {
				align = 1;
			} else {
				throw new WeaselRuntimeException("Display: " + object + " is not a valid alignment.");
			}

		}
		setData("align", align);
	}
	
	public String align(){
		return align == -1 ? "L" : align == 0 ? "C" : "R";
	}
	
}
