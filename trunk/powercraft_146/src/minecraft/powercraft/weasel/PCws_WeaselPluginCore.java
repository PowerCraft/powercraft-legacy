package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.PC_Struct2;
import weasel.WeaselEngine;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin {
	
	/** The Weasel Engine */
	private WeaselEngine weasel = new WeaselEngine(this);
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	
	public PCws_WeaselPluginCore(){
		connectToNetowrk(new PCws_WeaselNetwork());
	}
	
	@Override
	public boolean doesProvideFunctionOnEngine(String functionName) {
		return weasel.doesProvideFunction(functionName);
	}

	@Override
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {
		int state = weasel.callFunctionExternal(functionName, (Object[])args);
		if(state == -1 || state == 1) return;	
		externalCallsWaiting.add(new PC_Struct2<String, Object[]>(functionName, args));
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void syncWithClient() {
		// TODO Auto-generated method stub
		
	}

}
