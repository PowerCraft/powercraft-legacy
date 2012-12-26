package powercraft.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import net.minecraft.src.mod_PowerCraft;

public class PC_ClientUtils extends PC_Utils {
	
	private HashMap<PC_IModule, HashMap<String, PC_Property>> moduleTranslation = new HashMap<PC_IModule, HashMap<String, PC_Property>>();
	private HashMap<String, Class<? extends EntityFX>> entityFX = new HashMap<String, Class<? extends EntityFX>>();
	
	private PC_ClientUtils(){}
	
	public static boolean create()
    {
        if (instance == null)
        {
        	instance = new PC_ClientUtils();
            return true;
        }

        return false;
    }
	
	public static Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	@Override
	protected void iRegisterTextureFiles(String[] textureFiles) {
		if(textureFiles==null)
			return;
		for(String textureFile:textureFiles){
			ModLoader.getMinecraftInstance().renderEngine.getTexture(textureFile);
		}
	}
	
	@Override
	protected boolean client(){return true;}
	
	@Override
	protected void iRegisterLanguage(PC_IModule module, String lang, PC_Struct3<String, String, String[]>[] translations){
		HashMap<String, PC_Property> langs;
		if(moduleTranslation.containsKey(module))
			langs = moduleTranslation.get(module);
		else
			moduleTranslation.put(module, langs = new HashMap<String, PC_Property>());
		PC_Property translation;
		if(langs.containsKey(lang))
			translation = langs.get(lang);
		else
			langs.put(lang, translation = new PC_Property(null));
		for(PC_Struct3<String, String, String[]> trans:translations){
			if(trans.a.startsWith("tile.") || trans.a.startsWith("item.")){
				if(!trans.a.endsWith(".name")){
					trans.a += ".name";
				}
			}
			
			mod_PowerCraft.addStringLocalization(trans.a, lang, translation.getString(trans.a, trans.b, trans.c));

		}
	}
	
	@Override
	protected void iLoadLanguage(PC_IModule module){
		final PC_IModule m = module;
		File folder = new File(Minecraft.getMinecraftDir(), GameInfo.getPowerCraftFile() + "lang/");

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
				
				PC_Property prop = PC_Property.loadFromFile(new FileInputStream(folder.getCanonicalPath()+ "/" + filename));
				
				HashMap<String, PC_Property> langs;
				if(moduleTranslation.containsKey(module))
					langs = moduleTranslation.get(module);
				else
					moduleTranslation.put(module, langs = new HashMap<String, PC_Property>());
				PC_Property translation;
				if(langs.containsKey(language))
					translation = langs.get(language);
				else
					langs.put(language, translation = new PC_Property(null));
				
				translation.replaceWith(prop);
				
				updateLangRegistry();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		PC_Logger.finer("Translations loaded.");
	}
	
	private void updateLangRegistry() {
		for(HashMap<String, PC_Property> e1:moduleTranslation.values()){
			for(Entry<String, PC_Property> e2:e1.entrySet()){
				PC_Property conf = e2.getValue();
				String lang = e2.getKey();
				
				registerLang(lang, "", conf);
				
			}
		}
	}

	private void registerLang(String lang, String key, PC_Property prop){
		if(prop.hasChildren()){
			for(Entry<String, PC_Property> e:prop.getPropertys().entrySet()){
				if(key.equals("")){
					registerLang(lang, e.getKey(), e.getValue());
				}else{
					registerLang(lang, key + "." + e.getKey(), e.getValue());
				}
			}
		}else{
			mod_PowerCraft.addStringLocalization(key, lang, prop.getString());
		}
	}
	
