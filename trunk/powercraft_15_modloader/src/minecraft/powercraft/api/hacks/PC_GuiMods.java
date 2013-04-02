package powercraft.api.hacks;

import java.util.List;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import powercraft.api.registry.PC_LangRegistry;

public class PC_GuiMods extends GuiScreen {
	
	private GuiScreen parentScreen;
	
	private PC_GuiModScroll scroll;
	
	private String screenTitle = "Mods";
	
	public PC_GuiMods(PC_GuiMainMenuHack parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	@Override
	public void initGui() {
		scroll = new PC_GuiModScroll(10, 10, 100, height - 20);
		buttonList.add(new GuiSmallButton(1, this.width / 2 - 75, this.height - 38, PC_LangRegistry.tr("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.id == 1) {
			mc.displayGuiScreen(parentScreen);
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, 110 + (this.width - 110) / 2, 15, 16777215);
		super.drawScreen(par1, par2, par3);
		scroll.drawScreen(par1, par2, par3);
		PC_ModInfo mi = scroll.getActiveModInfo();
		if (mi != null) {
			int y = 30;
			if (mi.logoFile != null) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				this.mc.renderEngine.bindTexture(mi.logoFile);
				int texWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
				int texHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
				double scaleX = texWidth / 200.0;
				double scaleY = texHeight / 65.0;
				double scale = 1.0;
				if (scaleX > 1 || scaleY > 1) {
					scale = 1.0 / Math.max(scaleX, scaleY);
				}
				texWidth *= scale;
				texHeight *= scale;
				int top = 32;
				Tessellator tess = Tessellator.instance;
				tess.startDrawingQuads();
				tess.addVertexWithUV(120, top + texHeight, zLevel, 0, 1);
				tess.addVertexWithUV(120 + texWidth, top + texHeight, zLevel, 1, 1);
				tess.addVertexWithUV(120 + texWidth, top, zLevel, 1, 0);
				tess.addVertexWithUV(120, top, zLevel, 0, 0);
				tess.draw();
				GL11.glDisable(GL11.GL_BLEND);
				y += texHeight;
			}
			drawString(fontRenderer, mi.name, 120, y += 10, 16777215);
			drawString(fontRenderer, mi.version, 120, y += 10, 16777215);
			if (mi.credits != null) {
				drawString(fontRenderer, "Credits: " + mi.credits, 120, y += 10, 16777215);
			}
			String authors = "Authors: ";
			if (mi.authors != null && mi.authors.size() > 0) {
				authors += mi.authors.get(0);
				for (int i = 1; i < mi.authors.size(); i++) {
					authors += ", " + mi.authors.get(i);
				}
			} else {
				authors += "Unknown";
			}
			drawString(fontRenderer, authors, 120, y += 10, 16777215);
			if (mi.link != null) {
				drawString(fontRenderer, "Link: " + mi.link, 120, y += 10, 16777215);
			}
			if (width - 130 > 10) {
				List<String> desc;
				if (mi.description != null) {
					desc = fontRenderer.listFormattedStringToWidth(mi.description, width - 130);
				} else {
					desc = fontRenderer.listFormattedStringToWidth("No Description", width - 130);
				}
				y += 10;
				for (String l : desc) {
					drawString(fontRenderer, l, 120, y += 10, 16777215);
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		scroll.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		scroll.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
		scroll.mouseMovedOrUp(par1, par2, par3);
	}
	
}
