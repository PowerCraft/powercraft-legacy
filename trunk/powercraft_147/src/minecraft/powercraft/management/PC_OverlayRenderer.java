package powercraft.management;

import java.util.EnumSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.MSG;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_OverlayRenderer extends GuiIngame {

	public PC_OverlayRenderer(Minecraft minecraft) {
		super(minecraft);
	}

	@Override
	public void renderGameOverlay(float ts, boolean screen, int mx, int my) {
		PC_ClientUtils.mc().entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		MSG.callAllMSG(PC_Utils.MSG_RENDER_OVERLAY, this, ts, screen, mx, my);
		super.renderGameOverlay(ts, screen, mx, my);
	}

}
