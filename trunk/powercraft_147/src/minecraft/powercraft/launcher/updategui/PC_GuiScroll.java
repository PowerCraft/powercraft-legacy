package powercraft.launcher.updategui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import powercraft.launcher.PC_LauncherClientUtils;

public class PC_GuiScroll {

	private List<ScrollElement> elements = new ArrayList<ScrollElement>();
	private int x;
	private int y;
	private int width;
	private int height;
	private ScrollElement activeElement;
	private float scroll;
	private Minecraft mc = PC_LauncherClientUtils.mc();
	private int my=-1;
	private boolean bar;
	
	public PC_GuiScroll(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public ScrollElement getActiveElement(){
		return activeElement;
	}
	
	public void add(ScrollElement element){
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		element.setWorldAndResolution(mc, resolution.getScaledWidth(), resolution.getScaledHeight());
		elements.add(element);
	}
	
	public int getTotalHeight(){
		int height=0;
		for(ScrollElement scrollElement:elements){
			height += scrollElement.getHeight() + 4;
		}
		return height;
	}
	
	public boolean displayScrollBar(){
		return getTotalHeight()>height-4;
	}
	
	private void bindAmountScrolled()
    {
        int var1 = this.getTotalHeight() - height + 4;

        if (var1 < 0)
        {
            var1 /= 2;
        }

        if (this.scroll < 0.0F)
        {
            this.scroll = 0.0F;
        }

        if (this.scroll > (float)var1)
        {
            this.scroll = (float)var1;
        }
    } 
	
	public void drawScreen(int par1, int par2, float par3){
		ScaledResolution resolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int scale = resolution.getScaleFactor();
		if(my!=-1){
			int nmy = resolution.getScaledHeight() - Mouse.getY() * resolution.getScaledHeight() / this.mc.displayHeight - 1;
			float scrollMultiplier = 1;
			if(bar){
				int h = getTotalHeight()-height+4;
	
	            if (h < 1)
	            {
	                h = 1;
	            }
	
	            int a = height*height / getTotalHeight();
	
	            if (a < 32)
	            {
	                a = 32;
	            }
	
	            if (a > height - 8)
	            {
	                a = height - 8;
	            }
	
	            scrollMultiplier /= (float)(height - a) / (float)h;
			}else{
				scrollMultiplier = -1;
			}
			scroll += (nmy-my)*scrollMultiplier;
			bindAmountScrolled();
			my = nmy;
		}
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var17 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV(x, y+height, 0.0D, (float)x / var17, (float)(y+height + (int)this.scroll) / var17);
        tessellator.addVertexWithUV(x+width, y+height, 0.0D, (float)(x+width) / var17, (float)(y+height + (int)this.scroll) / var17);
        tessellator.addVertexWithUV(x+width, y, 0.0D, (float)(x+width) / var17, (float)(y + (int)this.scroll) / var17);
        tessellator.addVertexWithUV(x, y, 0.0D, (float)x / var17, (float)(y + (int)this.scroll) / var17);
        tessellator.draw();
        
        int drawY=-(int)scroll;
        int elementWidth=width-8;
        
        for(ScrollElement scrollElement:elements){
        	scrollElement.setElementWidth(elementWidth-8);
        	int elementHeight = scrollElement.getHeight();
        	if(drawY+elementHeight>0){
        		GL11.glPushMatrix();
        		GL11.glTranslatef(x + 4, y + drawY + 4, 0);
        		GL11.glEnable(GL11.GL_SCISSOR_TEST);
        		GL11.glScissor(x * scale, mc.displayHeight - (y + height) * scale, width * scale, height * scale);
        		if(activeElement == scrollElement && activeElement.showSelection()){
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     GL11.glDisable(GL11.GL_TEXTURE_2D);
                     tessellator.startDrawingQuads();
                     tessellator.setColorOpaque_I(8421504);
                     tessellator.addVertexWithUV(-2, 2 + elementHeight, 0.0D, 0.0D, 1.0D);
                     tessellator.addVertexWithUV(elementWidth-6, 2 + elementHeight, 0.0D, 1.0D, 1.0D);
                     tessellator.addVertexWithUV(elementWidth-6, -2, 0.0D, 1.0D, 0.0D);
                     tessellator.addVertexWithUV(-2, -2, 0.0D, 0.0D, 0.0D);
                     tessellator.setColorOpaque_I(0);
                     tessellator.addVertexWithUV(-1, 1 + elementHeight, 0.0D, 0.0D, 1.0D);
                     tessellator.addVertexWithUV(elementWidth-7, 1 + elementHeight, 0.0D, 1.0D, 1.0D);
                     tessellator.addVertexWithUV(elementWidth-7, -1, 0.0D, 1.0D, 0.0D);
                     tessellator.addVertexWithUV(-1, -1, 0.0D, 0.0D, 0.0D);
                     tessellator.draw();
                     GL11.glEnable(GL11.GL_TEXTURE_2D);
        		}
        		int w = elementHeight * scale;
        		int bottom = mc.displayHeight - (Math.max(y + drawY + 4, y) + elementHeight) * scale;
        		if(height - drawY-4<elementHeight){
        			w = (height - drawY-4)*scale;
        			bottom += (elementHeight-(height - drawY - 4))*scale;
        		}
        		if(elementWidth>8 && w>0){
	        		GL11.glScissor((x + 4) * scale, bottom, (elementWidth - 8) * scale, w);
	        		scrollElement.drawScreen(par1-x, par2-y-drawY, par3);
        		}
        		GL11.glDisable(GL11.GL_SCISSOR_TEST);
        		GL11.glPopMatrix();
        	}
        	drawY += elementHeight + 4;
        }
        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte var20 = 4;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(x, y + var20, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x+width, y + var20, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(x+width, y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(x, y+height, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x+width, y+height, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(x+width, y+height - var20, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y+height - var20, 0.0D, 0.0D, 0.0D);
        tessellator.draw();

        if (displayScrollBar()){
        	int var13 = height*height / getTotalHeight();

            if (var13 < 32)
            {
                var13 = 32;
            }

            if (var13 > height - 8)
            {
                var13 = height - 8;
            }

            int var14 = (int)this.scroll * (height - var13) / (getTotalHeight() - height + 4) + y;

            if (var14 < y)
            {
                var14 = y;
            }

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(x+width-7, y+height, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, y+height, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, y, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(x+width-7, y, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV(x+width-7, (double)(var14 + var13), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, (double)(var14 + var13), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, (double)var14, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(x+width-7, (double)var14, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV(x+width-7, (double)(var14 + var13 - 1), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(x+width-1, (double)(var14 + var13 - 1), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(x+width-1, (double)var14, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(x+width-7, (double)var14, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }else{
	        tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV(x+width-7, y+height, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, y+height, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(x+width, y, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(x+width-7, y, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV(x+width-7, y+height - 1, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(x+width-1, y+height - 1, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(x+width-1, y, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(x+width-7, y, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        
	}
	
	public void keyTyped(char par1, int par2){
		if(activeElement!=null){
			activeElement.keyTyped(par1, par2);
		}
	}
	
	public void mouseClicked(int par1, int par2, int par3){
		
		par1 -= x;
		par2 -= y;
		if(!(par1>0 && par1<width && par2>0 && par2<height)){
			return;
		}
		if(displayScrollBar()){
			bar = par1>width-7;
			my = par2+y;
			if(bar){
				return;
			}
		}
		par1 -= 4;
		par2 -= 2;
		activeElement = null;
		int elementWidth=width-8;
		if(par1<0 && par1>elementWidth-8){
			return;
		}
		int cY=-(int)scroll;
		for(ScrollElement scrollElement:elements){
			if(cY<par2 && cY+scrollElement.getHeight()+4>par2){
				activeElement = scrollElement;
				break;
			}
			cY += scrollElement.getHeight() + 4;
		}
		if(activeElement!=null){
			activeElement.mouseClicked(par1, par2, par3);
		}
	}
	
	public void mouseMovedOrUp(int par1, int par2, int par3){
		if(my!=-1){
			my = -1;
			return;
		}
		if(activeElement!=null){
			activeElement.mouseMovedOrUp(par1, par2, par3);
		}
	}
	
	public void updateScreen() {
		for(ScrollElement scrollElement:elements){
			scrollElement.updateScreen();
		}
	}
	
	public static abstract class ScrollElement extends GuiScreen{
		
		protected int elementWidth;
		
		public void setElementWidth(int elementWidth){
			this.elementWidth = elementWidth;
		}
		
		public void keyTyped(char par1, int par2){
			super.keyTyped(par1, par2);
		}
		
		public void mouseClicked(int par1, int par2, int par3){
			super.mouseClicked(par1, par2, par3);
		}
		
		public void mouseMovedOrUp(int par1, int par2, int par3){
			super.mouseMovedOrUp(par1, par2, par3);
		}
		
		public abstract boolean showSelection();
		
		public abstract int getHeight();
		
	}
	
}