	@Override
	protected void iSaveLanguage(PC_IModule module){
		if(!moduleTranslation.containsKey(module))
			return;
		Set<Entry<String, PC_Property>> langs = moduleTranslation.get(module).entrySet();
		for(Entry<String, PC_Property> langEntry:langs){
			
			try {
				File f = new File(GameInfo.getPowerCraftFile(), "lang");
				if(!f.exists())
					f.mkdirs();
				f = new File(f, langEntry.getKey() + "-" +  module.getName() + ".lang");
				if(!f.exists())
					f.createNewFile();
				
				langEntry.getValue().save(new FileOutputStream(f));
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	@Override
	protected void iPlaySound(double x, double y, double z, String sound, float soundVolume, float pitch){
		World world = mc().theWorld;
		if(GameInfo.isSoundEnabled() && world!=null && mc().renderViewEntity!=null){
			world.playSound(x, y, z, sound, soundVolume, pitch, false);
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
				mc().displayGuiScreen(new PC_GresGui((PC_GresBaseWithInventory)ValueWriting.createClass(c, new Class[]{EntityPlayer.class, Object[].class}, new Object[]{player, o})));
				player.openContainer.windowId = guiID;
			}else{
				mc().displayGuiScreen(new PC_GresGui((PC_IGresClient)ValueWriting.createClass(c, new Class[]{EntityPlayer.class, Object[].class}, new Object[]{player, o})));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player){
		return (EnumGameType)ValueWriting.getPrivateValue(PlayerControllerMP.class, mc().playerController, 11);
	}
	
	@Override
	protected void iChatMsg(String msg, boolean clear){
		if (clear) {
			 mc().ingameGUI.getChatGUI().func_73761_a();
		}
		mc().thePlayer.addChatMessage(msg);
	}
	
	@Override
	protected boolean iIsPlacingReversed(EntityPlayer player){
		return Communication.isKeyPressed(mc().thePlayer, "keyReverse");
	}
	
	@Override
	protected File iGetMCDirectory(){
		return Minecraft.getMinecraftDir();
	}
	
	@Override
	protected int iAddArmor(String name) {
		return ModLoader.addArmor(name);
	}
	
	@Override
	protected boolean iIsEntityFX(Entity entity) {
		return entity instanceof EntityFX;
	}
	
	@Override
	protected void iWatchForKey(String name, int key){
		ModLoader.registerKey(mod_PowerCraft.getInstance(), new KeyBinding(name, key), false);
	}
	
	public static void keyDown(String keyCode) {
		instance.handleIncomingPacket(mc().thePlayer, new Object[]{KEYEVENT, true, keyCode});
		PC_PacketHandler.sendToPacketHandler(mc().theWorld, "PacketUtils", KEYEVENT, true, keyCode);
	}

	public static void keyUp(String keyCode) {
		instance.handleIncomingPacket(mc().thePlayer, new Object[]{KEYEVENT, false, keyCode});
		PC_PacketHandler.sendToPacketHandler(mc().theWorld, "PacketUtils", KEYEVENT, false, keyCode);
	}
	
	@Override
	protected void iTileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass){
		bindTileEntitySpecialRenderer(tileEntityClass, PC_TileEntitySpecialRenderer.getInstance());
	}
	
	public static void bindTileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer specialRenderer){
		ModLoader.registerTileEntity(tileEntityClass, tileEntityClass.getName(), specialRenderer);
	}
	
	public static void registerEnitiyFX(Class<? extends EntityFX> fx){
		registerEnitiyFX(fx.getSimpleName(), fx);
	}
	
	public static void registerEnitiyFX(String name, Class<? extends EntityFX> fx){
		((PC_ClientUtils)instance).entityFX.put(name, fx);
	}
	
	@Override
	protected void iSpawnParticle(String name, Object[] o){
		
		if(!entityFX.containsKey(name)){
			System.err.println("no particle for \""+name+"\"");
			return;
		}
		
		Class c = entityFX.get(name);
		
		Class cp[] = new Class[o.length];
		for(int i=0; i<o.length; i++)
			cp[i] = o[i].getClass();
		
		Constructor cons = Coding.findBestConstructor(c, cp);
		if(cons==null){
			System.err.println("no best constructor for \""+name+"\"");
			return;
		}
		
		EntityFX fx=null;
		
		try {
			fx = (EntityFX)cons.newInstance(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(fx!=null){
			mc().effectRenderer.addEffect(fx);
		}
		
	}
	
}
