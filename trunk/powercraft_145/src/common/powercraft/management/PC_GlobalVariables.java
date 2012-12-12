package powercraft.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PC_GlobalVariables {

	public static List<String> splashes = new ArrayList<String>();
	public static boolean hackSplashes = true;
	public static boolean showUpdateWindow = false;
	public static String useUserName = "";
	
	public static PC_Configuration config;
	
	public static void loadConfig(){
		File f = new File(PC_Utils.getMCDirectory(), "config/PowerCraft.cfg");
		if(f.exists()){
			try {
				InputStream is = new FileInputStream(f);
				config = PC_Configuration.load(is);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File "+f);
			}
		}
		if(config==null){
			config = new PC_Configuration();
		}
		
		hackSplashes = config.getBoolean("hacks.splash", true);
		useUserName = config.getString("hacks.userName", "");
		
	}
	
	public static void saveConfig(){
		File f = new File(PC_Utils.getMCDirectory(), "config/PowerCraft.cfg");
		if(config!=null){
			try {
				OutputStream os = new FileOutputStream(f);
				config.save(os);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File "+f);
			}
		}
	}
	
}