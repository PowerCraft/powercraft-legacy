package powercraft.api.upgrade;

import java.util.ArrayList;
import java.util.List;

public class PC_Upgrade {
	
	private static final List<String> upgrades = new ArrayList<String>();
	
	private PC_Upgrade(){}
	
	public static int getUpgrade(String name){
		int upgrade = upgrades.indexOf(name);
		if(upgrade==-1){
			upgrade = upgrades.size();
			upgrades.add(name);
		}
		return upgrade+1;
	}
	
}
