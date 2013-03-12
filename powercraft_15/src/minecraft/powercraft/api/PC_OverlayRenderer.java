package powercraft.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

import org.lwjgl.opengl.GL11;

import powercraft.api.registry.PC_MSGRegistry;

public class PC_OverlayRenderer extends GuiIngame {

	public PC_OverlayRenderer(Minecraft minecraft) {
		super(minecraft);
	}

	@Override
	public void renderGameOverlay(float ts, boolean screen, int mx, int my) {
		PC_ClientUtils.mc().entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_RENDER_OVERLAY, this, ts,
				screen, mx, my);
		super.renderGameOverlay(ts, screen, mx, my);
	}

}
