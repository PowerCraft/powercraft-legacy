package powercraft.weasel;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelRenderer;
import powercraft.management.renderer.PC_Renderer;

public class PCws_WeaselModelTouchscreen extends PCws_WeaselModelBase {

	public PCws_WeaselModelTouchscreen(){
		model = new ModelRenderer[1];
		modelColorMark = new ModelRenderer[1];
		
		// the bottom pad
		model[0] = new ModelRenderer(this, 0, 81);
		//legs
		model[0].addBox(-5F, -1F, -5F, 1, 1, 10, 0.0F);
		model[0].addBox(4F, -1F, -5F, 1, 1, 10, 0.0F);

		// connections
		model[0].addBox(-5F, -2F, -0.5F, 1, 1, 1, 0.0F);
		model[0].addBox(4F, -2F, -0.5F, 1, 1, 1, 0.0F);

		// top,bottom
		model[0].addBox(-8F, -3F, -0.5F, 16, 1, 1, 0.0F);
		model[0].addBox(-8F, -16F, -0.5F, 16, 1, 1, 0.0F);

		// sides
		model[0].addBox(-8F, -15F, -0.5F, 1, 12, 1, 0.0F);
		model[0].addBox(7F, -15F, -0.5F, 1, 12, 1, 0.0F);

		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);
	}

	@Override
	public void renderText(PCws_TileEntityWeasel te, FontRenderer fontrenderer) {

		PC_Renderer.glTranslatef(0.0f, 0.0625f * 9, 0.0f);
		PC_Renderer.glScalef(0.0625f * 14 / PCws_WeaselPluginTouchscreen.WIDTH, -0.0625f * 12 / PCws_WeaselPluginTouchscreen.HEIGHT, 0.01111111f);
		PC_Renderer.glDisable(0xde1); // GL_TEXTURE_2D

		PC_Renderer.tessellatorStartDrawingQuads();
		double posX, posY, pixelW, pixelH;
		int color;
		pixelW = 1.0D;
		pixelH = 1.0D;
		for (int j = 0; j < PCws_WeaselPluginTouchscreen.HEIGHT; j++) {
			for (int i = 0; i < PCws_WeaselPluginTouchscreen.WIDTH; i++) {
				if(te.getData("pic["+i+"]["+j+"]")==null)
					break;
				color = (Integer)te.getData("pic["+i+"]["+j+"]");
				if (color != -1) {
					posX = i - PCws_WeaselPluginTouchscreen.WIDTH * 0.5;
					posY = j - PCws_WeaselPluginTouchscreen.HEIGHT * 0.5;
					PC_Renderer.tessellatorSetColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255);
					PC_Renderer.tessellatorAddVertex(posX + pixelW, posY, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX, posY, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX, posY + pixelH, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX + pixelW, posY + pixelH, 0.0D);

					PC_Renderer.tessellatorAddVertex(posX, posY, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX + pixelW, posY, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX + pixelW, posY + pixelH, 0.0D);
					PC_Renderer.tessellatorAddVertex(posX, posY + pixelH, 0.0D);

				}
			}
		}
		
		PC_Renderer.tessellatorDraw();

		PC_Renderer.glEnable(0xde1); // GL_TEXTURE_2D
	}
	
	
	
}
