package powercraft.management.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_PowerCraft;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Logger;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_TileEntitySpecialRenderer;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.gres.PC_GresContainerGui;
import powercraft.management.gres.PC_GresGui;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PC_RegistryClient extends PC_RegistryServer {
	
	protected PC_RegistryClient(){}
	
	public static boolean create(){
		if(instance==null){
			instance = new PC_RegistryClient();
			return true;
		}
		return false;
	}
	
	
	private HashMap<PC_IModule, HashMap<String, PC_Property>> moduleTranslation = new HashMap<PC_IModule, HashMap<String, PC_Property>>();

	@Override
	protected void registerLanguage(PC_IModule module, String lang,
			LangEntry[] translations) {
		HashMap<String, PC_Property> langs;
		if (moduleTranslation.containsKey(module))
			langs = moduleTranslation.get(module);
		else
			moduleTranslation.put(module,
					langs = new HashMap<String, PC_Property>());
		PC_Property translation;
		if (langs.containsKey(lang))
			translation = langs.get(lang);
		else
			langs.put(lang, translation = new PC_Property(null));
		for (PC_Struct3<String, String, String[]> trans : translations) {
			if (trans.a.startsWith("tile.") || trans.a.startsWith("item.")) {
				if (!trans.a.endsWith(".name")) {
					trans.a += ".name";
				}
			}

			mod_PowerCraft.addStringLocalization(trans.a, lang,
					translation.getString(trans.a, trans.b, trans.c));

		}
	}

	@Override
	protected void loadLanguage(PC_IModule module) {
		final PC_IModule m = module;
		File folder = new File(Minecraft.getMinecraftDir(),
				GameInfo.getPowerCraftFile() + "lang/");

		String[] files = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.matches("[a-z]{2,3}_[A-Z]{2,3}-" + m.getName()
						+ "[.]lang");
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

				PC_Property prop = PC_Property
						.loadFromFile(new FileInputStream(folder
								.getCanonicalPath() + "/" + filename));

				HashMap<String, PC_Property> langs;
				if (moduleTranslation.containsKey(module))
					langs = moduleTranslation.get(module);
				else
					moduleTranslation.put(module,
							langs = new HashMap<String, PC_Property>());
				PC_Property translation;
				if (langs.containsKey(language))
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
		for (HashMap<String, PC_Property> e1 : moduleTranslation.values()) {
			for (Entry<String, PC_Property> e2 : e1.entrySet()) {
				PC_Property conf = e2.getValue();
				String lang = e2.getKey();

				registerLang(lang, "", conf);

			}
		}
	}

	private void registerLang(String lang, String key, PC_Property prop) {
		if (prop.hasChildren()) {
			for (Entry<String, PC_Property> e : prop.getPropertys().entrySet()) {
				if (key.equals("")) {
					registerLang(lang, e.getKey(), e.getValue());
				} else {
					registerLang(lang, key + "." + e.getKey(), e.getValue());
				}
			}
		} else {
			mod_PowerCraft.addStringLocalization(key, lang, prop.getString());
		}
	}

	@Override
	protected void saveLanguage(PC_IModule module) {
		if (!moduleTranslation.containsKey(module))
			return;
		Set<Entry<String, PC_Property>> langs = moduleTranslation.get(module)
				.entrySet();
		for (Entry<String, PC_Property> langEntry : langs) {

			try {
				File f = new File(GameInfo.getPowerCraftFile(), "lang");
				if (!f.exists())
					f.mkdirs();
				f = new File(f, langEntry.getKey() + "-" + module.getName()
						+ ".lang");
				if (!f.exists())
					f.createNewFile();

				langEntry.getValue().save(new FileOutputStream(f));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	@Override
	protected void tileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass){
		ModLoader.registerTileEntity(tileEntityClass, tileEntityClass.getName(), PC_TileEntitySpecialRenderer.getInstance());
	}
	
	@Override
	protected void openGres(String name, EntityPlayer player, PC_TileEntity te, Object[]o){
		if(player!=null&&!player.worldObj.isRemote){
			super.openGres(name, player, te, o);
			return;
		}
		int guiID = 0;
		if(o!=null && o.length==1 && o[0] instanceof ObjectInputStream){
			ObjectInputStream input = (ObjectInputStream)o[0];
			try {
				name = (String)input.readObject();
				guiID = input.readInt();
				PC_VecI vec = (PC_VecI)input.readObject();
				if(vec!=null){
					te = GameInfo.getTE(player.worldObj, vec);
				}
				o = (Object[])input.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        Class c = PC_GresRegistry.getGui(name);

		try {
			if(PC_GresBaseWithInventory.class.isAssignableFrom(c)){
				PC_ClientUtils.mc().displayGuiScreen(new PC_GresContainerGui(te, (PC_GresBaseWithInventory)PC_ReflectHelper.create(c, player, te, o)));
				player.openContainer.windowId = guiID;
			}else{
				PC_ClientUtils.mc().displayGuiScreen(new PC_GresGui(te, (PC_IGresClient)PC_ReflectHelper.create(c, player, te, o)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void registerTexture(String texture){
		if(texture==null)
			return;
		ModLoader.getMinecraftInstance().renderEngine.getTexture(texture);
	}
	
	@Override
	protected void playSound(double x, double y, double z, String sound, float soundVolume, float pitch){
		World world = PC_ClientUtils.mc().theWorld;
		if(world!=null && PC_ClientUtils.mc().renderViewEntity!=null){
			world.playSound(x, y, z, sound, soundVolume, pitch, false);
		}
	}
	
	@Override
	protected void watchForKey(String name, int key) {
		ModLoader.registerKey(mod_PowerCraft.getInstance(), new KeyBinding(name, key), false);
	}
	
	public static void keyEvent(String keyCode, boolean state) {
		instance.handleIncomingPacket(PC_ClientUtils.mc().thePlayer, new Object[]{KEYEVENT, state, keyCode});
		PC_PacketHandler.sendToPacketHandler(PC_ClientUtils.mc().theWorld, "RegistryPacket", KEYEVENT, state, keyCode);
	}
	
}
