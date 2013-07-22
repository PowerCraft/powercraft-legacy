package powercraft.api;


import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumGameType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class PC_ClientUtils extends PC_Utils {

	public PC_ClientUtils() {

		instance = this;
		PC_Renderer.create();
		TickRegistry.registerTickHandler(new PC_TickHandler(), Side.CLIENT);
	}


	public static Minecraft mc() {

		return Minecraft.getMinecraft();
	}


	@Override
	protected File iGetPowerCraftFile() {

		return new File(mc().mcDataDir, "PowerCraft");
	}


	@Override
	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {

		return PC_Reflection.getValue(PlayerControllerMP.class, mc().playerController, 10, EnumGameType.class);
	}


	public static ResourceLocation getResourceLocation(PC_Module module, String file) {

		return new ResourceLocation(module.getMetadata().modId.toLowerCase(), file);
	}

}
