package powercraft.launcher.updategui;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import powercraft.launcher.PC_LauncherClientUtils;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.update.PC_UpdateManager;
import powercraft.launcher.update.PC_UpdateManager.ModuleUpdateInfo;

public class PC_GuiUpdate extends GuiScreen {
	
	private static String launcherDownload = "http://www.powercrafting.net/wiki/?Welcome___Common_Information___Downloads";
	
	private static Minecraft smc = PC_LauncherClientUtils.mc();
	private static boolean stop;
	private static PC_GuiUpdate gui;
	
	private PC_GuiScrollModuleAndPackScroll scroll;
	private PC_GuiScrollVersions versionList;
	private PC_GuiScrollVersionInfo infoList;
	public GuiButton download;
	public GuiButton activate;
	public GuiButton delete;
	
	@Override
	public void initGui() {
		scroll = new PC_GuiScrollModuleAndPackScroll(10, 20, 100, height - 80);
		versionList = new PC_GuiScrollVersions(110, 20, 100, height - 80, scroll, this);
		infoList = new PC_GuiScrollVersionInfo(210, 20, width - 220, height - 80, versionList);
		buttonList.add(new GuiButton(1, 10, height - 28, 190, 20, StringTranslate.getInstance().translateKey("gui.done")));
		buttonList.add(download = new GuiButton(2, 220, height - 58, width - 240, 20, StringTranslate.getInstance().translateKey("Download")));
		buttonList.add(new GuiButton(3, 220, height - 28, width - 240, 20, StringTranslate.getInstance().translateKey("Where to watch for downloads")));
		buttonList.add(activate = new GuiButton(4, 220, height - 58, (width - 240) / 2, 20, StringTranslate.getInstance().translateKey("Activate")));
		buttonList.add(delete = new GuiButton(5, 220 + (width - 240) / 2, height - 58, (width - 240) / 2, 20, StringTranslate.getInstance().translateKey(
				"Delete")));
		buttonList.add(new GuiButton(6, 10, height - 58, 190, 20, StringTranslate.getInstance().translateKey("Ignore")));
		activate.drawButton = false;
		delete.drawButton = false;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		scroll.drawScreen(par1, par2, par3);
		versionList.drawScreen(par1, par2, par3);
		infoList.drawScreen(par1, par2, par3);
		drawCenteredString(fontRenderer, "PowerCraft Update Screen", width / 2, 4, 0xffffff);
		super.drawScreen(par1, par2, par3);
		
		if (PC_UpdateManager.newLauncher) {
			drawInfo("There is a new Launcher update", isMouseOverInfo(par1, par2));
		} else if (!hasAPI()) {
			drawInfo("Please download the API", false);
		} else if (PC_LauncherUtils.isDeveloperVersion()) {
			drawInfo("You are using the Developer Version", false);
		}
		
		drawTooltip(par1, par2, par3);
		
		PC_UpdateManager.lookForDirectoryChange();
	}
	
