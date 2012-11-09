package powercraft.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class PC_ClientUtils extends PC_Utils {
	
	private HashMap<PC_Module, HashMap<String, HashMap<String, String[]>>> moduleTranslation = new HashMap<PC_Module, HashMap<String, HashMap<String, String[]>>>();
	
	public static Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	@Override
	protected void iRegisterTextureFiles(String[] textureFiles) {
		if(textureFiles==null)
			return;
		for(String textureFile:textureFiles){
			MinecraftForgeClient.preloadTexture(textureFile);
		}
	}
	
	@Override
	protected boolean client(){return true;}
	
	@Override
	protected void iRegisterLanguage(PC_Module module, String lang, String[] translations) {
		HashMap<String, HashMap<String, String[]>> langs;
		if(moduleTranslation.containsKey(module))
			langs = moduleTranslation.get(module);
		else
			moduleTranslation.put(module, langs = new HashMap<String, HashMap<String, String[]>>());
		HashMap<String, String[]> translation;
		if(langs.containsKey(lang))
			translation = langs.get(lang);
		else
			langs.put(lang, translation = new HashMap<String, String[]>());
		for(int i=0; i<translations.length; i+=2){
			if(translations[i].startsWith("tile.") || translations[i].startsWith("item.")){
				if(!translations[i].endsWith(".name")){
					translations[i] += ".name";
				}
			}
			if(!translation.containsKey(translations[i])){
				translation.put(translations[i], new String[]{translations[i+1]});
				LanguageRegistry.instance().addStringLocalization(translations[i], lang, translations[i+1]);
			}
		}
	}
	
	@Override
	protected void iLoadLanguage(PC_Module module){
		final PC_Module m = module;
		File folder = new File(Minecraft.getMinecraftDir(), PC_Module.getPowerCraftFile() + "lang/");

		String[] files = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.matches("[a-z]{2,3}_[A-Z]{2,3}-" + m.getName() + "[.]lang");
			}

		});
		
		if (files == null) {
			PC_Logger.severe("Received NULL instead of list of translations.");
			return;
		}
		
		for (String filename : files) {
			
			PC_Logger.finest("* loading names from file " + filename + "...");
			String language = filename.substring(0, filename.indexOf('-'));
			
			try {
				
				LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(folder.getCanonicalPath()+ "/" + filename), "utf-8"));
				
				String line = lnr.readLine();
				
				while(line!=null){
					line = line.trim();
					if(!(line.startsWith("#")||line.equals(""))){
						int peq = line.indexOf('=');
						if(peq>0){
							String key = line.substring(0, peq).trim();
							String value = line.substring(peq+1).trim();
							registerLanguageForLang(module, language, key, value);
						}
					}
					line = lnr.readLine();
				}
				
				lnr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		PC_Logger.finer("Translations loaded.");
	}
	
	@Override
	protected void iSaveLanguage(PC_Module module){
		if(!moduleTranslation.containsKey(module))
			return;
		Set<Entry<String, HashMap<String, String[]>>> langs = moduleTranslation.get(module).entrySet();
		for(Entry<String, HashMap<String, String[]>> langEntry:langs){
			
			try {
				File f = new File(Minecraft.getMinecraftDir(), PC_Module.getPowerCraftFile()+ "lang/");
				if(!f.exists())
					f.mkdirs();
				f = new File(f, langEntry.getKey() + "-" +  module.getName() + ".lang");
				if(!f.exists())
					f.createNewFile();
				
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
				osw.write("# Translation File for \""+module.getName()+"\"\n");
				
				String[] keys = langEntry.getValue().keySet().toArray(new String[0]);
				Arrays.sort(keys);
				for(String key:keys){
					osw.write(key+" = "+langEntry.getValue().get(key)[0]+"\n");
				}
				
				osw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	@Override
	protected void iPlaySound(double x, double y, double z, String sound, float soundVolume, float pitch){
		World world = mc().theWorld;
		if(PC_Utils.isSoundEnabled() && world!=null){
			world.playSound(x, y, z, sound, soundVolume, pitch);
		}
	}
	
	@Override
	protected void iOpenGres(String name, EntityPlayer player, Object[]o){
		if(player!=null&&!player.worldObj.isRemote){
			super.iOpenGres(name, player, o);
			return;
		}
		int guiID = 0;
		if(o!=null && o.length==1 && o[0] instanceof ObjectInputStream){
			ObjectInputStream input = (ObjectInputStream)o[0];
			try {
				name = (String)input.readObject();
				 guiID = input.readInt();
			        o = (Object[])input.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

        Class c = guis.get(name);

		try {
			if(PC_GresBaseWithInventory.class.isAssignableFrom(c)){
				mc().displayGuiScreen(new PC_GresGui((PC_GresBaseWithInventory)PC_Utils.createClass(c, new Class[]{EntityPlayer.class, Object[].class}, new Object[]{player, o})));
				player.craftingInventory.windowId = guiID;
			}else{
				mc().displayGuiScreen(new PC_GresGui((PC_IGresClient)PC_Utils.createClass(c, new Class[]{EntityPlayer.class, Object[].class}, new Object[]{player, o})));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player){
		return (EnumGameType)PC_Utils.getPrivateValue(PlayerControllerMP.class, mc().playerController, 10);
	}
	
	@Override
	protected void iChatMsg(String msg, boolean clear){
		if (clear) {
			 mc().ingameGUI.getChatGUI().func_73761_a();
		}
		mc().thePlayer.addChatMessage(msg);
	}
	
	@Override
	protected File iGetMCDirectory(){
		return Minecraft.getMinecraftDir();
	}
	
	@Override
	protected int iAddArmor(String name) {
		return RenderingRegistry.addNewArmourRendererPrefix(name);
	}
	
	@Override
	protected boolean iIsEntityFX(Entity entity) {
		return entity instanceof EntityFX;
	}
	
}
