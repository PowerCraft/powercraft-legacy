package powercraft.management;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;

import org.lwjgl.opengl.GL11;

import powercraft.management.PC_Utils.MSG;

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
