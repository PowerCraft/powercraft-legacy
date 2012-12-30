package powercraft.weasel;

import net.minecraft.client.model.ModelRenderer;

public class PCws_WeaselModelDisplay extends PCws_WeaselModelBase {

	public PCws_WeaselModelDisplay(){
		model = new ModelRenderer[4];
		modelColorMark = new ModelRenderer[1];
	
		// the bottom pad
		model[0] = new ModelRenderer(this, 85, 27);
		model[0].addBox(-5F, -1F, -5F, 10, 1, 10, 0.0F);
	
		// the connection piece
		model[1] = new ModelRenderer(this, 86, 16);
		model[1].addBox(-1F, -2F, -1F, 2, 1, 2, 0.0F);
	
		// display body
		model[2] = new ModelRenderer(this, 58, 20);
		model[2].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);
	
		// screen
		model[3] = new ModelRenderer(this, 0, 64);
		model[3].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);
	
		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);
	}

	@Override
	public void renderText(PCws_TileEntityWeasel te) {
		// TODO Auto-generated method stub
		super.renderText(te);
	}
	
	
	
}
