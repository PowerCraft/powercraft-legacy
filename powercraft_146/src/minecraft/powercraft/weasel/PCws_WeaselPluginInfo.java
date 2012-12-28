package powercraft.weasel;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.block.Block;

import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfo {

	private Class<? extends PCws_WeaselPlugin> c;
	private String defaultName;
	
	public PCws_WeaselPluginInfo(Class<? extends PCws_WeaselPlugin> c, String defaultName){
		this.c = c;
		this.defaultName = defaultName;
	}
	
	public PCws_WeaselPlugin createPlugin(){
		try {
			return ValueWriting.createClass(c, new Class[0], new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public Class<? extends PCws_WeaselPlugin> getPluginClass() {
		return c;
	}
	
	public String getDefaultName(){
		return defaultName;
	}

	public String getKey() {
		return c.getSimpleName();
	}

	public void renderInventoryBlock(Block block, Object renderer) {
		
	}
	
}
