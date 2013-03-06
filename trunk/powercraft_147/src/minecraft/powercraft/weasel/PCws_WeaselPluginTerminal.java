package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.weasel.engine.WeaselFunctionManager;
import powercraft.weasel.obj.WeaselString;

public class PCws_WeaselPluginTerminal extends PCws_WeaselPlugin {

	/** Displayed text. "\n" is a newline. */
	public String text = "";
	/**  */
	private List<String> userInput = new ArrayList<String>();
	
	private long lastTime = -1;
	
	@Override
	public WeaselFunctionManager makePluginProvider() {
		WeaselFunctionManager fp = new WeaselFunctionManager();
		fp.registerMethod("restart", "restartDevice", this);
		fp.registerMethod("reset", "restartDevice", this);
		fp.registerMethod("clear", this);
		fp.registerMethod("cls", "clear", this);
		fp.registerMethod("print", this);
		fp.registerMethod("out", "print", this);
		fp.registerMethod("hasInput", this);
		fp.registerMethod("getInput", this);
		fp.registerMethod("in", "getInput", this);
		fp.registerMethod("this", "_this", this);
		fp.registerVariable("text", this);
		fp.registerVariable("txt", "text", this);
		return fp;
	}
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		userInput.clear();
		text = tag.getString("text");
		int num = tag.getInteger("count");
		for(int i=0; i<num; i++){
			userInput.add(tag.getString("value["+i+"]"));
		}
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);
		int num = userInput.size();
		tag.setInteger("count", num);
		for(int i=0; i<num; i++){
			tag.setString("value["+i+"]", userInput.get(i));
		}
		return tag;
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
		if(System.currentTimeMillis() - lastTime > 100){
			if(getTE()!=null){
				getTE().call("play", null);
				lastTime = System.currentTimeMillis();
			}
		}
		
		
		this.text += text.replace("\\n", "\n");
		if (countIn(this.text, '\n') > 60) {
			while (countIn(this.text, '\n') > 60) {
				this.text = this.text.substring(this.text.indexOf('\n') + 1);
			}
		}
		setData("text", this.text);
		needsSave();
	}

	@Override
	public void update() {}

	@Override
	public void getClientMsg(String msg, Object obj) {
		if(msg.equalsIgnoreCase("input")){
			String input = (String)obj;
			input = input.trim();
			if (input.length() > 0) {
				userInput.add(input);
				addText("> " + input + "\n");
				if(getNetwork()!=null){
					if(!getNetwork().callFunctionOnEngine("termIn."+getName(), new WeaselString(input))){
						getNetwork().callFunctionOnEngine("termIn", new WeaselString(getName()), new WeaselString(input));
					}
				}
			}
			if (userInput.size() > 16) {
				userInput.remove(0);
			}
			needsSave();
		}else{
			super.getClientMsg(msg, obj);
		}
	}
	
	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {
		tileEntityWeasel.setData("text", text);
	}

	@Override
	public void refreshInport(){}
	
	@Override
	protected void openPluginGui(EntityPlayer player) {
		PC_GresRegistry.openGres("WeaselTerminal", player, getTE());
	}

	@Override
	public void restart() {
		text = "";
		userInput.clear();
	}

	public void clear(){
		text = "";
		userInput.clear();
		setData("text", text);
	}
	
	public void print(String text){
		addText(text + "\n");
	}
	
	public boolean hasInput(){
		return userInput.size() > 0;
	}
	
	public String getInput(){
		if (userInput.size() == 0) return "";
		String s = userInput.get(0);
		userInput.remove(0);
		return s;
	}
	
	public String _this(){
		return getInput();
	}
	
	public void _this(String text){
		print(text);
	}
	
	public void text(String text){
		text = "";
		addText(text);
	}
	
	public String text(){
		return text;
	}
	
}
