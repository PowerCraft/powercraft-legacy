package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.weasel.engine.WeaselFunctionManager;

public class PCws_WeaselPluginPort extends PCws_WeaselPlugin {

	@Override
	public WeaselFunctionManager makePluginProvider() {
		WeaselFunctionManager fp = new WeaselFunctionManager();
		fp.registerMethod("restart", "restartDevice", this);
		fp.registerMethod("reset", "restartDevice", this);
		fp.registerVariable("front", this);
		fp.registerVariable("f", "front", this);
		fp.registerVariable("back", this);
		fp.registerVariable("b", "back", this);
		fp.registerVariable("left", this);
		fp.registerVariable("l", "left", this);
		fp.registerVariable("right", this);
		fp.registerVariable("r", "right", this);
		fp.registerVariable("top", this);
		fp.registerVariable("up", "top", this);
		fp.registerVariable("u", "top", this);
		fp.registerVariable("bottom", this);
		fp.registerVariable("down", "bottom", this);
		fp.registerVariable("d", "bottom", this);
		return fp;
	}
	
	@Override
	public void update() {}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {}
	
	@Override
	protected void openPluginGui(EntityPlayer player) {
		PC_GresRegistry.openGres("WeaselOnlyNet", player, getTE());
	}

	@Override
	public void restart() {}
	
	public void front(boolean state){
		front(state?15:0);
	}
	
	public void front(int state){
		setOutport(3, state);
	}
	
	public int front(){
		return getInport(3);
	}
	
	public void back(boolean state){
		back(state?15:0);
	}
	
	public void back(int state){
		setOutport(0, state);
	}
	
	public int back(){
		return getInport(0);
	}
	
	public void left(boolean state){
		left(state?15:0);
	}
	
	public void left(int state){
		setOutport(1, state);
	}
	
	public int left(){
		return getInport(1);
	}
	
	public void right(boolean state){
		right(state?15:0);
	}
	
	public void right(int state){
		setOutport(2, state);
	}
	
	public int right(){
		return getInport(2);
	}
	
	public void top(boolean state){
		top(state?15:0);
	}
	
	public void top(int state){
		setOutport(4, state);
	}
	
	public int top(){
		return getInport(4);
	}
	
	public void bottom(boolean state){
		bottom(state?15:0);
	}
	
	public void bottom(int state){
		setOutport(5, state);
	}
	
	public int bottom(){
		return getInport(5);
	}
	
}
