package powercraft.api.utils;

import java.io.File;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_RegistryClient;

public class PC_ClientUtils extends PC_Utils {
	
	private HashMap<String, Class<? extends EntityFX>> entityFX = new HashMap<String, Class<? extends EntityFX>>();
	
	private PC_ClientUtils() {
	}
	
	public static boolean create() {
		if (instance == null) {
			instance = new PC_ClientUtils();
			PC_RegistryClient.create();
			return true;
		}
		
		return false;
	}
	
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}
	
	@Override
	protected World iGetWorldForDimension(int dimension) {
		IntegratedServer server = mc().getIntegratedServer();
		if (server != null) {
			return server.worldServerForDimension(dimension);
		}
		return mc().theWorld;
	}
	
	@Override
	protected boolean iIsClient() {
		return true;
	}
	
	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {
		return PC_ReflectHelper.getValue(PlayerControllerMP.class, mc().playerController, 10, EnumGameType.class);
	}
	
	@Override
	protected File iGetMCDirectory() {
		return mc().mcDataDir;
	}
	
	@Override
	protected boolean iIsEntityFX(Entity entity) {
		return entity instanceof EntityFX;
	}
	
	public static void registerEnitiyFX(Class<? extends EntityFX> fx) {
		registerEnitiyFX(fx.getSimpleName(), fx);
	}
	
	public static void registerEnitiyFX(String name, Class<? extends EntityFX> fx) {
		((PC_ClientUtils) instance).entityFX.put(name, fx);
	}
	
	@Override
	protected void iSpawnParticle(String name, Object[] o) {
		
		if (!entityFX.containsKey(name)) {
			System.err.println("no particle for \"" + name + "\"");
			return;
		}
		
		EntityFX fx = PC_ReflectHelper.create(entityFX.get(name), o);
		
		if (fx != null) {
			mc().effectRenderer.addEffect(fx);
		}
		
	}
	
	@Override
	protected void iChatMsg(String tr) {
		mc().ingameGUI.getChatGUI().printChatMessage(tr);
	}
	
}
