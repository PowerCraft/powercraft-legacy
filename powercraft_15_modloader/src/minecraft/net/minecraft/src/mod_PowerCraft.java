package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import powercraft.launcher.PC_Launcher;
import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_PacketHandler;

/**
 * 
 * PowerCraft Main Class with will be constructed form forge
 * 
 * @author XOR
 * 
 */
public class mod_PowerCraft extends BaseMod {

	/**
	 * The LauncherUtils
	 */
	public static PC_LauncherUtils proxy;

	/**
	 * Mod instance
	 */
	private static mod_PowerCraft instance;

	/**
	 * Packet Handler
	 */
	private static PC_PacketHandler packetHandler;
	
	/**
	 * Autors
	 */
	public List<String> authorList;
	
	/**
	 * Credits
	 */
	public String credits;
	
	/**
	 * 
	 * get the Mod instance
	 * 
	 * @return the Mod instance
	 */
	public static mod_PowerCraft getInstance() {
		return instance;
	}

	public mod_PowerCraft() {
		proxy = new PC_LauncherClientUtils();
		packetHandler = new PC_PacketHandler();
		instance = this;
	}

	/**
	 * 
	 * get the powercraft version
	 * 
	 * @return powercraft version
	 */
	@Override
	public String getVersion() {
		return "3.5.2";
	}

	/**
	 * 
	 * get the powercraft name
	 * 
	 * @return powercraft name
	 */
	public String getName() {
		return "PowerCraft";
	}

	@Override
	public int addFuel(int var1, int var2) {
		return (Integer)PC_Launcher.callapiMethod("addFuel", 
				new Class<?>[]{int.class,  int.class}, 
				new Object[]{var1, var2});
	}

	@Override
	public void addRenderer(Map var1) {
		PC_Launcher.callapiMethod("addRenderer", 
				new Class<?>[]{Map.class}, 
				new Object[]{var1});
	}

	@Override
	public void generateNether(World var1, Random var2, int var3, int var4) {
		PC_Launcher.callapiMethod("generateNether", 
				new Class<?>[]{World.class, Random.class, int.class, int.class}, 
				new Object[]{var1, var2, var3, var4});
	}

	@Override
	public void generateSurface(World var1, Random var2, int var3, int var4) {
		PC_Launcher.callapiMethod("generateSurface", 
				new Class<?>[]{World.class, Random.class, int.class, int.class}, 
				new Object[]{var1, var2, var3, var4});
	}

	@Override
	public String getPriorities() {
		return "after:*";
	}

	@Override
	public void keyboardEvent(KeyBinding var1) {
		PC_Launcher.callapiMethod("keyboardEvent", 
				new Class<?>[]{KeyBinding.class}, 
				new Object[]{var1});
	}

	@Override
	public void load() {
		hackInfo();
		PC_Launcher.preInit();
		PC_Launcher.init();
	}

	@Override
	public void modsLoaded() {
		PC_Launcher.postInit();
	}

	@Override
	public void onItemPickup(EntityPlayer var1, ItemStack var2) {
		PC_Launcher.callapiMethod("onItemPickup", 
				new Class<?>[]{EntityPlayer.class, ItemStack.class}, 
				new Object[]{var1, var2});
	}

	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		return (Boolean)PC_Launcher.callapiMethod("onTickInGame", 
				new Class<?>[]{float.class, Minecraft.class}, 
				new Object[]{var1, var2});
	}

	@Override
	public boolean onTickInGUI(float var1, Minecraft var2, GuiScreen var3) {
		return (Boolean)PC_Launcher.callapiMethod("onTickInGUI", 
				new Class<?>[]{float.class, Minecraft.class, GuiScreen.class}, 
				new Object[]{var1, var2, var3});
	}

	@Override
	public void clientChat(String var1) {
		PC_Launcher.callapiMethod("clientChat", 
				new Class<?>[]{String.class}, 
				new Object[]{var1});
	}

	@Override
	public void serverChat(NetServerHandler var1, String var2) {
		PC_Launcher.callapiMethod("serverChat", 
				new Class<?>[]{NetServerHandler.class, String.class}, 
				new Object[]{var1, var2});
	}

	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		packetHandler.onPacketData(var1.getNetManager(), var2, PC_LauncherClientUtils.mc().thePlayer);
	}

	@Override
	public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		packetHandler.onPacketData(var1.netManager, var2, var1.playerEntity);
	}

	@Override
	public void registerAnimation(Minecraft var1) {
		PC_Launcher.callapiMethod("registerAnimation", 
				new Class<?>[]{Minecraft.class}, 
				new Object[]{var1});
	}

	@Override
	public void renderInvBlock(RenderBlocks var1, Block var2, int var3, int var4) {
		PC_Launcher.callapiMethod("renderInvBlock", 
				new Class<?>[]{RenderBlocks.class, Block.class, int.class, int.class}, 
				new Object[]{var1, var2, var3, var4});
	}

	@Override
	public boolean renderWorldBlock(RenderBlocks var1, IBlockAccess var2,
			int var3, int var4, int var5, Block var6, int var7) {
		return (Boolean)PC_Launcher.callapiMethod("renderWorldBlock", 
				new Class<?>[]{RenderBlocks.class, IBlockAccess.class, int.class, int.class, int.class, Block.class, int.class}, 
				new Object[]{var1, var2, var3, var4, var5, var6, var7});
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		PC_Launcher.callapiMethod("clientConnect", 
				new Class<?>[]{NetClientHandler.class}, 
				new Object[]{var1});
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		PC_Launcher.callapiMethod("clientDisconnect", 
				new Class<?>[]{NetClientHandler.class}, 
				new Object[]{var1});
	}

	@Override
	public void takenFromCrafting(EntityPlayer var1, ItemStack var2,
			IInventory var3) {
		PC_Launcher.callapiMethod("takenFromCrafting", 
				new Class<?>[]{EntityPlayer.class, ItemStack.class, IInventory.class}, 
				new Object[]{var1, var2, var3});
	}

	@Override
	public void takenFromFurnace(EntityPlayer var1, ItemStack var2) {
		PC_Launcher.callapiMethod("takenFromFurnace", 
				new Class<?>[]{EntityPlayer.class, ItemStack.class}, 
				new Object[]{var1, var2});
	}

	@Override
	public Entity spawnEntity(int var1, World var2, double var3, double var5,
			double var7) {
		return (Entity)PC_Launcher.callapiMethod("spawnEntity", 
				new Class<?>[]{int.class, World.class, double.class, double.class, double.class}, 
				new Object[]{var1, var2, var3, var5, var7});
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		return (Packet23VehicleSpawn)PC_Launcher.callapiMethod("getSpawnPacket", 
				new Class<?>[]{Entity.class, int.class}, 
				new Object[]{var1, var2});
	}

	public static String minecraftVersion() {
		return new CallableMinecraftVersion(null).minecraftVersion();
	}

	private void hackInfo() {
		authorList = new ArrayList<String>();
		authorList.add("XOR");
		authorList.add("Rapus");
		authorList.add("wolfhowl42");
		credits = "MightyPork, RxD, LOLerul2";
	}
	
	public static void addAuthor(String name) {
		instance.authorList.add(name);
	}

	public static void addCredit(String name) {
		instance.credits += ", "+name;
	}
	
	public String getLogoFile(){
		return "/textures/PowerCraft.png";
	}
	
	public List<String> getAuthors(){
		return authorList;
	}
	
	public String getCredits(){
		return credits;
	}
	
	public String getLink(){
		return "http://powercrafting.net/";
	}
	
	public String getDescription(){
		return "";
	}
	
}