	private boolean hasAPI() {
		for (ModuleUpdateInfo mui : PC_UpdateManager.moduleList) {
			if (mui.module == null) {
				if (mui.xmlModule.getName().equals("Api")) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isMouseOverInfo(int par1, int par2) {
		return par1 > width - 120 && par2 < 32;
	}
	
	private void drawInfo(String info, boolean highLite) {
		if (highLite) {
			GL11.glColor4f(1.0F, 1.0F, 0.4F, 1.0F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		smc.renderEngine.bindTexture("/gui/inventory.png");
		drawTexturedModalRect(width - 120, 0, 0, 166, 120, 32);
		List<String> lines = fontRenderer.listFormattedStringToWidth(info, 100);
		if (lines.size() == 1) {
			fontRenderer.drawStringWithShadow(lines.get(0), width - 110, 11, highLite ? 0xFF4433 : 0xFFFFFF);
		} else if (lines.size() > 1) {
			fontRenderer.drawStringWithShadow(lines.get(0), width - 110, 6, highLite ? 0xFF4433 : 0xFFFFFF);
			fontRenderer.drawStringWithShadow(lines.get(1), width - 110, 16, highLite ? 0xFF4433 : 0xFFFFFF);
		}
	}
	
	private void drawTooltip(int par1, int par2, float par3) {
		List list = null;
		
		if (isMouseOverInfo(par1, par2)) {
			if (PC_UpdateManager.newLauncher) {
				list = Arrays.asList("Click to Download");
			}
		}
		if (list == null && par1 > 220 && par1 < width - 20 && par2 > height - 28 && par2 < height - 8) {
			list = Arrays.asList("Browser Download", "Directory");
		}
		
		if (list != null && list.size() > 0) {
			int l1 = 0;
			
			for (int i2 = 0; i2 < list.size(); i2++) {
				int k2 = fontRenderer.getStringWidth((String) list.get(i2));
				
				if (k2 > l1) {
					l1 = k2;
				}
			}
			
			int j2 = (par1) + 12;
			int l2 = par2 - 12;
			int i3 = l1;
			int j3 = 8;
			
			if (list.size() > 1) {
				j3 += 2 + (list.size() - 1) * 10;
			}
			
			zLevel = 300F;
			int k3 = 0xf0100010;
			drawGradientRect(j2 - 3, l2 - 4, j2 + i3 + 3, l2 - 3, k3, k3);
			drawGradientRect(j2 - 3, l2 + j3 + 3, j2 + i3 + 3, l2 + j3 + 4, k3, k3);
			drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, l2 + j3 + 3, k3, k3);
			drawGradientRect(j2 - 4, l2 - 3, j2 - 3, l2 + j3 + 3, k3, k3);
			drawGradientRect(j2 + i3 + 3, l2 - 3, j2 + i3 + 4, l2 + j3 + 3, k3, k3);
			int l3 = 0x505000ff;
			int i4 = (l3 & 0xfefefe) >> 1 | l3 & 0xff000000;
			drawGradientRect(j2 - 3, (l2 - 3) + 1, (j2 - 3) + 1, (l2 + j3 + 3) - 1, l3, i4);
			drawGradientRect(j2 + i3 + 2, (l2 - 3) + 1, j2 + i3 + 3, (l2 + j3 + 3) - 1, l3, i4);
			drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, (l2 - 3) + 1, l3, l3);
			drawGradientRect(j2 - 3, l2 + j3 + 2, j2 + i3 + 3, l2 + j3 + 3, i4, i4);
			
			for (int j4 = 0; j4 < list.size(); j4++) {
				String s = (String) list.get(j4);
				
				fontRenderer.drawStringWithShadow(s, j2, l2, -1);
				
				if (j4 == 0) {
					l2 += 2;
				}
				
				l2 += 10;
			}
			
			zLevel = 0.0F;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 1) {
			stop = true;
		} else if (par1GuiButton.id == 2) {
			PC_UpdateManager.download(versionList.getActiveVersionDownload());
		} else if (par1GuiButton.id == 3) {
			new PC_FileRequestThread();
		} else if (par1GuiButton.id == 4) {
			PC_UpdateManager.activateModule(versionList.getActiveModuleVersion());
		} else if (par1GuiButton.id == 5) {
			PC_UpdateManager.delete(versionList.getActiveModuleVersion());
		} else if (par1GuiButton.id == 6) {
			PC_UpdateManager.ignoreUpdates();
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		scroll.keyTyped(par1, par2);
		versionList.keyTyped(par1, par2);
		infoList.keyTyped(par1, par2);
		super.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (isMouseOverInfo(par1, par2)) {
			if (PC_UpdateManager.newLauncher) {
				PC_UpdateManager.openURL(launcherDownload);
			}
		}
		scroll.mouseClicked(par1, par2, par3);
		versionList.mouseClicked(par1, par2, par3);
		infoList.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		scroll.mouseMovedOrUp(par1, par2, par3);
		versionList.mouseMovedOrUp(par1, par2, par3);
		infoList.mouseMovedOrUp(par1, par2, par3);
		super.mouseMovedOrUp(par1, par2, par3);
	}
	
	public static void show(boolean requestDownloadTarget) {
		gui = new PC_GuiUpdate();
		ScaledResolution resolution = new ScaledResolution(smc.gameSettings, smc.displayWidth, smc.displayHeight);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		gui.setWorldAndResolution(smc, width, height);
		while (!stop) {
			if (smc.mcCanvas != null && !smc.isFullScreen() && (smc.mcCanvas.getWidth() != smc.displayWidth || smc.mcCanvas.getHeight() != smc.displayHeight)) {
				smc.displayWidth = smc.mcCanvas.getWidth();
				smc.displayHeight = smc.mcCanvas.getHeight();
				
				if (smc.displayWidth <= 0) {
					smc.displayWidth = 1;
				}
				
				if (smc.displayHeight <= 0) {
					smc.displayHeight = 1;
				}
				
				resolution = new ScaledResolution(smc.gameSettings, smc.displayWidth, smc.displayHeight);
				width = resolution.getScaledWidth();
				height = resolution.getScaledHeight();
				gui.setWorldAndResolution(smc, width, height);
				GL11.glViewport(0, 0, smc.displayWidth, smc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, resolution.getScaledWidth_double(), resolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			}
			gui.handleInput();
			gui.drawDefaultBackground();
			gui.drawScreen(Mouse.getX() * width / smc.displayWidth, height - Mouse.getY() * height / smc.displayHeight - 1, 0.0f);
			Display.update();
			Display.sync(60);
			if (Display.isCloseRequested()) {
				stop = true;
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			checkGLError("post gui render");
			if (requestDownloadTarget) {
				requestDownloadTarget = false;
				new PC_FileRequestThread();
			}
		}
		Display.sync(-1);
		gui = null;
		PC_UpdateManager.stopWatchDirectory();
		resolution = new ScaledResolution(smc.gameSettings, smc.displayWidth, smc.displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, resolution.getScaledWidth_double(), resolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, smc.displayWidth, smc.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator var2 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smc.renderEngine.getTexture("/title/mojang.png"));
		var2.startDrawingQuads();
		var2.setColorOpaque_I(16777215);
		var2.addVertexWithUV(0.0D, (double) smc.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double) smc.displayWidth, (double) smc.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double) smc.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var2.setColorOpaque_I(16777215);
		short var3 = 256;
		short var4 = 256;
		smc.scaledTessellator((resolution.getScaledWidth() - var3) / 2, (resolution.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		try {
			Display.swapBuffers();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkGLError(String par1Str) {
		int var2 = GL11.glGetError();
		
		if (var2 != 0) {
			String var3 = GLU.gluErrorString(var2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + par1Str);
			System.out.println(var2 + ": " + var3);
		}
	}
	
}
