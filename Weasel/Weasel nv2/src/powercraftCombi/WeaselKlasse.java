package powercraftCombi;

import java.util.Map;

public class WeaselKlasse {

	public void a(){
		
	}
	
	@WeaselNamedMethod("Class.name")
	public void b(){
		
	}
	
	@WeaselNamedMethod("Class.name")
	public @WeaselClassName("Vec")Map<String, Object> c(@WeaselClassName("Blub") Map<String, Object>in){
		return null;
	}
}
