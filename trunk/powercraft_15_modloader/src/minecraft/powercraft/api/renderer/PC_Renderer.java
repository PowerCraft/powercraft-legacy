package powercraft.api.renderer;

import net.minecraft.src.Block;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import powercraft.api.utils.PC_VecF;

public class PC_Renderer {
	
	protected static int renderId;
	protected static PC_Renderer renderer;
	
	public PC_Renderer() {
		
		renderer = this;
		renderId = 0;

	}
	
	public static int getRendererID() {
		return renderId;
	}
	
	protected void iTessellatorDraw() {
	}
	
	public static void tessellatorDraw() {
		renderer.iTessellatorDraw();
	}
	
	protected void iTessellatorStartDrawingQuads() {
	}
	
	public static void tessellatorStartDrawingQuads() {
		renderer.iTessellatorStartDrawingQuads();
	}
	
	protected void iTessellatorStartDrawing(int i) {
	}
	
	public static void tessellatorStartDrawing(int i) {
		renderer.iTessellatorStartDrawing(i);
	}
	
	protected void iTessellatorSetColorOpaque_I(int i) {
	}
	
	public static void tessellatorSetColorOpaque_I(int i) {
		PC_Renderer.renderer.iTessellatorSetColorOpaque_I(i);
	}
	
	protected void iTessellatorSetColor(int r, int g, int b, int a) {
	}
	
	public static void tessellatorSetColor(int r, int g, int b, int a) {
		PC_Renderer.renderer.iTessellatorSetColor(r, g, b, a);
	}
	
	protected void iTessellatorAddVertex(double x, double y, double z) {
	}
	
	public static void tessellatorAddVertex(double x, double y, double z) {
		PC_Renderer.renderer.iTessellatorAddVertex(x, y, z);
	}
	
	protected void iBindTexture(String texture) {
	};
	
	public static void bindTexture(String texture) {
		PC_Renderer.renderer.iBindTexture(texture);
	};
	
	protected void iRenderStandardBlock(Object renderer, Block block, int x, int y, int z) {
	};
	
	public static void renderStandardBlock(Object renderer, Block block, int x, int y, int z) {
		PC_Renderer.renderer.iRenderStandardBlock(renderer, block, x, y, z);
	}
	
	protected void iRenderInvBox(Object renderer, Block block, int metadata) {
	}
	
	public static void renderInvBox(Object renderer, Block block, int metadata) {
		PC_Renderer.renderer.iRenderInvBox(renderer, block, metadata);
	}
	
	protected void iRenderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
	}
	
	public static void renderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		PC_Renderer.renderer.iRenderBlockRotatedBox(world, x, y, z, block, modelId, renderer);
	};
	
	protected void iRenderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer) {
	}
	
	public static void renderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer) {
		PC_Renderer.renderer.iRenderInvBlockRotatedBox(block, metadata, modelID, renderer);
	}
	
	protected void iRenderInvBoxWithTexture(Object renderer, Block block, Icon icon) {
	}
	
	public static void renderInvBoxWithTexture(Object renderer, Block block, Icon icon) {
		PC_Renderer.renderer.iRenderInvBoxWithTexture(renderer, block, icon);
	}
	
	protected void iRenderInvBoxWithTextures(Object renderer, Block block, Icon[] icon) {
	}
	
	public static void renderInvBoxWithTextures(Object renderer, Block block, Icon[] icon) {
		PC_Renderer.renderer.iRenderInvBoxWithTextures(renderer, block, icon);
	}
	
	protected void iRenderBlockByRenderType(Object renderer, Block block, int x, int y, int z) {
	}
	
	public static void renderBlockByRenderType(Object renderer, Block block, int x, int y, int z) {
		PC_Renderer.renderer.iRenderBlockByRenderType(renderer, block, x, y, z);
	}
	
	public static void glColor3f(float r, float g, float b) {
		glColor4f(r, g, b, 1.0f);
	}
	
	protected void iglColor4f(float r, float g, float b, float a) {
	}
	
	public static void glColor4f(float red, float green, float blue, float a) {
		PC_Renderer.renderer.iglColor4f(red, green, blue, a);
	}
	
	protected void iglPushMatrix() {
	}
	
	public static void glPushMatrix() {
		PC_Renderer.renderer.iglPushMatrix();
	}
	
	protected void iglPopMatrix() {
	}
	
	public static void glPopMatrix() {
		PC_Renderer.renderer.iglPopMatrix();
	}
	
	protected void iglTranslatef(float x, float y, float z) {
	}
	
	public static void glTranslatef(float x, float y, float z) {
		PC_Renderer.renderer.iglTranslatef(x, y, z);
	}
	
	protected void iglRotatef(float angel, float x, float y, float z) {
	}
	
	public static void glRotatef(float angel, float x, float y, float z) {
		PC_Renderer.renderer.iglRotatef(angel, x, y, z);
	}
	
	protected void iglScalef(float x, float y, float z) {
	}
	
	public static void glScalef(float x, float y, float z) {
		PC_Renderer.renderer.iglScalef(x, y, z);
	}
	
	protected void iglEnable(int i) {
	}
	
	public static void glEnable(int i) {
		PC_Renderer.renderer.iglEnable(i);
	}
	
	protected void iglDisable(int i) {
	}
	
	public static void glDisable(int i) {
		PC_Renderer.renderer.iglDisable(i);
	}
	
	protected void iglBlendFunc(int i, int j) {
	}
	
	public static void glBlendFunc(int i, int j) {
		PC_Renderer.renderer.iglBlendFunc(i, j);
	}
	
	protected void iglNormal3f(float x, float y, float z) {
	}
	
	public static void glNormal3f(float x, float y, float z) {
		PC_Renderer.renderer.iglNormal3f(x, y, z);
	}
	
	protected void iglDepthMask(boolean state) {
	}
	
	public static void glDepthMask(boolean state) {
		PC_Renderer.renderer.iglDepthMask(state);
	}
	
	protected void irenderEntityLabelAt(String label, PC_VecF realPos, int viewDistance, float yOffset, double x, double y, double z) {
	}
	
	public static void renderEntityLabelAt(String label, PC_VecF realPos, int viewDistance, float yOffset, double x, double y, double z) {
		PC_Renderer.renderer.irenderEntityLabelAt(label, realPos, viewDistance, yOffset, x, y, z);
	}
	
	protected FontRenderer igetFontRenderer() {
		return null;
	}
	
	public static FontRenderer getFontRenderer() {
		return PC_Renderer.renderer.igetFontRenderer();
	}
	
}
