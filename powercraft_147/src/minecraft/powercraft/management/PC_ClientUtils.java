package powercraft.management;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_RegistryClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class PC_ClientUtils extends PC_Utils {
	
	private HashMap<String, Class<? extends EntityFX>> entityFX = new HashMap<String, Class<? extends EntityFX>>();
	private PC_KeyHandler keyHandler;
	
	private PC_ClientUtils(){
		KeyBindingRegistry.registerKeyBinding(keyHandler = new PC_KeyHandler());
	}
	
	public static boolean create()
    {
        if (instance == null)
        {
        	instance = new PC_ClientUtils();
        	PC_RegistryClient.create();
            return true;
        }

        return false;
    }
	
	public static Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	@Override
	protected World iGetWorldForDimension(int dimension) {
		IntegratedServer server = mc().getIntegratedServer();
		if(server!=null){
			return server.worldServerForDimension(dimension);
		}
		return mc().theWorld;
	}
	
	@Override
	protected boolean client(){return true;}
	
	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player){
		return (EnumGameType)PC_ReflectHelper.getValue(PlayerControllerMP.class, mc().playerController, 11);
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
	
	public static void bindTileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer specialRenderer){
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
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
