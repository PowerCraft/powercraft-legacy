package powercraft.core;

import net.minecraft.src.Block;

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
	};
	
	
}
