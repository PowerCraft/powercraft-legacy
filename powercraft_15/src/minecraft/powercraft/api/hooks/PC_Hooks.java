package powercraft.api.hooks;

import java.util.EnumSet;
import java.util.Random;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.monster.EntityEnderman;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.renderer.PC_OverlayRenderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.launcher.PC_Logger;

public class PC_Hooks {
	
	public static void registerHooks() {
		fixEnderman();
	}
	
	private static void fixEnderman(){
		boolean[] carriableBlocks = (boolean[])PC_ReflectHelper.getValue(EntityEnderman.class, EntityEnderman.class, 0, boolean[].class);
		boolean[] newCarriableBlocks = new boolean[Block.blocksList.length];
		for(int i=0; i<carriableBlocks.length; i++){
			newCarriableBlocks[i] = carriableBlocks[i];
		}
		PC_ReflectHelper.setValue(EntityEnderman.class, EntityEnderman.class, 0, newCarriableBlocks, boolean[].class);
	}
	
}
