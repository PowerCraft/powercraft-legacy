package powercraft.api.hooks;

import java.io.File;
import java.util.EnumSet;
import java.util.Random;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.renderer.PC_OverlayRenderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.launcher.PC_Logger;

public class PC_ClientHooks implements ITickHandler {

	private static boolean started;
	private static GuiScreen lastGuiScreen;
	private static Random rand = new Random();
	
	public static void registerClientHooks(){
		registerMinecraftSaverHook();
	}
	
	private static void registerMinecraftSaverHook(){
		Minecraft mc = PC_ClientUtils.mc();
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44, new PC_SaveConverterHook(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}

	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}
	
	private static void guiChangeHook(GuiScreen guiScreen){
		if(guiScreen instanceof GuiMainMenu){
			PC_Logger.finest("Hacking main menu splashes");
			if (rand.nextInt(2) == 0) {
				try {
					PC_ReflectHelper.setValue(GuiMainMenu.class, guiScreen, 2, getRandomSplash(), String.class);
				} catch (Throwable t) {}
			}
		}
	}
	
	private static void startupHook(Minecraft mc){
		String useUserName = PC_GlobalVariables.useUserName;
		if(!useUserName.equals("")){
			PC_ClientUtils.mc().session.username = useUserName;
		}
		mc.ingameGUI = new PC_OverlayRenderer(mc);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		GuiScreen guiScreen = mc.currentScreen;
		if(guiScreen!=lastGuiScreen){
			guiChangeHook(guiScreen);
			lastGuiScreen = guiScreen;
		}
		if(!started){
			startupHook(mc);
			started = true;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "PC_Hooks";
	}
	
}
