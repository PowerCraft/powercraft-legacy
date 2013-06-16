package powercraft.weasel;

import net.minecraft.src.ModelRenderer;

public class PCws_WeaselModelCore extends PCws_WeaselModelBase {

	public PCws_WeaselModelCore(){
		
		model = new ModelRenderer[3];
		
		// the stone pad
		model[0] = new ModelRenderer(this, 9, 20);
		model[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// body
		model[1] = new ModelRenderer(this, 0, 0);
		model[1].addBox(-4F, -6F, -5F, 8, 2, 10, 0.0F);

		// legs
		model[2] = new ModelRenderer(this, 70, 0);
		model[2].addBox(-5F, -5F, -5F, 10, 3, 10, 0.0F);
		
		modelColorMark = new ModelRenderer[1];

		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -6.5F, 2F, 4, 1, 2, 0.0F);
		
	}
	
}
