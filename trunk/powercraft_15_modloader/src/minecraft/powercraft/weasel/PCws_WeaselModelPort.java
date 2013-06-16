package powercraft.weasel;

import net.minecraft.src.ModelRenderer;

public class PCws_WeaselModelPort extends PCws_WeaselModelBase {

	public PCws_WeaselModelPort(){
		
		model = new ModelRenderer[3];
		modelColorMark = new ModelRenderer[1];
		
		// the stone pad
		model[0] = new ModelRenderer(this, 21, 0);
		model[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// the piece with light, on
		model[1] = new ModelRenderer(this, 0, 14);
		model[1].addBox(-3F, -5F, -3F, 6, 2, 6, 0.0F);

		// the piece with light, off
		model[2] = new ModelRenderer(this, 0, 23);
		model[2].addBox(-3F, -5F, -3F, 6, 2, 6, 0.0F);

		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -4.5F, -6F, 4, 1, 2, 0.0F);
	}
	
}
