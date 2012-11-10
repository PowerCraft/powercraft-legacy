package powercraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderEngine;

public class PC_Renderer {

	protected static int render3dId;
	protected static int render2dId;
	protected static PC_Renderer renderer3d;
	protected static PC_Renderer renderer2d;
	protected boolean render3d;
	
	public PC_Renderer(boolean render3d){
		this.render3d = render3d;
		if(render3d){
			renderer3d = this;
			render3dId = 0;
		}else{
			renderer2d = this;
			render2dId = 0;
		}
	}
	
	public static int getRendererID(boolean renderer3d) {
		if(renderer3d)
			return render3dId;
		return render2dId;
	}
	
	protected void iTessellatorDraw(){};
	
	public static void tessellatorDraw(){
		renderer2d.iTessellatorDraw();
	}
	
	protected void iTessellatorStartDrawingQuads(){};
	
	public static void tessellatorStartDrawingQuads(){
		renderer2d.iTessellatorStartDrawingQuads();
	}
	
	protected void iBindTexture(String texture){};
	
	public static void bindTexture(String texture){
		renderer2d.iBindTexture(texture);
	};
	
	protected void iRenderStandardBlock(Object renderer, Block block, int x, int y, int z){};
	
	public static void renderStandardBlock(Object renderer, Block block, int x, int y, int z){
		renderer2d.iRenderStandardBlock(renderer, block, x, y, z);
	}

	protected void iRenderInvBox(Object renderer, Block block, int metadata){}
	
	public static void renderInvBox(Object renderer, Block block, int metadata) {
		renderer2d.iRenderInvBox(renderer, block, metadata);
	}

	protected void iRenderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer){}
	
	public static void renderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		renderer2d.iRenderBlockRotatedBox(world, x, y, z, block, modelId, renderer);
	};
	
	protected void iRenderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer){}
	
	public static void renderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer) {
		renderer2d.iRenderInvBlockRotatedBox(block, metadata, modelID, renderer);
	}
	
	protected void iRenderInvBoxWithTexture(Object renderer, Block block, int tectureID){}
	
	public static void renderInvBoxWithTexture(Object renderer, Block block, int tectureID) {
		renderer2d.iRenderInvBoxWithTexture(renderer, block, tectureID);
	}
	
	
	protected void iSwapTerrain(String filename) {}

	public static void swapTerrain(String filename) {
		renderer2d.iSwapTerrain(filename);
	}
	
	protected boolean iSwapTerrain(Block block) {
		if (block instanceof PC_Block && !block.getTextureFile().equalsIgnoreCase("/terrain.png")) {
			return true;
		}
		return false;
	}

	public static boolean swapTerrain(Block block) {
		return renderer2d.iSwapTerrain(block);
	}
	
	protected void iResetTerrain(boolean do_it) {}
	
	public static void resetTerrain(boolean do_it) {
		renderer2d.iResetTerrain(do_it);
	}
	
	protected void iglColor3f(float r, float g, float b) {}
	
	public static void glColor3f(float r, float g, float b) {
		renderer2d.iglColor3f(r, g, b);
	}
	
}
