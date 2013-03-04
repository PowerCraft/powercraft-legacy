package net.minecraft.src;

import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import powercraft.launcher.PC_Launcher;

public class mod_PowerCraft extends BaseMod {

	private static mod_PowerCraft instance;
	private static PC_Launcher launcher = new PC_Launcher();
	
	public static mod_PowerCraft getInstance() {
		return instance;
	}
	
	public mod_PowerCraft(){
		instance = this;
	}
	
	@Override
	public String getVersion() {
		return "3.5.0AlphaJ";
	}

	@Override
	public String getName(){
		return "PowerCraft";
	}
	
	@Override
	public String getPriorities(){
		return "after:*";
	}
	
	@Override
	public void load() {
		launcher.preInit();
		launcher.init();
	}
	
	@Override
	public void modsLoaded() {
		launcher.postInit();
	}

	@Override
	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
		launcher.callManagementMethod("generateNether", new Class<?>[]{World.class, Random.class, int.class, int.class}, new Object[]{world, random, chunkX, chunkZ});
	}

	@Override
    public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		launcher.callManagementMethod("generateSurface", new Class<?>[]{World.class, Random.class, int.class, int.class}, new Object[]{world, random, chunkX, chunkZ});
    }
	
	@Override
	public int addFuel(int var1, int var2){
		return (Integer) launcher.callManagementMethod("addFuel", new Class<?>[]{int.class, int.class}, new Object[]{var1, var2});
    }
	
	@Override
	public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId) {
		return (Boolean) launcher.callManagementMethod("renderWorldBlock", new Class<?>[]{RenderBlocks.class, IBlockAccess.class, int.class, int.class, Block.class, int.class},
				new Object[]{renderer, blockAccess, x, y, z, block, modelId});
	}

	@Override
	public void renderInvBlock(RenderBlocks renderer, Block block, int metadata, int modelId) {
		launcher.callManagementMethod("renderInvBlock", new Class<?>[]{RenderBlocks.class, Block.class, int.class, int.class}, new Object[]{renderer, block, metadata, modelId});
	}
	
	@Override
	public void keyboardEvent(KeyBinding kb) {
		launcher.callManagementMethod("keyboardEvent", new Class<?>[]{KeyBinding.class}, new Object[]{kb});
	}
	
	@Override
	public boolean onTickInGUI(float var1, Minecraft var2, GuiScreen var3) {
		return (Boolean)launcher.callManagementMethod("onTickInGUI", new Class<?>[]{float.class, Minecraft.class, GuiScreen.class}, new Object[]{var1, var2, var3});
	}
	
	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		return (Boolean)launcher.callManagementMethod("onTickInGame", new Class<?>[]{float.class, Minecraft.class}, new Object[]{var1, var2});
	}

	@Override
	public void serverCustomPayload(NetServerHandler netServerHandler, Packet250CustomPayload packet){
		launcher.callManagementMethod("serverCustomPayload", new Class<?>[]{NetServerHandler.class, Packet250CustomPayload.class}, new Object[]{netServerHandler, packet});
	}
	
	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload packet){
		launcher.callManagementMethod("clientCustomPayload", new Class<?>[]{NetServerHandler.class, Packet250CustomPayload.class}, new Object[]{var1, packet});
	}
	
	@Override
	public void clientConnect(NetClientHandler var1) {
		launcher.callManagementMethod("clientConnect", new Class<?>[]{NetClientHandler.class}, new Object[]{var1});
	}
	
	@Override
	public void addRenderer(Map map) {
		launcher.callManagementMethod("addRenderer", new Class<?>[]{Map.class}, new Object[]{map});
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		return (Packet23VehicleSpawn)launcher.callManagementMethod("getSpawnPacket", new Class<?>[]{Entity.class, int.class}, new Object[]{var1, var2});
	}

	@Override
	public Entity spawnEntity(int id, World world, double x, double y, double z) {
		return (Entity)launcher.callManagementMethod("spawnEntity", new Class<?>[]{int.class, World.class, double.class, double.class, double.class}, new Object[]{id, world, x, y, z});
	}
	
	@Override
	public void onItemPickup(EntityPlayer var1, ItemStack var2) {
		launcher.callManagementMethod("onItemPickup", new Class<?>[]{EntityPlayer.class, ItemStack.class}, new Object[]{var1, var2});
	}

	@Override
	public void clientChat(String var1) {
		launcher.callManagementMethod("clientChat", new Class<?>[]{String.class}, new Object[]{var1});
	}

	@Override
	public void serverChat(NetServerHandler var1, String var2) {
		launcher.callManagementMethod("serverChat", new Class<?>[]{NetServerHandler.class, String.class}, new Object[]{var1, var2});
	}

	@Override
	public void registerAnimation(Minecraft var1) {
		launcher.callManagementMethod("registerAnimation", new Class<?>[]{Minecraft.class}, new Object[]{var1});
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		launcher.callManagementMethod("clientDisconnect", new Class<?>[]{NetClientHandler.class}, new Object[]{var1});
	}

	@Override
	public void takenFromCrafting(EntityPlayer var1, ItemStack var2, IInventory var3) {
		launcher.callManagementMethod("clientDisconnect", new Class<?>[]{EntityPlayer.class, ItemStack.class, IInventory.class}, new Object[]{var1, var2, var3});
	}

	@Override
	public void takenFromFurnace(EntityPlayer var1, ItemStack var2) {
		launcher.callManagementMethod("clientDisconnect", new Class<?>[]{EntityPlayer.class, ItemStack.class}, new Object[]{var1, var2});
	}

	@Override
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2, int var3, int var4, int var5) {
		return (GuiContainer)launcher.callManagementMethod("getContainerGUI", new Class<?>[]{EntityClientPlayerMP.class, int.class, int.class, int.class, int.class}, new Object[]{var1, var2, var3, var4, var5});
	}

	public static String getMinecraftVersion(){
		return new CallableMinecraftVersion(null).minecraftVersion();
	}
	
}
