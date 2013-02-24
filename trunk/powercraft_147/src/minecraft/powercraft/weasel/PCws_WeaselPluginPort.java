package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.registry.PC_GresRegistry;
import weasel.WeaselFunctionManager;

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
		setOutport(3, state);
	}
	
	public boolean front(){
		return getInport(3);
	}
	
	public void back(boolean state){
		setOutport(0, state);
	}
	
	public boolean back(){
		return getInport(0);
	}
	
	public void left(boolean state){
		setOutport(1, state);
	}
	
	public boolean left(){
		return getInport(1);
	}
	
	public void right(boolean state){
		setOutport(2, state);
	}
	
	public boolean right(){
		return getInport(2);
	}
	
	public void top(boolean state){
		setOutport(4, state);
	}
	
	public boolean top(){
		return getInport(4);
	}
	
	public void bottom(boolean state){
		setOutport(5, state);
	}
	
	public boolean bottom(){
		return getInport(5);
	}
	
}
