package powercraft.api;


import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumGameType;
import powercraft.api.registries.PC_Registry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * Utils for client side
 * 
 * @author XOR
 *
 */
@SideOnly(Side.CLIENT)
public class PC_ClientUtils extends PC_Utils {

	/**
	 * Will be called from Forge
	 */
	public PC_ClientUtils() {

		PC_Renderer.create();
		TickRegistry.registerTickHandler(new PC_TickHandler(), Side.CLIENT);
		PC_Registry.createClientRegistry();
		
	}

	/**
	 * get the Minecraft instance
	 * @return the Minecraft instance
	 */
	public static Minecraft mc() {

		return Minecraft.getMinecraft();
	}

	/**
	 * get the PowerCraft file in the same folder as the mod folder is
	 * @return the file
	 */
	@Override
	protected File iGetPowerCraftFile() {

		return new File(mc().mcDataDir, "PowerCraft");
	}

	/**
	 * get the game type for a specific player
	 * @param player the player
	 * @return the game type
	 */
	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {

		return PC_Reflection.getValue(PlayerControllerMP.class, mc().playerController, 10, EnumGameType.class);
	}

	/**
	 * is this game running on client
	 * @return always yes
	 */
	@Override
	protected boolean iisClient(){
		return true;
	}
	
}
